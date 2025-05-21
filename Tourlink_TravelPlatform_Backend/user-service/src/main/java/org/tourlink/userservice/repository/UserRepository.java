package org.tourlink.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tourlink.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
} 