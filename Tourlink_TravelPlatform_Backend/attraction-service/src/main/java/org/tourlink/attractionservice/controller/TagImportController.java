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
import org.tourlink.attractionservice.service.TagExcelImportService;
import org.tourlink.attractionservice.service.TagExcelTemplateService;

import java.io.ByteArrayOutputStream;

/**
 * 标签导入控制器
 */
@RestController
@RequestMapping("/api/tags/import")
@RequiredArgsConstructor
@Slf4j
public class TagImportController {

    private final TagExcelImportService tagExcelImportService;
    private final TagExcelTemplateService tagExcelTemplateService;

    /**
     * 导入标签数据
     * @param file Excel文件
     * @return 导入结果
     */
    @PostMapping("/excel")
    public ResponseEntity<ImportResultDTO> importTagsFromExcel(@RequestParam("file") MultipartFile file) {
        log.info("开始导入标签数据，文件名: {}", file.getOriginalFilename());
        ImportResultDTO result = tagExcelImportService.importTagsFromExcel(file);
        log.info("标签数据导入完成，总数: {}, 成功: {}, 失败: {}", result.getTotalCount(), result.getSuccessCount(), result.getFailCount());
        return ResponseEntity.ok(result);
    }

    /**
     * 下载标签导入模板
     * @return Excel模板文件
     */
    @GetMapping("/template")
    public ResponseEntity<Resource> downloadTemplate() {
        log.info("下载标签导入模板");
        ByteArrayOutputStream outputStream = tagExcelTemplateService.generateTagImportTemplate();
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tag_import_template.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
