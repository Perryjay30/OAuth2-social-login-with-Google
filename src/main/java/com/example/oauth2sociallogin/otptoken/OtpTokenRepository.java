package com.example.oauth2sociallogin.otptoken;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
public interface OtpTokenRepository extends JpaRepository<OtpToken, Integer> {

    Optional<OtpToken> findByToken(String token);

    void deleteOtpTokensByExpiredAtBefore(LocalDateTime currentTime);

    @Modifying
    @Query("UPDATE OtpToken otpToken " +
            "SET otpToken.verifiedAt= ?1 " +
            "WHERE otpToken.token = ?2")
    void setVerifiedAt(LocalDateTime verified, String token);
}
