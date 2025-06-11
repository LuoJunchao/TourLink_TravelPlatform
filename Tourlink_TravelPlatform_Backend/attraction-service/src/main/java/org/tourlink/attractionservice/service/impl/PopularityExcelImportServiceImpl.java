package org.tourlink.attractionservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.tourlink.attractionservice.dto.ImportResultDTO;
import org.tourlink.attractionservice.entity.AttractionPopularity;
import org.tourlink.attractionservice.repository.AttractionPopularityRepository;
import org.tourlink.attractionservice.service.PopularityExcelImportService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 景点热度Excel导入服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PopularityExcelImportServiceImpl implements PopularityExcelImportService {

    private final AttractionPopularityRepository popularityRepository;
    private final PlatformTransactionManager transactionManager;

    @Override
    public ImportResultDTO importPopularityFromExcel(MultipartFile file) {
        ImportResultDTO result = new ImportResultDTO();
        result.setTotalCount(0);
        result.setSuccessCount(0);
        result.setFailCount(0);
        result.setErrors(new ArrayList<>());

        if (file.isEmpty()) {
            result.addError("文件为空");
            return result;
        }

        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !(originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xls"))) {
            result.addError("文件格式不正确，只支持.xlsx或.xls格式");
            return result;
        }

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            // 获取表头
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                result.addError("Excel文件格式不正确，缺少表头");
                return result;
            }

            // 解析表头
            Map<String, Integer> headerMap = parseHeader(headerRow);

            // 验证必需的列是否存在
            if (!headerMap.containsKey("景点ID")) {
                result.addError("缺少必需的列：景点ID");
                return result;
            }

            // 处理数据行
            int totalRows = sheet.getPhysicalNumberOfRows();
            result.setTotalCount(totalRows - 1); // 减去表头行

            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                try {
                    AttractionPopularity popularity = parsePopularityFromRow(row, headerMap);
                    // 使用TransactionTemplate为每条记录创建独立事务
                    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
                    transactionTemplate.execute(status -> {
                        try {
                            // 检查是否已存在该景点的热度记录
                            Optional<AttractionPopularity> existingOpt = 
                                popularityRepository.findByAttractionId(popularity.getAttractionId());
                            
                            if (existingOpt.isPresent()) {
                                // 更新现有记录
                                AttractionPopularity existing = existingOpt.get();
                                existing.setViewCount(popularity.getViewCount());
                                existing.setLikeCount(popularity.getLikeCount());
                                existing.setShareCount(popularity.getShareCount());
                                popularityRepository.save(existing);
                            } else {
                                // 创建新记录，不设置ID，让数据库自动生成
                                popularity.setPopularityId(null); // 确保ID为null，让数据库生成
                                popularityRepository.save(popularity);
                            }
                            result.setSuccessCount(result.getSuccessCount() + 1);
                            return null;
                        } catch (Exception e) {
                            status.setRollbackOnly();
                            throw e;
                        }
                    });
                } catch (NumberFormatException e) {
                    // 数字格式错误
                    result.setFailCount(result.getFailCount() + 1);
                    String errorMsg = e.getMessage();
                    if (errorMsg == null || !errorMsg.contains(":")) {
                        errorMsg = "数字格式错误: " + e.getMessage();
                    }
                    result.addError("第" + (i + 1) + "行导入失败: " + errorMsg);
                    log.error("导入第{}行数据失败 - 数字格式错误", i + 1, e);
                } catch (Exception e) {
                    // 其他错误
                    result.setFailCount(result.getFailCount() + 1);
                    result.addError("第" + (i + 1) + "行导入失败: " + e.getMessage());
                    log.error("导入第{}行数据失败", i + 1, e);
                }
            }

        } catch (IOException e) {
            result.addError("读取Excel文件失败: " + e.getMessage());
            log.error("读取Excel文件失败", e);
        }

        return result;
    }

    /**
     * 解析表头
     * @param headerRow 表头行
     * @return 表头映射
     */
    private Map<String, Integer> parseHeader(Row headerRow) {
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                headerMap.put(cell.getStringCellValue().trim(), i);
            }
        }
        return headerMap;
    }

    /**
     * 从行数据解析景点热度对象
     * @param row 数据行
     * @param headerMap 表头映射
     * @return 景点热度对象
     */
    private AttractionPopularity parsePopularityFromRow(Row row, Map<String, Integer> headerMap) {
        AttractionPopularity popularity = new AttractionPopularity();

        // 景点ID（必需）
        Integer attractionIdCol = headerMap.get("景点ID");
        if (attractionIdCol != null) {
            String attractionIdStr = getCellValueAsString(row.getCell(attractionIdCol));
            if (attractionIdStr == null || attractionIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("景点ID不能为空");
            }
            try {
                popularity.setAttractionId(Long.parseLong(attractionIdStr.trim()));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("景点ID格式错误: " + attractionIdStr);
            }
        } else {
            throw new IllegalArgumentException("缺少景点ID列");
        }

        // 浏览量（可选，默认为0）
        Integer viewCountCol = headerMap.get("浏览量");
        if (viewCountCol != null) {
            String viewCountStr = getCellValueAsString(row.getCell(viewCountCol));
            if (viewCountStr != null && !viewCountStr.trim().isEmpty()) {
                try {
                    popularity.setViewCount(Integer.parseInt(viewCountStr.trim()));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("浏览量格式错误: " + viewCountStr);
                }
            } else {
                popularity.setViewCount(0);
            }
        } else {
            popularity.setViewCount(0);
        }

        // 点赞量（可选，默认为0）
        Integer likeCountCol = headerMap.get("点赞量");
        if (likeCountCol != null) {
            String likeCountStr = getCellValueAsString(row.getCell(likeCountCol));
            if (likeCountStr != null && !likeCountStr.trim().isEmpty()) {
                try {
                    popularity.setLikeCount(Integer.parseInt(likeCountStr.trim()));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("点赞量格式错误: " + likeCountStr);
                }
            } else {
                popularity.setLikeCount(0);
            }
        } else {
            popularity.setLikeCount(0);
        }

        // 分享量（可选，默认为0）
        Integer shareCountCol = headerMap.get("分享量");
        if (shareCountCol != null) {
            String shareCountStr = getCellValueAsString(row.getCell(shareCountCol));
            if (shareCountStr != null && !shareCountStr.trim().isEmpty()) {
                try {
                    popularity.setShareCount(Integer.parseInt(shareCountStr.trim()));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("分享量格式错误: " + shareCountStr);
                }
            } else {
                popularity.setShareCount(0);
            }
        } else {
            popularity.setShareCount(0);
        }

        return popularity;
    }

    /**
     * 获取单元格值作为字符串
     * @param cell 单元格
     * @return 字符串值
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    double value = cell.getNumericCellValue();
                    // 检查是否为整数
                    if (value == Math.floor(value) && !Double.isInfinite(value)) {
                        return String.valueOf((int) value);
                    } else {
                        return String.valueOf(value);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
}
