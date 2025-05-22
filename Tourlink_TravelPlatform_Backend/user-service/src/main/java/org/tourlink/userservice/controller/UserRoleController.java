package org.tourlink.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tourlink.userservice.entity.UserRole;
import org.tourlink.userservice.service.UserRoleService;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {
    
    @Autowired
    private UserRoleService userRoleService;

    @PostMapping
    public ResponseEntity<UserRole> createUserRole(@RequestBody UserRole userRole) {
        return ResponseEntity.ok(userRoleService.createUserRole(userRole));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRole> getUserRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(userRoleService.getUserRoleById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRole>> getUserRolesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userRoleService.getUserRolesByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRole> updateUserRole(@PathVariable Long id, @RequestBody UserRole userRole) {
        return ResponseEntity.ok(userRoleService.updateUserRole(id, userRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id) {
        userRoleService.deleteUserRole(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{userId}/role/{roleType}")
    public ResponseEntity<UserRole> assignRoleToUser(
            @PathVariable Long userId,
            @PathVariable UserRole.RoleType roleType) {
        return ResponseEntity.ok(userRoleService.assignRoleToUser(userId, roleType));
    }

    @DeleteMapping("/user/{userId}/role/{roleType}")
    public ResponseEntity<Void> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable UserRole.RoleType roleType) {
        userRoleService.removeRoleFromUser(userId, roleType);
        return ResponseEntity.ok().build();
    }
} 