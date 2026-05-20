package com.simpleblog.backend.me;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateMyPostRequest(
        @NotBlank(message = "标题不能为空")
        @Size(min = 3, max = 200, message = "标题长度需在 3 到 200 个字符之间")
        String title,

        @NotBlank(message = "摘要不能为空")
        @Size(min = 10, max = 500, message = "摘要长度需在 10 到 500 个字符之间")
        String summary,

        @NotBlank(message = "正文不能为空")
        @Size(min = 20, max = 20000, message = "正文字数需在 20 到 20000 个字符之间")
        String content,

        @NotEmpty(message = "至少需要一个标签")
        @Size(max = 5, message = "标签数量不能超过 5 个")
        List<@Size(min = 1, max = 20, message = "单个标签长度需在 1 到 20 个字符之间") String> tags
) {
}
