package org.tourlink.attractionservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.tourlink.attractionservice.service.TagExcelTemplateService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class TagExcelTemplateServiceImpl implements TagExcelTemplateService {

    @Override
    public ByteArrayOutputStream generateTagImportTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("标签导入模板");

            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "标签名称", "标签类型"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256); // 设置列宽
            }

            // 添加示例数据
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue("美食"); // 标签名称
            exampleRow.createCell(1).setCellValue("类型"); // 标签类型

            // 添加第二个示例行
            Row exampleRow2 = sheet.createRow(2);
            exampleRow2.createCell(0).setCellValue("景区"); 
            exampleRow2.createCell(1).setCellValue("类型");

            // 写入到输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream;

        } catch (IOException e) {
            log.error("生成Excel模板失败", e);
            throw new RuntimeException("生成Excel模板失败", e);
        }
    }
}
