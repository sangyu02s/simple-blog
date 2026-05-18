package com.simpleblog.backend.comment;

import com.simpleblog.backend.auth.AuthenticatedUser;
import com.simpleblog.backend.common.ApiException;
import com.simpleblog.backend.post.Post;
import com.simpleblog.backend.post.PostRepository;
import com.simpleblog.backend.post.PostStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getVisibleComments(Long postId) {
        ensurePublishedPost(postId);
        return commentRepository.findAllByPostIdAndStatusOrderByCreatedAtAsc(postId, CommentStatus.VISIBLE).stream()
                .map(CommentMapper::toResponse)
                .toList();
    }

    @Transactional
    public CommentResponse createComment(Long postId,
                                         CreateCommentRequest request,
                                         AuthenticatedUser authenticatedUser) {
        Post post = ensurePublishedPost(postId);

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(authenticatedUser.getSourceUser());
        comment.setContent(request.content().trim());
        comment.setStatus(CommentStatus.VISIBLE);
        comment.setCreatedAt(LocalDateTime.now());

        // 评论创建成功后同步维护文章评论数，保证详情页与列表页的统计字段一致。
        post.setCommentCount(post.getCommentCount() + 1);

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toResponse(savedComment);
    }

    private Post ensurePublishedPost(Long postId) {
        return postRepository.findByIdAndStatus(postId, PostStatus.PUBLISHED)
                .orElseThrow(() -> new ApiException("文章不存在或不可评论"));
    }
}
