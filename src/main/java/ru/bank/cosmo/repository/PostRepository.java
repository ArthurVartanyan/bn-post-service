package ru.bank.cosmo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bank.cosmo.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
