package org.tourlink.socialservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tourlink.socialservice.entity.Blog;
import org.tourlink.socialservice.entity.BlogLike;

@Repository
public interface LikeRepository extends CrudRepository<BlogLike, Long> {

    BlogLike findByBlogAndUserId(Blog blog, String userId);

    boolean existsByBlogAndUserId(Blog blog, String userId);
}
