package org.tourlink.socialservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tourlink.socialservice.entity.Blog;

import java.util.List;

@Repository
public interface BlogRepository extends CrudRepository<Blog, Long> {

    @Modifying
    @Transactional  // 确保修改类操作能提交
    @Query("UPDATE Blog b SET b.likeCount = b.likeCount + 1 WHERE b.blogId = :blogId")
    void incrementLikeCount(@Param("blogId") Long blogId);

    @Modifying
    @Transactional
    @Query("UPDATE Blog b SET b.likeCount = b.likeCount - 1 WHERE b.blogId = :blogId")
    void decrementLikeCount(@Param("blogId") Long blogId);

    @Modifying
    @Transactional
    @Query("UPDATE Blog b SET b.commentCount = b.commentCount + 1 WHERE b.blogId = :blogId")
    void incrementCommentCount(@Param("blogId") Long blogId);

    @Modifying
    @Transactional
    @Query("UPDATE Blog b SET b.viewCount = b.viewCount + 1 WHERE b.blogId = :blogId")
    void incrementViewCount(@Param("blogId") Long blogId);

    List<Blog> findByUserIdOrderByPublishTime(String userId);

    Page<Blog> findAll(Specification<Blog> spec, Pageable pageable);
}
