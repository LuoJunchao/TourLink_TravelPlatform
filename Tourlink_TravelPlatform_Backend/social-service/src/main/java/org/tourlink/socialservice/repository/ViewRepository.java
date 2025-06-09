package org.tourlink.socialservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tourlink.socialservice.entity.Blog;
import org.tourlink.socialservice.entity.BlogView;

@Repository
public interface ViewRepository extends CrudRepository<BlogView, Long> {

    boolean existsByBlogAndUserId(Blog blog, String userId);

}
