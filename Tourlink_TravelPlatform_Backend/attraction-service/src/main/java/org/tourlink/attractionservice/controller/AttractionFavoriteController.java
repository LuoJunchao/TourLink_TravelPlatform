package org.tourlink.attractionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.tourlink.attractionservice.entity.AttractionFavorite;
import org.tourlink.attractionservice.service.AttractionFavoriteService;
import org.tourlink.attractionservice.service.AttractionService;

import java.util.List;

@RestController
@RequestMapping("/api/attraction-favorites")
public class AttractionFavoriteController {

    @Autowired
    private AttractionFavoriteService favoriteService;

    @Autowired
    private AttractionService attractionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AttractionFavorite>> getUserFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUserId(userId));
    }

    @GetMapping("/attraction/{attractionId}/user/{userId}")
    public ResponseEntity<Boolean> isFavorite(@PathVariable Long attractionId, @PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.isAttractionFavoritedByUser(attractionId, userId));
    }

    @PostMapping
    public ResponseEntity<AttractionFavorite> addFavorite(@RequestBody AttractionFavorite favorite) {
        // 检查是否已经收藏
        if (favoriteService.isAttractionFavoritedByUser(favorite.getAttraction().getId(), favorite.getUserId())) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(favoriteService.addFavorite(favorite));
    }

    @PostMapping("/attraction/{attractionId}/user/{userId}")
    public ResponseEntity<AttractionFavorite> addFavoriteByIds(@PathVariable Long attractionId, @PathVariable Long userId) {
        // 检查是否已经收藏
        if (favoriteService.isAttractionFavoritedByUser(attractionId, userId)) {
            return ResponseEntity.badRequest().body(null);
        }

        // 创建新的收藏
        AttractionFavorite favorite = new AttractionFavorite();
        favorite.setAttraction(attractionService.getAttractionEntityById(attractionId));
        favorite.setUserId(userId);

        return ResponseEntity.ok(favoriteService.addFavorite(favorite));
    }

    @DeleteMapping("/attraction/{attractionId}/user/{userId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long attractionId, @PathVariable Long userId) {
        // 查找收藏记录
        List<AttractionFavorite> favorites = favoriteService.getFavoritesByUserId(userId);
        AttractionFavorite favoriteToDelete = favorites.stream()
                .filter(f -> f.getAttraction().getId().equals(attractionId))
                .findFirst()
                .orElse(null);

        if (favoriteToDelete == null) {
            return ResponseEntity.notFound().build();
        }

        favoriteService.deleteFavoriteById(favoriteToDelete.getId());
        return ResponseEntity.ok().build();
    }
}