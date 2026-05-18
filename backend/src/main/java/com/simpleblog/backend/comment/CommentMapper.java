package com.simpleblog.backend.comment;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                new CommentAuthorResponse(
                        comment.getAuthor().getId(),
                        comment.getAuthor().getUsername(),
                        comment.getAuthor().getNickname()
                ),
                comment.getCreatedAt()
        );
    }
}
