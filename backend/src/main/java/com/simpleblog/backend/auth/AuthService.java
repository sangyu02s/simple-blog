package com.simpleblog.backend.auth;

import com.simpleblog.backend.common.ApiException;
import com.simpleblog.backend.user.User;
import com.simpleblog.backend.user.UserRepository;
import com.simpleblog.backend.user.UserRole;
import com.simpleblog.backend.user.UserStatus;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ApiException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.username().trim());
        user.setNickname(request.nickname().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return buildAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username().trim())
                .orElseThrow(() -> new ApiException("用户名或密码错误"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ApiException("当前账号不可登录");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ApiException("用户名或密码错误");
        }

        return buildAuthResponse(user);
    }

    public UserResponse currentUser(AuthenticatedUser authenticatedUser) {
        return AuthMapper.toUserResponse(authenticatedUser.getSourceUser());
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtTokenProvider.generateToken(user.getUsername());
        return new AuthResponse(token, AuthMapper.toUserResponse(user));
    }
}
