package com.simpleblog.backend.admin;

import com.simpleblog.backend.user.UserStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull(message = "状态不能为空")
        UserStatus status
) {
}
