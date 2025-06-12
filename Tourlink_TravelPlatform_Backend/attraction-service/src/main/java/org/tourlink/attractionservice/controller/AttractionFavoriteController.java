package org.tourlink.attractionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.attractionservice.dto.AttractionFavoriteResponse;
import java.util.List;
import java.util.stream.Collectors;

import org.tourlink.attractionservice.entity.AttractionFavorite;
import org.tourlink.attractionservice.service.AttractionFavoriteService;
import org.tourlink.attractionservice.service.AttractionService;
import org.tourlink.attractionservice.service.event.BehaviorEventSender;
import org.tourlink.common.dto.dataPlatformDTO.UserBehaviorMessage;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attraction-favorites")
@RequiredArgsConstructor
public class AttractionFavoriteController {

    private final AttractionFavoriteService favoriteService;

    private final AttractionService attractionService;

    private final BehaviorEventSender behaviorEventSender;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AttractionFavoriteResponse>> getUserFavorites(@PathVariable Long userId) {
        List<AttractionFavorite> favorites = favoriteService.getFavoritesByUserId(userId);
        List<AttractionFavoriteResponse> response = favorites.stream()
            .map(favorite -> {
                AttractionFavoriteResponse dto = new AttractionFavoriteResponse();
                dto.setFavoriteId(favorite.getFavoriteId());
                dto.setAttractionId(favorite.getAttraction().getId());
                dto.setUserId(favorite.getUserId());
                dto.setCreatedTime(favorite.getCreatedTime());
                return dto;
            })
            .toList();
        return ResponseEntity.ok(response);
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

        // 发送行为消息
        UserBehaviorMessage message = new UserBehaviorMessage(
                String.valueOf(favorite.getUserId()),
                "ATTRACTION",
                favorite.getAttraction().getId(),
                "COLLECT",
                LocalDateTime.now()
        );
        behaviorEventSender.send(message);

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