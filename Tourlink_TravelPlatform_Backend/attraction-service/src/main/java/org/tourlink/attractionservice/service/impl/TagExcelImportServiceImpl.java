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
import org.tourlink.attractionservice.entity.Tag;
import org.tourlink.attractionservice.repository.TagRepository;
import org.tourlink.attractionservice.service.TagExcelImportService;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagExcelImportServiceImpl implements TagExcelImportService {

    private final TagRepository tagRepository;
    private final PlatformTransactionManager transactionManager;

    @Override
    public ImportResultDTO importTagsFromExcel(MultipartFile file) {
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

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !(originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xls"))) {
            result.addError("文件格式不正确，只支持.xlsx或.xls格式");
            return result;
        }

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
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
                    Tag tag = parseTagFromRow(row, headerMap);
                    // 使用TransactionTemplate为每个标签创建独立事务
                    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
                    transactionTemplate.execute(status -> {
                        try {
                            tagRepository.save(tag);
                            result.setSuccessCount(result.getSuccessCount() + 1);
                            return null;
                        } catch (Exception e) {
                            status.setRollbackOnly();
                            throw e;
                        }
                    });
                } catch (Exception e) {
                    result.setFailCount(result.getFailCount() + 1);
                    String errorMsg = e.getMessage();
                    if (errorMsg == null || !errorMsg.contains(":")) {
                        errorMsg = "数据格式错误: " + e.getMessage();
                    }
                    result.addError("第" + (i + 1) + "行导入失败: " + errorMsg);
                    log.error("导入第{}行数据失败", i + 1, e);
                }
            }

        } catch (IOException e) {
            result.addError("读取Excel文件失败: " + e.getMessage());
            log.error("读取Excel文件失败", e);
        }

        return result;
    }

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

    private Tag parseTagFromRow(Row row, Map<String, Integer> headerMap) {
        Tag tag = new Tag();

        // 设置标签名称
        String name = getStringCellValue(row, headerMap, "标签名称");
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("标签名称不能为空");
        }
        tag.setName(name.trim());

        // 设置标签类型
        String type = getStringCellValue(row, headerMap, "标签类型");
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("标签类型不能为空");
        }
        tag.setType(type.trim());

        return tag;
    }

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
                double value = cell.getNumericCellValue();
                if (value == Math.floor(value) && !Double.isInfinite(value)) {
                    return String.valueOf((int) value);
                } else {
                    return String.valueOf(value);
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
