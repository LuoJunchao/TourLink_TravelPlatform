package org.tourlink.attractionservice.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.tourlink.attractionservice.service.ExcelTemplateService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Excel模板服务实现
 */
@Service
@Slf4j
public class ExcelTemplateServiceImpl implements ExcelTemplateService {

    /**
     * 生成景点导入模板
     * @return Excel文件字节数组
     */
    @Override
    public ByteArrayOutputStream generateAttractionImportTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("景点导入模板");

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
                    "省份", "名称", "星级", "评分", "价格", "销量", "省/市/区",
                    "坐标", "简介", "是否免费", "具体地址", "标签", "推荐游玩时间"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256); // 设置列宽
            }

            // 创建数值格式（一位小数）
            CellStyle decimalStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            decimalStyle.setDataFormat(format.getFormat("0.0"));

            // 设置评分列的数据验证（0-5）
            DataValidationHelper validationHelper = sheet.getDataValidationHelper();
            DataValidationConstraint ratingConstraint = validationHelper.createDecimalConstraint(
                    DataValidationConstraint.OperatorType.BETWEEN, "0.0", "5.0");
            CellRangeAddressList ratingAddressList = new CellRangeAddressList(1, 1000, 3, 3);
            DataValidation ratingValidation = validationHelper.createValidation(ratingConstraint, ratingAddressList);
            ratingValidation.setShowErrorBox(true);
            ratingValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            ratingValidation.createErrorBox("无效的评分", "评分必须在0.0到5.0之间，且保留一位小数");
            sheet.addValidationData(ratingValidation);

            // 添加示例数据
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue("北京"); // 省份
            exampleRow.createCell(1).setCellValue("故宫博物院"); // 名称
            exampleRow.createCell(2).setCellValue("5A"); // 星级

            // 设置评分，使用一位小数格式
            Cell ratingCell = exampleRow.createCell(3);
            ratingCell.setCellValue(4.8); // 评分
            ratingCell.setCellStyle(decimalStyle);

            exampleRow.createCell(4).setCellValue(60); // 价格
            exampleRow.createCell(5).setCellValue(10000); // 销量
            exampleRow.createCell(6).setCellValue("北京市/东城区"); // 省/市/区
            exampleRow.createCell(7).setCellValue("39.9163,116.3972"); // 坐标
            exampleRow.createCell(8).setCellValue("故宫博物院，旧称为紫禁城，是中国明清两代的皇家宫殿..."); // 简介
            exampleRow.createCell(9).setCellValue("FALSE"); // 是否免费，使用布尔值
            exampleRow.createCell(10).setCellValue("北京市东城区景山前街4号"); // 具体地址
            exampleRow.createCell(11).setCellValue("历史,文化,博物馆"); // 标签
            exampleRow.createCell(12).setCellValue("3-4小时"); // 推荐游玩时间，使用字符串

            // 添加第二个示例行，展示未评级和TRUE的情况
            Row exampleRow2 = sheet.createRow(2);
            exampleRow2.createCell(0).setCellValue("上海");
            exampleRow2.createCell(1).setCellValue("外滩");
            // 星级留空，表示未评级

            Cell ratingCell2 = exampleRow2.createCell(3);
            ratingCell2.setCellValue(4.5);
            ratingCell2.setCellStyle(decimalStyle);

            exampleRow2.createCell(4).setCellValue(0); // 免费景点，价格为0
            exampleRow2.createCell(5).setCellValue(50000);
            exampleRow2.createCell(6).setCellValue("上海市/黄浦区");
            exampleRow2.createCell(7).setCellValue("31.2304,121.4737");
            exampleRow2.createCell(8).setCellValue("外滩是上海市中心的一个区域，位于黄浦区的黄浦江畔...");
            exampleRow2.createCell(9).setCellValue("TRUE"); // 是否免费，使用布尔值
            exampleRow2.createCell(10).setCellValue("上海市黄浦区中山东一路");
            exampleRow2.createCell(11).setCellValue("景点,夜景,建筑");
            exampleRow2.createCell(12).setCellValue("1-2小时");

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
