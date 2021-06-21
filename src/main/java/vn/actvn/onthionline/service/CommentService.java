package vn.actvn.onthionline.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.domain.Comment;
import vn.actvn.onthionline.domain.Exam;
import vn.actvn.onthionline.domain.Question;
import vn.actvn.onthionline.domain.User;
import vn.actvn.onthionline.repository.CommentRepository;
import vn.actvn.onthionline.repository.ExamRepository;
import vn.actvn.onthionline.repository.UserRepository;
import vn.actvn.onthionline.service.dto.CommentDto;
import vn.actvn.onthionline.service.dto.CommentMessageDto;
import vn.actvn.onthionline.service.dto.GetAllCommentDto;
import vn.actvn.onthionline.service.dto.LikeDto;
import vn.actvn.onthionline.service.mapper.CommentMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CommentMapper commentMapper;

    public CommentDto addComment(CommentMessageDto message) throws IOException {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(message.getUsername()));
        if (!user.isPresent()) return null;
        Optional<Exam> exam = examRepository.findById(message.getExamId());

        Comment comment = new Comment();
        comment.setParentId(message.getParentId());
        comment.setContent(message.getContent());
        comment.setCreatedDate(new Date());
        comment.setUpdatedDate(new Date());
        comment.setUserCreated(user.get());
        comment.setExam(exam.get());

        Optional<Comment> newComment = Optional.of(
                Optional.of(commentRepository.saveAndFlush(comment)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(savedComment -> {
//                    LOGGER.info("New Comment {}", savedComment);
                    return savedComment;
                });

        return commentMapper.toDto(newComment.get());
    }

    public List<CommentDto> getAll(GetAllCommentDto request) {
        List<Comment> comments = commentRepository.findAllByExamId(request.getExamId());
        List<CommentDto> commentDtos = handleListEntityToDto(comments, null, request.getUsername());
        commentDtos.stream().forEach(parent -> {
            parent.setReplyComment(handleListEntityToDto(comments, parent.getId(), request.getUsername()));
        });
//        LOGGER.info("Get All Comment {}", commentDtos);
        return commentDtos;
    }

    private List<CommentDto> handleListEntityToDto(List<Comment> comments, Integer parentId, String username) {
        List<Comment> comment = comments.stream().filter(cmt -> parentId == cmt.getParentId()).collect(Collectors.toList());
        List<CommentDto> commentDtos = new ArrayList<>();
        comment.stream().forEach(cmt -> {
            CommentDto commentDto = null;
            try {
                commentDto = commentMapper.toDto(cmt);
            } catch (IOException e) {
                e.printStackTrace();
            }
            commentDtos.add(commentDto);
        });
        return commentDtos;
    }

    public CommentDto like(LikeDto request) throws IOException {
        Optional<Comment> comment = commentRepository.findById(request.getCommentId());
        if (!comment.isPresent()) return null;

        if (request.getType() == 1) {
            List<String> userLiked = commentMapper.getUserLikedToList(comment.get().getUserLiked());
            if (userLiked.size() > 0)
                userLiked = userLiked.stream().filter(username -> !username.equalsIgnoreCase(request.getUsername())).collect(Collectors.toList());
            userLiked.add(request.getUsername());
            comment.get().setUserLiked(commentMapper.listUserLikedToString(userLiked));
            Optional<Comment> newComment = Optional.of(
                    Optional.of(commentRepository.saveAndFlush(comment.get())))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(savedComment -> {
//                        LOGGER.info("New Like For Comment {}", savedComment);
                        return savedComment;
                    });
            return commentMapper.toDto(newComment.get());
        } else {
            List<String> userLiked = commentMapper.getUserLikedToList(comment.get().getUserLiked());
            if (userLiked.size() > 0)
                userLiked = userLiked.stream().filter(username -> !username.equalsIgnoreCase(request.getUsername())).collect(Collectors.toList());
            comment.get().setUserLiked(commentMapper.listUserLikedToString(userLiked));
            Optional<Comment> newComment = Optional.of(
                    Optional.of(commentRepository.saveAndFlush(comment.get())))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(savedComment -> {
//                        LOGGER.info("Un Like For Comment {}", savedComment);
                        return savedComment;
                    });
            return commentMapper.toDto(newComment.get());
        }
    }
}
