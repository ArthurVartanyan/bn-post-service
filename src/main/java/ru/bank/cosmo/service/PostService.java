package ru.bank.cosmo.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bank.cosmo.model.Post;
import ru.bank.cosmo.repository.PostRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${minio.bucket}")
    private String bucket;

    private final MinioClient minio;
    private final PostRepository postRepository;

    public Post createPost(Long companyId, String content, String objectKey) {
        Post post = new Post();
        post.setCompanyId(companyId);
        post.setContent(content);
        post.setImagePath(objectKey);

        return postRepository.save(post);
    }

    public Optional<Post> editPost(Long postId, String newContent, String newObjectKey) {
        return postRepository.findById(postId).map(post -> {
            post.setContent(newContent);

            if (newObjectKey != null) {
                post.setImagePath(newObjectKey);
            }

            return postRepository.save(post);
        });
    }

    public boolean deletePost(Long postId) {
        return postRepository.findById(postId).map(post -> {
            postRepository.delete(post);
            return true;
        }).orElse(false);
    }

    @SneakyThrows
    public String addImage(MultipartFile file, Long companyId) {
        String objectKey = "logo/companies/" + companyId + "/" +
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

    public String editImage(String oldObjectKey, MultipartFile newFile, UUID companyId) {
        deleteImage(oldObjectKey);
        return addImage(newFile, companyId);
    }

    public void deleteImage(String objectKey) {

    }
}