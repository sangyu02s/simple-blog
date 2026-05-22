package com.simpleblog.backend.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findAllByOrderByCreatedAtDesc();

    List<User> findAllByUsernameContainingIgnoreCaseOrNicknameContainingIgnoreCaseOrderByCreatedAtDesc(String usernameKeyword, String nicknameKeyword);

    List<User> findAllByStatusAndUsernameContainingIgnoreCaseOrStatusAndNicknameContainingIgnoreCaseOrderByCreatedAtDesc(
            UserStatus usernameStatus,
            String usernameKeyword,
            UserStatus nicknameStatus,
            String nicknameKeyword
    );

    long countByStatus(UserStatus status);
}
