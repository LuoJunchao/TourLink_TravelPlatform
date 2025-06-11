package org.tourlink.routingservice.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tourlink.routingservice.entity.TransportStation;

import java.util.List;

@Repository
public interface TransportStationRepository extends JpaRepository<TransportStation, Long> {
    List<TransportStation> findByCityName(String cityName);
}