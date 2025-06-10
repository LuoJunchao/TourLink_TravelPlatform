package org.tourlink.attractionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tourlink.attractionservice.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String name);
}
