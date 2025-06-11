package org.tourlink.routingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tourlink.routingservice.service.SpotImportService;

import java.io.IOException;

@RestController
@RequestMapping("/api/import")
public class SpotImportController {

    @Autowired
    private SpotImportService spotImportService;

    @PostMapping("/spots")
    public ResponseEntity<String> importSpots(@RequestParam("file") MultipartFile file) {
        try {
            // 修改点：先清空数据库，再导入新数据
            spotImportService.clearAllSpots(); // 新增清空方法
            spotImportService.importSpotsFromCsv(file.getInputStream());
            return ResponseEntity.ok("景点数据已清空并成功导入新景点！");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("导入失败: " + e.getMessage());
        }
    }
}