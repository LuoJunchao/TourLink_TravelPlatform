package org.tourlink.attractionservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tourlink.attractionservice.dto.ImportResultDTO;
import org.tourlink.attractionservice.service.ExcelImportService;
import org.tourlink.attractionservice.service.ExcelTemplateService;

import java.io.ByteArrayOutputStream;

/**
 * Excel导入控制器
 */
@RestController
@RequestMapping("/api/attractions/import")
@RequiredArgsConstructor
@Slf4j
public class ExcelImportController {

    private final ExcelImportService excelImportService;
    private final ExcelTemplateService excelTemplateService;

    /**
     * 导入景点数据
     * @param file Excel文件
     * @return 导入结果
     */
    @PostMapping("/excel")
    public ResponseEntity<ImportResultDTO> importAttractionsFromExcel(@RequestParam("file") MultipartFile file) {
        log.info("开始导入景点数据，文件名: {}", file.getOriginalFilename());
        ImportResultDTO result = excelImportService.importAttractionsFromExcel(file);
        log.info("景点数据导入完成，总数: {}, 成功: {}, 失败: {}", result.getTotalCount(), result.getSuccessCount(), result.getFailCount());
        return ResponseEntity.ok(result);
    }

    /**
     * 下载景点导入模板
     * @return Excel模板文件
     */
    @GetMapping("/template")
    public ResponseEntity<Resource> downloadTemplate() {
        log.info("下载景点导入模板");
        ByteArrayOutputStream outputStream = excelTemplateService.generateAttractionImportTemplate();
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attraction_import_template.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
