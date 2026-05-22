package com.simpleblog.backend.admin;

import com.simpleblog.backend.comment.Comment;
import com.simpleblog.backend.comment.CommentRepository;
import com.simpleblog.backend.comment.CommentStatus;
import com.simpleblog.backend.common.ApiException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AdminCommentService {

    private final CommentRepository commentRepository;

    public AdminCommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public List<AdminCommentSummaryResponse> getComments(CommentStatus status, String keyword) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        List<Comment> comments;
        if (status == null && !StringUtils.hasText(normalizedKeyword)) {
            comments = commentRepository.findAllByOrderByCreatedAtDesc();
        } else if (status == null) {
            comments = commentRepository.findAllByContentContainingIgnoreCaseOrderByCreatedAtDesc(normalizedKeyword);
        } else if (!StringUtils.hasText(normalizedKeyword)) {
            comments = commentRepository.findAllByOrderByCreatedAtDesc().stream()
                    .filter(comment -> comment.getStatus() == status)
                    .toList();
        } else {
            comments = commentRepository.findAllByStatusAndContentContainingIgnoreCaseOrderByCreatedAtDesc(status, normalizedKeyword);
        }

        return comments.stream().map(this::toResponse).toList();
    }

    @Transactional
    public AdminCommentSummaryResponse updateStatus(Long commentId, UpdateCommentStatusRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException("评论不存在"));

        comment.setStatus(request.status());
        return toResponse(comment);
    }

    private AdminCommentSummaryResponse toResponse(Comment comment) {
        return new AdminCommentSummaryResponse(
                comment.getId(),
                comment.getPost().getTitle(),
                comment.getAuthor().getNickname(),
                comment.getContent(),
                comment.getStatus(),
                comment.getCreatedAt()
        );
    }
}
