package com.simpleblog.backend.me;

import com.simpleblog.backend.auth.AuthenticatedUser;
import com.simpleblog.backend.common.ApiException;
import com.simpleblog.backend.post.Post;
import com.simpleblog.backend.post.PostRepository;
import com.simpleblog.backend.post.PostStatus;
import com.simpleblog.backend.tag.Tag;
import com.simpleblog.backend.tag.TagRepository;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class MyProfileService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public MyProfileService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public MyProfileResponse getProfile(AuthenticatedUser authenticatedUser) {
        var user = authenticatedUser.getSourceUser();
        return new MyProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getRole(),
                user.getStatus()
        );
    }

    @Transactional(readOnly = true)
    public List<MyPostSummaryResponse> getMyPosts(AuthenticatedUser authenticatedUser) {
        return postRepository.findAllByAuthorIdOrderByCreatedAtDesc(authenticatedUser.getSourceUser().getId()).stream()
                .map(this::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UpdateMyPostRequest getMyPostForEdit(Long postId, AuthenticatedUser authenticatedUser) {
        Post post = getOwnedPost(postId, authenticatedUser);
        return new UpdateMyPostRequest(
                post.getTitle(),
                post.getSummary(),
                post.getContent(),
                post.getTags().stream().map(Tag::getName).toList()
        );
    }

    @Transactional
    public MyPostSummaryResponse updateMyPost(Long postId,
                                              UpdateMyPostRequest request,
                                              AuthenticatedUser authenticatedUser) {
        Post post = getOwnedPost(postId, authenticatedUser);
        post.setTitle(request.title().trim());
        post.setSummary(request.summary().trim());
        post.setContent(request.content().trim());
        post.setTags(resolveTags(request.tags()));
        post.setUpdatedAt(LocalDateTime.now());
        return toSummaryResponse(post);
    }

    @Transactional
    public MyPostSummaryResponse hideMyPost(Long postId, AuthenticatedUser authenticatedUser) {
        Post post = getOwnedPost(postId, authenticatedUser);
        post.setStatus(PostStatus.HIDDEN);
        post.setUpdatedAt(LocalDateTime.now());
        return toSummaryResponse(post);
    }

    private Post getOwnedPost(Long postId, AuthenticatedUser authenticatedUser) {
        return postRepository.findById(postId)
                .filter(post -> post.getAuthor().getId().equals(authenticatedUser.getSourceUser().getId()))
                .orElseThrow(() -> new ApiException("文章不存在或无权操作"));
    }

    private Set<Tag> resolveTags(List<String> rawTags) {
        Set<String> normalizedTags = rawTags.stream()
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(tag -> tag.length() > 20 ? tag.substring(0, 20) : tag)
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

        if (normalizedTags.isEmpty()) {
            throw new ApiException("至少需要一个有效标签");
        }

        Set<Tag> tags = new LinkedHashSet<>();
        for (String normalizedTag : normalizedTags) {
            String slug = toSlug(normalizedTag);
            Tag tag = tagRepository.findBySlug(slug).orElseGet(() -> createTag(normalizedTag, slug));
            tags.add(tag);
        }
        return tags;
    }

    private Tag createTag(String name, String slug) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setSlug(slug);
        return tagRepository.save(tag);
    }

    private String toSlug(String value) {
        String slug = value.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\u4e00-\\u9fa5]+", "-")
                .replaceAll("^-+|-+$", "");

        if (!StringUtils.hasText(slug)) {
            throw new ApiException("标签格式不合法");
        }

        return slug;
    }

    private MyPostSummaryResponse toSummaryResponse(Post post) {
        return new MyPostSummaryResponse(
                post.getId(),
                post.getTitle(),
                post.getSummary(),
                post.getStatus(),
                post.getCreatedAt(),
                post.getLikeCount(),
                post.getCommentCount()
        );
    }
}
