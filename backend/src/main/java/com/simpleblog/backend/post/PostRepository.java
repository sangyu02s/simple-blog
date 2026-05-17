package com.simpleblog.backend.post;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"author", "tags"})
    List<Post> findAllByStatusOrderByCreatedAtDesc(PostStatus status);

    @EntityGraph(attributePaths = {"author", "tags"})
    Optional<Post> findByIdAndStatus(Long id, PostStatus status);
}
