package com.simpleblog.backend.post;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"author", "tags"})
    List<Post> findAllByStatusOrderByCreatedAtDesc(PostStatus status);

    @EntityGraph(attributePaths = {"author", "tags"})
    Optional<Post> findByIdAndStatus(Long id, PostStatus status);

    @EntityGraph(attributePaths = {"author", "tags"})
    List<Post> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"author", "tags"})
    List<Post> findAllByAuthorIdOrderByCreatedAtDesc(Long authorId);

    @EntityGraph(attributePaths = {"author", "tags"})
    Page<Post> findAllByStatus(PostStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "tags"})
    Page<Post> findAllByStatusAndTagsSlug(PostStatus status, String slug, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "tags"})
    List<Post> findAllByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

    @EntityGraph(attributePaths = {"author", "tags"})
    List<Post> findAllByStatusAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(PostStatus status, String keyword);

    long countByStatus(PostStatus status);
}
