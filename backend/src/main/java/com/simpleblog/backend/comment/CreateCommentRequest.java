package com.simpleblog.backend.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(
        @NotBlank(message = "评论内容不能为空")
        @Size(min = 2, max = 1000, message = "评论长度需在 2 到 1000 个字符之间")
        String content
) {
}
