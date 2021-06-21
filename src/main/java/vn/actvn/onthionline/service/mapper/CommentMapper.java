package vn.actvn.onthionline.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.common.Constant;
import vn.actvn.onthionline.domain.Comment;
import vn.actvn.onthionline.service.ImageService;
import vn.actvn.onthionline.service.dto.CommentDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentMapper {
    @Autowired
    private ImageService imageService;

    public CommentDto toDto(Comment comment) throws IOException {
        if (null ==  comment)
            return null;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setParentId(comment.getParentId());
        commentDto.setReplyComment(new ArrayList<>());
        commentDto.setUserLiked(getUserLikedToList(comment.getUserLiked()));
        commentDto.setCreatedDate(comment.getCreatedDate());
        commentDto.setUpdatedDate(comment.getUpdatedDate());
        commentDto.setFullNameUserCreated(comment.getUserCreated().getFullname());
        commentDto.setUsername(comment.getUserCreated().getUsername());
        if (null != comment.getUserCreated().getAvatar())
            commentDto.setAvatarBase64(imageService.getFile(comment.getUserCreated().getAvatar()));
        return commentDto;
    }

    public List<String> getUserLikedToList(String userLiked) {
        if (null == userLiked || "".equalsIgnoreCase(userLiked)) return new ArrayList<>();
        return Arrays.asList(userLiked.split(Constant.SEPARATOR).clone());
    }

    public String listUserLikedToString(List<String> userLiked) {
        String likedStr = "";
        for (int i = 0; i < userLiked.size(); i++) {
            if (i != userLiked.size() - 1) {
                likedStr += userLiked.get(i) + Constant.SEPARATOR;
            } else {
                likedStr += userLiked.get(i);
            }
        }
        return likedStr;
    }
}
