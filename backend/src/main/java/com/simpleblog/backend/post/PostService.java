package com.simpleblog.backend.post;

import com.simpleblog.backend.auth.AuthenticatedUser;
import com.simpleblog.backend.common.ApiException;
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
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public PostDetailResponse createPost(CreatePostRequest request, AuthenticatedUser authenticatedUser) {
        Post post = new Post();
        post.setAuthor(authenticatedUser.getSourceUser());
        post.setTitle(request.title().trim());
        post.setSummary(request.summary().trim());
        post.setContent(request.content().trim());
        post.setStatus(PostStatus.PUBLISHED);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setViewCount(0);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setTags(resolveTags(request.tags()));

        Post savedPost = postRepository.save(post);
        return PostMapper.toDetailResponse(savedPost);
    }

    @Transactional(readOnly = true)
    public List<PostSummaryResponse> getPublishedPosts() {
        return postRepository.findAllByStatusOrderByCreatedAtDesc(PostStatus.PUBLISHED).stream()
                .map(PostMapper::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPublishedPost(Long id) {
        Post post = postRepository.findByIdAndStatus(id, PostStatus.PUBLISHED)
                .orElseThrow(() -> new ApiException("文章不存在或不可查看"));
        return PostMapper.toDetailResponse(post);
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
}
