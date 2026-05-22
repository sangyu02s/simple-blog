package com.simpleblog.backend.admin;

import com.simpleblog.backend.common.ApiException;
import com.simpleblog.backend.user.User;
import com.simpleblog.backend.user.UserRepository;
import com.simpleblog.backend.user.UserStatus;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<AdminUserSummaryResponse> getUsers(UserStatus status, String keyword) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        List<User> users;
        if (status == null && !StringUtils.hasText(normalizedKeyword)) {
            users = userRepository.findAllByOrderByCreatedAtDesc();
        } else if (status == null) {
            users = userRepository.findAllByUsernameContainingIgnoreCaseOrNicknameContainingIgnoreCaseOrderByCreatedAtDesc(
                    normalizedKeyword,
                    normalizedKeyword
            );
        } else if (!StringUtils.hasText(normalizedKeyword)) {
            users = userRepository.findAllByOrderByCreatedAtDesc().stream()
                    .filter(user -> user.getStatus() == status)
                    .toList();
        } else {
            users = userRepository.findAllByStatusAndUsernameContainingIgnoreCaseOrStatusAndNicknameContainingIgnoreCaseOrderByCreatedAtDesc(
                    status,
                    normalizedKeyword,
                    status,
                    normalizedKeyword
            );
        }

        return users.stream().map(this::toResponse).toList();
    }

    @Transactional
    public AdminUserSummaryResponse updateStatus(Long userId, UpdateUserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("用户不存在"));

        user.setStatus(request.status());
        return toResponse(user);
    }

    private AdminUserSummaryResponse toResponse(User user) {
        return new AdminUserSummaryResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
