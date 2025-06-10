package org.tourlink.dataplatformservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.tourlink.dataplatformservice.entity.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(String userId);
}
