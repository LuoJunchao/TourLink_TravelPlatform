package org.tourlink.socialservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tourlink.socialservice.entity.Blog;
import org.tourlink.socialservice.entity.BlogComment;

@Repository
public interface CommentRepository extends CrudRepository<BlogComment, Long> {

    Page<BlogComment> findByBlog(Blog blog, Pageable pageable);
}
