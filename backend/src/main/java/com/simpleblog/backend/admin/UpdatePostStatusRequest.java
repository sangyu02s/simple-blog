package com.simpleblog.backend.admin;

import com.simpleblog.backend.post.PostStatus;
import jakarta.validation.constraints.NotNull;

public record UpdatePostStatusRequest(
        @NotNull(message = "状态不能为空")
        PostStatus status
) {
}
