package com.simpleblog.backend.me;

import com.simpleblog.backend.user.UserRole;
import com.simpleblog.backend.user.UserStatus;

public record MyProfileResponse(
        Long id,
        String username,
        String nickname,
        UserRole role,
        UserStatus status
) {
}
