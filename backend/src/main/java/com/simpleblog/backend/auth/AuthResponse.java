package com.simpleblog.backend.auth;

public record AuthResponse(
        String token,
        UserResponse user
) {
}
