package com.bezkoder.spring.hibernate.onetomany.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.hibernate.onetomany.exception.ResourceNotFoundException;
import com.bezkoder.spring.hibernate.onetomany.model.Comment;
import com.bezkoder.spring.hibernate.onetomany.model.Tutorial;
import com.bezkoder.spring.hibernate.onetomany.repository.CommentRepository;
import com.bezkoder.spring.hibernate.onetomany.repository.TutorialRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CommentController {

  @Autowired
  private TutorialRepository tutorialRepository;

  @Autowired
  private CommentRepository commentRepository;

  @GetMapping("/tutorials/{tutorialId}/comments")
  public ResponseEntity<List<Comment>> getAllCommentsByTutorialId(@PathVariable(value = "tutorialId") Long tutorialId) {    
    Tutorial tutorial = tutorialRepository.findById(tutorialId)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId));

    List<Comment> comments = new ArrayList<Comment>();
    comments.addAll(tutorial.getComments());
    
    return new ResponseEntity<>(comments, HttpStatus.OK);
  }

  @GetMapping("/comments/{id}")
  public ResponseEntity<Comment> getCommentsByTutorialId(@PathVariable(value = "id") Long id) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Comment with id = " + id));

    return new ResponseEntity<>(comment, HttpStatus.OK);
  }

  @PostMapping("/tutorials/{tutorialId}/comments")
  public ResponseEntity<Comment> createComment(@PathVariable(value = "tutorialId") Long tutorialId,
      @RequestBody Comment commentRequest) {
    Comment comment = tutorialRepository.findById(tutorialId).map(tutorial -> {
      tutorial.getComments().add(commentRequest);
      return commentRepository.save(commentRequest);
    }).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId));

    return new ResponseEntity<>(comment, HttpStatus.CREATED);
  }

  @PutMapping("/comments/{id}")
  public ResponseEntity<Comment> updateComment(@PathVariable("id") long id, @RequestBody Comment commentRequest) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("CommentId " + id + "not found"));

    comment.setContent(commentRequest.getContent());

    return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.OK);
  }

  @DeleteMapping("/comments/{id}")
  public ResponseEntity<HttpStatus> deleteComment(@PathVariable("id") long id) {
    commentRepository.deleteById(id);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  
  @DeleteMapping("/tutorials/{tutorialId}/comments")
  public ResponseEntity<List<Comment>> deleteAllCommentsOfTutorial(@PathVariable(value = "tutorialId") Long tutorialId) {
    Tutorial tutorial = tutorialRepository.findById(tutorialId)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId));
    
    tutorial.removeComments();
    tutorialRepository.save(tutorial);
    
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
