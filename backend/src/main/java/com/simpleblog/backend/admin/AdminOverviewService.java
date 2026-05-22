package com.simpleblog.backend.admin;

import com.simpleblog.backend.comment.CommentRepository;
import com.simpleblog.backend.comment.CommentStatus;
import com.simpleblog.backend.post.PostRepository;
import com.simpleblog.backend.post.PostStatus;
import com.simpleblog.backend.user.UserRepository;
import com.simpleblog.backend.user.UserStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminOverviewService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public AdminOverviewService(UserRepository userRepository,
                                PostRepository postRepository,
                                CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public AdminOverviewResponse getOverview() {
        return new AdminOverviewResponse(
                userRepository.count(),
                postRepository.count(),
                commentRepository.count(),
                postRepository.countByStatus(PostStatus.HIDDEN),
                commentRepository.countByStatus(CommentStatus.HIDDEN),
                userRepository.countByStatus(UserStatus.DISABLED)
        );
    }
}
