package ru.bank.cosmo.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bank.cosmo.model.Post;
import ru.bank.cosmo.repository.PostRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${minio.bucket}")
    private String bucket;

    private final MinioClient minio;
    private final PostRepository postRepository;

    public Long createPost(Long companyId, String content, MultipartFile file) {
        var objectKey = addImage(file, companyId);
        Post post = new Post();
        post.setCompanyId(companyId);
        post.setContent(content);
        post.setImagePath(objectKey);
        return postRepository.save(post).getId();
    }

    public Long editPost(Long postId, String newContent, MultipartFile file) {
        return postRepository.findById(postId).map(post -> {
            var newObjectKey = addImage(file, post.getCompanyId());
            post.setImagePath(newObjectKey);
            post.setContent(newContent);
            return postRepository.save(post).getId();
        }).orElseThrow(() -> new RuntimeException("Поста с таким ИД не существует!"));
    }

    public boolean deletePost(Long postId) {
        return postRepository.findById(postId).map(post -> {
            deleteImage(post.getImagePath());
            postRepository.delete(post);
            return true;
        }).orElseThrow(() -> new RuntimeException("Поста с таким ИД не существует!"));
    }

    @SneakyThrows
    private String addImage(MultipartFile file, Long companyId) {
        String objectKey = "post/companies/" + companyId + "/" +
                UUID.randomUUID() + "-" + file.getOriginalFilename();

        minio.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectKey)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build());
        return objectKey;
    }

    @SneakyThrows
    private void deleteImage(String path) {
        minio.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(path)
                        .build()
        );
    }
}