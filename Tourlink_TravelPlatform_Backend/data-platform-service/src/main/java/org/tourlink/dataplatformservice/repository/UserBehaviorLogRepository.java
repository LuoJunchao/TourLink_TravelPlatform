package org.tourlink.dataplatformservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.tourlink.dataplatformservice.entity.UserBehaviorLog;

public interface UserBehaviorLogRepository extends CrudRepository<UserBehaviorLog, Long> {
}
