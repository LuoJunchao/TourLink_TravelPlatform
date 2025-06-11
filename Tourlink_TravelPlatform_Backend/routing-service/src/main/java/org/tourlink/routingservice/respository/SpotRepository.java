package org.tourlink.routingservice.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tourlink.routingservice.entity.Spot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findByCityName(String cityName);

}