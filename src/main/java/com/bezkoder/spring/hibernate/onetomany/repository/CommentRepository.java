package com.bezkoder.spring.hibernate.onetomany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.hibernate.onetomany.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
