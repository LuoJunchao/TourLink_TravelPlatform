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
import org.tourlink.attractionservice.service.PopularityExcelImportService;
import org.tourlink.attractionservice.service.PopularityExcelTemplateService;

import java.io.ByteArrayOutputStream;

/**
 * 景点热度导入控制器
 */
@RestController
@RequestMapping("/api/popularity/import")
@RequiredArgsConstructor
@Slf4j
public class PopularityImportController {

    private final PopularityExcelImportService popularityExcelImportService;
    private final PopularityExcelTemplateService popularityExcelTemplateService;

    /**
     * 导入景点热度数据
     * @param file Excel文件
     * @return 导入结果
     */
    @PostMapping("/excel")
    public ResponseEntity<ImportResultDTO> importPopularityFromExcel(@RequestParam("file") MultipartFile file) {
        log.info("开始导入景点热度数据，文件名: {}", file.getOriginalFilename());
        ImportResultDTO result = popularityExcelImportService.importPopularityFromExcel(file);
        log.info("景点热度数据导入完成，总数: {}, 成功: {}, 失败: {}", 
                result.getTotalCount(), result.getSuccessCount(), result.getFailCount());
        return ResponseEntity.ok(result);
    }

    /**
     * 下载景点热度导入模板
     * @return Excel模板文件
     */
    @GetMapping("/template")
    public ResponseEntity<Resource> downloadTemplate() {
        log.info("下载景点热度导入模板");
        ByteArrayOutputStream outputStream = popularityExcelTemplateService.generatePopularityImportTemplate();
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=popularity_import_template.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
