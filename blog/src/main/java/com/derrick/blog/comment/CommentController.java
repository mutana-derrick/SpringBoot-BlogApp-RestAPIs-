package com.derrick.blog.comment;

import com.derrick.blog.post.PostRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/posts/{postId}/comments")
@Tag(name = "Comment", description = "Comment management APIs")
public class CommentController{

     CommentRepository commentRepository;
     PostRepository postRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @PostMapping
    @Operation(summary = "Add a new comment to a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment added successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Comment> addComment(
            @Parameter(description = "ID of the post") @PathVariable Long postId,
            @Parameter(description = "Comment details") @RequestBody Comment comment) {

        return postRepository.findById(postId)
                .map(post -> {
                    comment.setPost(post);
                    Comment savedComment = commentRepository.save(comment);
                    return ResponseEntity.ok(savedComment);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all comments for a post")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@Parameter(description = "ID of the post") @PathVariable Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "Update an existing comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    public ResponseEntity<Comment> updateComment(
            @Parameter(description = "ID of the post") @PathVariable Long postId,
            @Parameter(description = "ID of the comment to update") @PathVariable Long commentId,
            @Parameter(description = "Updated comment details") @RequestBody Comment updatedComment) {

        return commentRepository.findById(commentId)
                .map(comment -> {
                    comment.setContent(updatedComment.getContent());
                    comment.setAuthor(updatedComment.getAuthor());
                    Comment savedComment = commentRepository.save(comment);
                    return ResponseEntity.ok(savedComment);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "ID of the post") @PathVariable Long postId,
            @Parameter(description = "ID of the comment to delete") @PathVariable Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
