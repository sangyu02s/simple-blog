package com.simpleblog.backend.admin;

import com.simpleblog.backend.user.UserRole;
import com.simpleblog.backend.user.UserStatus;
import java.time.LocalDateTime;

public record AdminUserSummaryResponse(
        Long id,
        String username,
        String nickname,
        UserRole role,
        UserStatus status,
        LocalDateTime createdAt
) {
}
