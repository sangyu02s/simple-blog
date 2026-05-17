package com.simpleblog.backend.post;

public record PostAuthorResponse(
        Long id,
        String username,
        String nickname
) {
}
