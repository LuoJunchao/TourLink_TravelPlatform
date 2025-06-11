package org.tourlink.routingservice.service;

import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tourlink.routingservice.entity.Spot;
import org.tourlink.routingservice.respository.SpotRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SpotImportService {

    @Autowired
    private SpotRepository spotRepository;

    @Transactional // 添加事务管理
    public void clearAllSpots() {
        spotRepository.deleteAll(); // 清空数据库表
    }
    public void importSpotsFromCsv(InputStream csvStream) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        try (Reader reader = new InputStreamReader(csvStream, StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = format.parse(reader);
            List<Spot> spots = new ArrayList<>();

            for (CSVRecord record : records) {
                Spot spot = new Spot();
                spot.setId(Long.parseLong(record.get("id").trim()));
                spot.setName(record.get("name").trim());
                spot.setPrice(Double.parseDouble(record.get("price").trim()));

                String ratingStr = record.get("rating").trim();
                spot.setRating(ratingStr.isEmpty() ? 0.0 : Double.parseDouble(ratingStr));

                spot.setSales(Integer.parseInt(record.get("sales").trim()));
                spot.setLatitude(Double.parseDouble(record.get("latitude").trim()));
                spot.setLongitude(Double.parseDouble(record.get("longitude").trim()));

                // timeSlots 逗号分隔如 "100,010"
                String timeSlotRaw = record.get("timeSlots").trim();
                spot.setTimeSlots(Arrays.asList(timeSlotRaw.split(",")));

                // tags 逗号分隔如 "历史,文化"
                String tagsRaw = record.get("tags").trim();
                spot.setTags(Arrays.asList(tagsRaw.split(",")));

                spots.add(spot);
            }

            spotRepository.saveAll(spots);
        }
    }
}
