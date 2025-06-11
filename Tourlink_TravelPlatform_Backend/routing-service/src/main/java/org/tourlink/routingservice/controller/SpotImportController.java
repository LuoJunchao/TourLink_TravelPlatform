package org.tourlink.routingservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tourlink.routingservice.entity.Spot;
import org.tourlink.routingservice.respository.SpotRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/spot/import")
@RequiredArgsConstructor
public class SpotImportController {

    private final SpotRepository spotRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/csv")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        log.info("收到文件：{}", file.getOriginalFilename());
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("上传的文件为空！");
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<Spot> spotList = new ArrayList<>();
            String[] row;
            while ((row = reader.readNext()) != null) {
                Spot spot = convertRowToSpot(row);
                spotList.add(spot);
            }
            spotRepository.saveAll(spotList);
            return ResponseEntity.ok("成功导入 " + spotList.size() + " 条数据！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("导入失败: " + e.getMessage());
        }
    }
    private String extractCityName(String locationField) {
        if (locationField == null || locationField.isEmpty()) return "未知";
        String[] parts = locationField.split("·");
        return parts.length >= 2 ? parts[1].trim() : "未知";
    }

    private Spot convertRowToSpot(String[] row) {
        Spot spot = new Spot();
        try {
            spot.setName(row[10]);
            spot.setPrice(row[11].isEmpty() ? 0 : Double.parseDouble(row[11]));
            spot.setRating(row[2].isEmpty() ? 0 : Double.parseDouble(row[2]));
            spot.setSales(row[17].isEmpty() ? 0 : Integer.parseInt(row[17]));

            String[] coords = row[3].replace("\"", "").split(",");
            spot.setLongitude(Double.parseDouble(coords[0]));
            spot.setLatitude(Double.parseDouble(coords[1]));

            // ✅ 提取 cityName 字段，来自类似 "湖北·宜昌·三峡大坝旅游区"
            String cityRaw = row[15].trim(); // row[15] 是“湖北·宜昌·三峡大坝旅游区”
            spot.setCityName(extractCityName(cityRaw));

            List<String> timeSlots = row[14] == null ? new ArrayList<>() : Arrays.asList(row[14].split(","));
            spot.setTimeSlots(timeSlots);

            List<String> tags = row[19] == null || row[19].isEmpty() ? new ArrayList<>() :
                    objectMapper.readValue(row[19].replaceAll("“|”", "\""), new TypeReference<List<String>>() {});
            spot.setTags(tags);

        } catch (Exception e) {
            log.error("解析失败，跳过此行：" + Arrays.toString(row), e);
        }
        return spot;
    }

}