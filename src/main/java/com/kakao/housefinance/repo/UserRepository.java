package com.kakao.housefinance.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kakao.housefinance.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUserid(String userid);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUserid(String userid);
}