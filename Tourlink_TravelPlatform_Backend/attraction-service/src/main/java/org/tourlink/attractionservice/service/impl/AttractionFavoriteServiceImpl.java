package org.tourlink.attractionservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tourlink.attractionservice.entity.AttractionFavorite;
import org.tourlink.attractionservice.repository.AttractionFavoriteRepository;
import org.tourlink.attractionservice.service.AttractionFavoriteService;

import java.util.List;


@Service
@Transactional
public class AttractionFavoriteServiceImpl implements AttractionFavoriteService {

    @Autowired
    private AttractionFavoriteRepository favoriteRepository;

    @Override
    public List<AttractionFavorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    @Override
    public AttractionFavorite getFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Favorite not found with id: " + id));
    }

    @Override
    public AttractionFavorite addFavorite(AttractionFavorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Override
    public void deleteFavoriteById(Long id) {
        favoriteRepository.deleteById(id);
    }

    @Override
    public List<AttractionFavorite> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @Override
    public boolean isAttractionFavoritedByUser(Long attractionId, Long userId) {
        List<AttractionFavorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream().anyMatch(f -> f.getAttraction().getId().equals(attractionId));
    }
}