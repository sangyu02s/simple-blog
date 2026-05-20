package com.simpleblog.backend.admin;

import com.simpleblog.backend.common.ApiException;
import com.simpleblog.backend.post.Post;
import com.simpleblog.backend.post.PostRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminPostService {

    private final PostRepository postRepository;

    public AdminPostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<AdminPostSummaryResponse> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
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
