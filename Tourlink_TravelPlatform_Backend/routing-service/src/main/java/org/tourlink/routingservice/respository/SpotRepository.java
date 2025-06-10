package org.tourlink.routingservice.respository;

import org.springframework.stereotype.Repository;
import org.tourlink.routingservice.entity.Spot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SpotRepository {
    private static final Map<Long, Spot> spotStore = new ConcurrentHashMap<>();

    public void saveAll(List<Spot> spots) {
        for (Spot spot : spots) {
            spotStore.put(spot.getId(), spot);
        }
    }

    public static List<Spot> findAll() {
        return new ArrayList<>(spotStore.values());
    }
}
