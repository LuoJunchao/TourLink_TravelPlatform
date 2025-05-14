package org.tourlink.socialservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.tourlink.socialservice.entity.Tag;

import java.util.Optional;

public interface TagRepository extends CrudRepository<Tag, Integer> {

    Optional<Tag> findByName(String tagName);
}
