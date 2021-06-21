package vn.actvn.onthionline.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import vn.actvn.onthionline.service.CommentService;
import vn.actvn.onthionline.service.dto.CommentDto;
import vn.actvn.onthionline.service.dto.CommentMessageDto;
import vn.actvn.onthionline.service.dto.GetAllCommentDto;
import vn.actvn.onthionline.service.dto.LikeDto;

import java.io.IOException;
import java.util.List;

@RestController
public class CommentResource {
    private Logger LOGGER = LoggerFactory.getLogger(CommentResource.class);

    @Autowired
    private CommentService commentService;

    @MessageMapping("exam/{id}/initComment")
    @SendTo("/exam/{id}")
    public List<CommentDto> initComment(@DestinationVariable String id, @Payload GetAllCommentDto getAllCommentDto) {
        return commentService.getAll(getAllCommentDto);
    }

    @MessageMapping("exam/{id}/comment")
    @SendTo("/exam/{id}")
    public CommentDto newComment(@DestinationVariable String id, @Payload CommentMessageDto comment) throws IOException {
        return commentService.addComment(comment);
    }

    @MessageMapping("exam/{id}/like")
    @SendTo("/exam/{id}")
    public CommentDto like(@DestinationVariable String id, @Payload LikeDto request) throws IOException {
        LOGGER.info("Like {}", request);
        return commentService.like(request);
    }
}
