package com.simpleblog.backend.admin;

public record AdminOverviewResponse(
        long totalUsers,
        long totalPosts,
        long totalComments,
        long hiddenPosts,
        long hiddenComments,
        long disabledUsers
) {
}
