package com.simpleblog.backend.post;

import com.simpleblog.backend.tag.Tag;
import java.util.Comparator;
import java.util.List;

public final class PostMapper {

    private PostMapper() {
    }

    public static PostSummaryResponse toSummaryResponse(Post post) {
        return new PostSummaryResponse(
                post.getId(),
                post.getTitle(),
                post.getSummary(),
                toAuthorResponse(post),
                toTagNames(post),
                post.getCreatedAt(),
                post.getLikeCount(),
                post.getCommentCount()
        );
    }

    public static PostDetailResponse toDetailResponse(Post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getSummary(),
                post.getContent(),
                toAuthorResponse(post),
                toTagNames(post),
                post.getCreatedAt(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getViewCount()
        );
    }

    private static PostAuthorResponse toAuthorResponse(Post post) {
        return new PostAuthorResponse(
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getNickname()
        );
    }

    private static List<String> toTagNames(Post post) {
        return post.getTags().stream()
                .map(Tag::getName)
                .sorted(Comparator.naturalOrder())
                .toList();
    }
}
