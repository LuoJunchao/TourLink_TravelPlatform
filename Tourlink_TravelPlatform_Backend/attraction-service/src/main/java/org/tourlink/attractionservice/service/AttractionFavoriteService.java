package org.tourlink.attractionservice.service;

import org.tourlink.attractionservice.entity.AttractionFavorite;

import java.util.List;

public interface AttractionFavoriteService {
    List<AttractionFavorite> getAllFavorites();
    AttractionFavorite getFavoriteById(Long id);
    AttractionFavorite addFavorite(AttractionFavorite favorite);
    void deleteFavoriteById(Long id);
    List<AttractionFavorite> getFavoritesByUserId(Long userId);
    boolean isAttractionFavoritedByUser(Long attractionId, Long userId);
}