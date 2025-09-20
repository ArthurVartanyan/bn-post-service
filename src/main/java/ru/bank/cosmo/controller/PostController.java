package ru.bank.cosmo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bank.cosmo.service.PostService;

import java.util.Map;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * Создание поста
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createPost(
            @RequestParam Long companyId,
            @RequestParam String content,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        Long postId = postService.createPost(companyId, content, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("postId", postId));
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> editPost(
            @PathVariable Long postId,
            @RequestParam String content,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        Long updatedPostId = postService.editPost(postId, content, file);
        return ResponseEntity.ok(Map.of("postId", updatedPostId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long postId) {
        boolean deleted = postService.deletePost(postId);
        return ResponseEntity.ok(Map.of("deleted", deleted));
    }
}
