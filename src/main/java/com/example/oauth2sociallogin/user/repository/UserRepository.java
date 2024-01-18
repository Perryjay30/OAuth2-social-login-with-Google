package com.example.oauth2sociallogin.user.repository;

import com.example.oauth2sociallogin.user.data.model.Status;
import com.example.oauth2sociallogin.user.data.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAddress(String email);

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.emailAddress = :emailAddress")
    void enableUser(@Param("status") Status status, @Param("emailAddress") String emailAddress);
}
