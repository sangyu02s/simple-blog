package com.simpleblog.backend.auth;

import com.simpleblog.backend.user.User;

public final class AuthMapper {

    private AuthMapper() {
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getRole(),
                user.getStatus()
        );
    }
}
