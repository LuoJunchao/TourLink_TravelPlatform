package org.tourlink.attractionservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "routing-service", path = "/data-platform")
public interface RoutingClient {
}
