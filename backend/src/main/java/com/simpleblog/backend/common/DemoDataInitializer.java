package com.simpleblog.backend.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpleblog.backend.post.Post;
import com.simpleblog.backend.post.PostRepository;
import com.simpleblog.backend.post.PostStatus;
import com.simpleblog.backend.tag.Tag;
import com.simpleblog.backend.tag.TagRepository;
import com.simpleblog.backend.user.User;
import com.simpleblog.backend.user.UserRepository;
import com.simpleblog.backend.user.UserRole;
import com.simpleblog.backend.user.UserStatus;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DemoDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public DemoDataInitializer(UserRepository userRepository,
                               PostRepository postRepository,
                               TagRepository tagRepository,
                               PasswordEncoder passwordEncoder,
                               ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0 || postRepository.count() > 0) {
            return;
        }

        List<DemoUserResource> userResources = readJson("demo-data/users.json", new TypeReference<>() {});
        Map<String, User> users = createUsers(userResources);

        List<DemoPostResource> postResources = readJson("demo-data/posts.json", new TypeReference<>() {});
        createPosts(postResources, users);
    }

    private Map<String, User> createUsers(List<DemoUserResource> userResources) {
        return userResources.stream()
                .map(this::createUser)
                .collect(Collectors.toMap(User::getUsername, user -> user));
    }

    private User createUser(DemoUserResource resource) {
        User user = new User();
        user.setUsername(resource.username());
        user.setNickname(resource.nickname());
        user.setPasswordHash(passwordEncoder.encode(resource.password()));
        user.setRole(resource.role());
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    private void createPosts(List<DemoPostResource> postResources, Map<String, User> users) {
        for (DemoPostResource resource : postResources) {
            User author = users.get(resource.authorUsername());
            if (author == null) {
                throw new IllegalStateException("演示文章作者不存在: " + resource.authorUsername());
            }
            createPost(author, resource);
        }
    }

    private void createPost(User author, DemoPostResource resource) {
        Post post = new Post();
        post.setAuthor(author);
        post.setTitle(resource.title());
        post.setSummary(resource.summary());
        post.setContent(readText("demo-data/" + resource.contentFile()));
        post.setStatus(PostStatus.PUBLISHED);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setViewCount(0);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setTags(resolveTags(resource.tags()));
        postRepository.save(post);
    }

    private Set<Tag> resolveTags(List<String> tagNames) {
        Set<Tag> tags = new LinkedHashSet<>();
        for (String tagName : tagNames) {
            String slug = toSlug(tagName);
            Tag tag = tagRepository.findBySlug(slug).orElseGet(() -> createTag(tagName, slug));
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
        return value.toLowerCase()
                .replaceAll("[^a-z0-9\\u4e00-\\u9fa5]+", "-")
                .replaceAll("^-+|-+$", "");
    }

    private <T> T readJson(String path, TypeReference<T> typeReference) {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException exception) {
            throw new IllegalStateException("读取演示数据 JSON 失败: " + path, exception);
        }
    }

    private String readText(String path) {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("读取演示正文失败: " + path, exception);
        }
    }

    private record DemoUserResource(
            String username,
            String nickname,
            String password,
            UserRole role
    ) {
    }

    private record DemoPostResource(
            String authorUsername,
            String title,
            String summary,
            List<String> tags,
            String contentFile
    ) {
    }
}
