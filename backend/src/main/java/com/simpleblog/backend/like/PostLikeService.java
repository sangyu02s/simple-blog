package com.simpleblog.backend.like;

import com.simpleblog.backend.auth.AuthenticatedUser;
import com.simpleblog.backend.common.ApiException;
import com.simpleblog.backend.post.Post;
import com.simpleblog.backend.post.PostRepository;
import com.simpleblog.backend.post.PostStatus;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public PostLikeService(PostLikeRepository postLikeRepository, PostRepository postRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public LikeResponse getLikeState(Long postId, AuthenticatedUser authenticatedUser) {
        Post post = ensurePublishedPost(postId);
        if (authenticatedUser == null) {
            return new LikeResponse(false, post.getLikeCount());
        }

        boolean liked = postLikeRepository.existsByPostIdAndUserId(postId, authenticatedUser.getSourceUser().getId());
        return new LikeResponse(liked, post.getLikeCount());
    }

    @Transactional
    public LikeResponse likePost(Long postId, AuthenticatedUser authenticatedUser) {
        Post post = ensurePublishedPost(postId);
        Long userId = authenticatedUser.getSourceUser().getId();

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            return new LikeResponse(true, post.getLikeCount());
        }

        PostLike postLike = new PostLike();
        postLike.setPost(post);
        postLike.setUser(authenticatedUser.getSourceUser());
        postLike.setCreatedAt(LocalDateTime.now());
        postLikeRepository.save(postLike);

        post.setLikeCount(post.getLikeCount() + 1);
        return new LikeResponse(true, post.getLikeCount());
    }

    @Transactional
    public LikeResponse unlikePost(Long postId, AuthenticatedUser authenticatedUser) {
        Post post = ensurePublishedPost(postId);
        Long userId = authenticatedUser.getSourceUser().getId();

        postLikeRepository.findByPostIdAndUserId(postId, userId).ifPresent(postLike -> {
            postLikeRepository.delete(postLike);
            if (post.getLikeCount() > 0) {
                post.setLikeCount(post.getLikeCount() - 1);
            }
        });

        return new LikeResponse(false, post.getLikeCount());
    }

    private Post ensurePublishedPost(Long postId) {
        return postRepository.findByIdAndStatus(postId, PostStatus.PUBLISHED)
                .orElseThrow(() -> new ApiException("文章不存在或不可点赞"));
    }
}
