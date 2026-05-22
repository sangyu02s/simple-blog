package com.simpleblog.backend.admin;

import com.simpleblog.backend.common.ApiException;
import com.simpleblog.backend.post.Post;
import com.simpleblog.backend.post.PostRepository;
import com.simpleblog.backend.post.PostStatus;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AdminPostService {

    private final PostRepository postRepository;

    public AdminPostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<AdminPostSummaryResponse> getPosts(PostStatus status, String keyword) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        List<Post> posts;
        if (status == null && !StringUtils.hasText(normalizedKeyword)) {
            posts = postRepository.findAllByOrderByCreatedAtDesc();
        } else if (status == null) {
            posts = postRepository.findAllByTitleContainingIgnoreCaseOrderByCreatedAtDesc(normalizedKeyword);
        } else if (!StringUtils.hasText(normalizedKeyword)) {
            posts = postRepository.findAllByStatusOrderByCreatedAtDesc(status);
        } else {
            posts = postRepository.findAllByStatusAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(status, normalizedKeyword);
        }

        return posts.stream().map(this::toResponse).toList();
    }

    @Transactional
    public AdminPostSummaryResponse updateStatus(Long postId, UpdatePostStatusRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException("文章不存在"));

        post.setStatus(request.status());
        return toResponse(post);
    }

    private AdminPostSummaryResponse toResponse(Post post) {
        return new AdminPostSummaryResponse(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getNickname(),
                post.getStatus(),
                post.getCreatedAt(),
                post.getLikeCount(),
                post.getCommentCount()
        );
    }
}
