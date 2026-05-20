package com.simpleblog.backend.admin;

import com.simpleblog.backend.comment.Comment;
import com.simpleblog.backend.comment.CommentRepository;
import com.simpleblog.backend.common.ApiException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminCommentService {

    private final CommentRepository commentRepository;

    public AdminCommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public List<AdminCommentSummaryResponse> getComments() {
        return commentRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
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
