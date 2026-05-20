package com.simpleblog.backend.admin;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public List<AdminUserSummaryResponse> getUsers() {
        return adminUserService.getUsers();
    }

    @PatchMapping("/{userId}/status")
    public AdminUserSummaryResponse updateStatus(@PathVariable Long userId,
                                                 @Valid @RequestBody UpdateUserStatusRequest request) {
        return adminUserService.updateStatus(userId, request);
    }
}
