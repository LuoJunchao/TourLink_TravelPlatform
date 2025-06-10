package org.tourlink.routingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tourlink.routingservice.service.StationImportService;

import java.io.IOException;

@RestController
@RequestMapping("/api/import")
public class StationImportController {

    @Autowired
    private StationImportService stationImportService;

    @PostMapping("/stations")
    public ResponseEntity<String> importAllStations(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type // "AIRPORT" or "TRAIN"
    ) {
        try {
            if (!type.equalsIgnoreCase("AIRPORT") && !type.equalsIgnoreCase("TRAIN")) {
                return ResponseEntity.badRequest().body("参数 type 仅支持 AIRPORT 或 TRAIN");
            }

            stationImportService.importAllStations(file.getInputStream(), type.toUpperCase());
            return ResponseEntity.ok("导入成功！");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("导入失败: " + e.getMessage());
        }
    }
}
