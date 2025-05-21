package org.tourlink.attractionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tourlink.attractionservice.entity.AttractionCategory;

import java.util.Optional;

/**
 * 景点分类仓库
 */
@Repository
public interface AttractionCategoryRepository extends JpaRepository<AttractionCategory, Long> {
    
    /**
     * 根据名称查找分类
     * @param name 分类名称
     * @return 分类
     */
    Optional<AttractionCategory> findByName(String name);
}
