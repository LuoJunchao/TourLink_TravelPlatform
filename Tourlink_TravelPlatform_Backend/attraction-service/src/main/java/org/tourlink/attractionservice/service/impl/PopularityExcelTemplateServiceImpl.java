package org.tourlink.attractionservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.tourlink.attractionservice.service.PopularityExcelTemplateService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 景点热度Excel模板服务实现
 */
@Service
@Slf4j
public class PopularityExcelTemplateServiceImpl implements PopularityExcelTemplateService {

    @Override
    public ByteArrayOutputStream generatePopularityImportTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("景点热度导入模板");

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
                    "景点ID", "浏览量", "点赞量", "分享量"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256); // 设置列宽
            }

            // 添加示例数据
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue(1); // 景点ID
            exampleRow.createCell(1).setCellValue(1000); // 浏览量
            exampleRow.createCell(2).setCellValue(50); // 点赞量
            exampleRow.createCell(3).setCellValue(20); // 分享量

            // 添加第二个示例行
            Row exampleRow2 = sheet.createRow(2);
            exampleRow2.createCell(0).setCellValue(2); // 景点ID
            exampleRow2.createCell(1).setCellValue(2500); // 浏览量
            exampleRow2.createCell(2).setCellValue(120); // 点赞量
            exampleRow2.createCell(3).setCellValue(45); // 分享量

            // 写入到输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream;

        } catch (IOException e) {
            log.error("生成景点热度Excel模板失败", e);
            throw new RuntimeException("生成景点热度Excel模板失败", e);
        }
    }
}
