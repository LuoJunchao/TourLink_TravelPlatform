package org.tourlink.routingservice.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tourlink.routingservice.respository.TransportStationRepository;
import org.tourlink.routingservice.entity.TransportStation;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Service
public class StationImportService {

    @Autowired
    private TransportStationRepository repository;

    public void importAllStations(InputStream csvStream, String type) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        try (Reader in = new InputStreamReader(csvStream, StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = format.parse(in);

            for (CSVRecord record : records) {
                String city = record.get("市").trim();
                String name = record.get("中文名称").trim();
                String latStr = record.get("经度").trim();
                String lngStr = record.get("纬度").trim();

                if (city.isEmpty() || name.isEmpty() || latStr.isEmpty() || lngStr.isEmpty()) {
                    continue;
                }

                TransportStation station = new TransportStation();
                station.setCityName(city);
                station.setName(name);
                station.setType(type); // 类型由接口参数提供
                station.setLatitude(Double.parseDouble(latStr));
                station.setLongitude(Double.parseDouble(lngStr));

                repository.save(station);
            }
        }
    }


}
