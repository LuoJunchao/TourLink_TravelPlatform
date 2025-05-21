package org.tourlink.attractionservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tourlink.attractionservice.dto.ImportResultDTO;
import org.tourlink.attractionservice.entity.Attraction;
import org.tourlink.attractionservice.repository.AttractionRepository;
import org.tourlink.attractionservice.service.ExcelImportService;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Excel导入服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelImportServiceImpl implements ExcelImportService {

    private final AttractionRepository attractionRepository;

    /**
     * 从Excel文件导入景点数据
     * @param file Excel文件
     * @return 导入结果
     */
    @Override
    @Transactional
    public ImportResultDTO importAttractionsFromExcel(MultipartFile file) {
        ImportResultDTO result = ImportResultDTO.builder()
                .totalCount(0)
                .successCount(0)
                .failCount(0)
                .errors(new ArrayList<>())
                .build();

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

            // 处理数据行
            int totalRows = sheet.getPhysicalNumberOfRows();
            result.setTotalCount(totalRows - 1); // 减去表头行

            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                try {
                    Attraction attraction = parseAttractionFromRow(row, headerMap);
                    attractionRepository.save(attraction);
                    result.setSuccessCount(result.getSuccessCount() + 1);
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
     * 从行解析景点数据
     * @param row 数据行
     * @param headerMap 表头映射
     * @return 景点实体
     */
    private Attraction parseAttractionFromRow(Row row, Map<String, Integer> headerMap) {
        Attraction attraction = new Attraction();

        // 设置基本字段
        attraction.setProvince(getStringCellValue(row, headerMap, "省份"));
        attraction.setName(getStringCellValue(row, headerMap, "名称"));
        attraction.setStarLevel(getStringCellValue(row, headerMap, "星级"));

        // 设置评分
        String ratingStr = getStringCellValue(row, headerMap, "评分");
        if (ratingStr != null && !ratingStr.isEmpty()) {
            try {
                BigDecimal rating = new BigDecimal(ratingStr);
                // 验证评分范围（0-5）
                if (rating.compareTo(BigDecimal.ZERO) < 0 || rating.compareTo(new BigDecimal("5")) > 0) {
                    throw new NumberFormatException("评分必须在0到5之间: " + ratingStr);
                }
                // 确保只有一位小数
                rating = rating.setScale(1, RoundingMode.HALF_UP);
                attraction.setRating(rating);
                // 兼容旧代码
                attraction.setAverageRating(rating);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("评分格式不正确: " + ratingStr);
            }
        }

        // 设置价格
        String priceStr = getStringCellValue(row, headerMap, "价格");
        if (priceStr != null && !priceStr.isEmpty()) {
            try {
                attraction.setPrice(new BigDecimal(priceStr));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("价格格式不正确: " + priceStr);
            }
        }

        // 设置销量
        String salesVolumeStr = getStringCellValue(row, headerMap, "销量");
        if (salesVolumeStr != null && !salesVolumeStr.isEmpty()) {
            try {
                // 处理可能包含小数点的数值
                double salesVolumeDouble = Double.parseDouble(salesVolumeStr);
                attraction.setSalesVolume((int) salesVolumeDouble); // 转换为整数
            } catch (NumberFormatException e) {
                throw new NumberFormatException("销量格式不正确: " + salesVolumeStr);
            }
        }

        // 设置省/市/区
        attraction.setRegion(getStringCellValue(row, headerMap, "省/市/区"));

        // 设置坐标
        attraction.setCoordinates(getStringCellValue(row, headerMap, "坐标"));

        // 设置简介
        attraction.setDescription(getStringCellValue(row, headerMap, "简介"));

        // 设置是否免费（直接读取布尔值）
        Integer isFreeIndex = headerMap.get("是否免费");
        if (isFreeIndex != null) {
            Cell isFreeCell = row.getCell(isFreeIndex);
            if (isFreeCell != null) {
                if (isFreeCell.getCellType() == CellType.BOOLEAN) {
                    attraction.setIsFree(isFreeCell.getBooleanCellValue());
                } else if (isFreeCell.getCellType() == CellType.STRING) {
                    String isFreeStr = isFreeCell.getStringCellValue();
                    attraction.setIsFree("TRUE".equalsIgnoreCase(isFreeStr) || "是".equals(isFreeStr) || "1".equals(isFreeStr));
                } else {
                    attraction.setIsFree(false);
                }
            } else {
                attraction.setIsFree(false);
            }
        } else {
            attraction.setIsFree(false);
        }

        // 设置具体地址
        attraction.setAddress(getStringCellValue(row, headerMap, "具体地址"));

        // 设置标签
        String tagsStr = getStringCellValue(row, headerMap, "标签");
        if (tagsStr != null && !tagsStr.isEmpty()) {
            List<String> tags = Arrays.asList(tagsStr.split(","));
            attraction.setTags(tags);
        }

        // 设置推荐游玩时间（作为字符串）
        String recommendedPlayTimeStr = getStringCellValue(row, headerMap, "推荐游玩时间");
        if (recommendedPlayTimeStr != null && !recommendedPlayTimeStr.isEmpty()) {
            attraction.setRecommendedPlayTime(recommendedPlayTimeStr);
        }

        // 设置默认值
        attraction.setIsActive(true);
        attraction.setIsFeatured(false);
        attraction.setImages(new ArrayList<>());

        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        attraction.setCreatedTime(now);
        attraction.setUpdatedTime(now);

        // 设置默认值
        attraction.setViewCount(0);
        attraction.setReviewCount(0); // 兼容旧代码

        return attraction;
    }

    /**
     * 获取单元格字符串值
     * @param row 数据行
     * @param headerMap 表头映射
     * @param headerName 表头名称
     * @return 单元格值
     */
    private String getStringCellValue(Row row, Map<String, Integer> headerMap, String headerName) {
        Integer cellIndex = headerMap.get(headerName);
        if (cellIndex == null) {
            return null;
        }

        Cell cell = row.getCell(cellIndex);
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
                        // 保留原始格式
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
