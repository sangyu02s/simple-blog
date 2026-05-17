package com.simpleblog.backend.auth;

import com.simpleblog.backend.user.UserRole;
import com.simpleblog.backend.user.UserStatus;

public record UserResponse(
        Long id,
        String username,
        String nickname,
        UserRole role,
        UserStatus status
) {
}
