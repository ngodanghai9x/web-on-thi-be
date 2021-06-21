package vn.actvn.onthionline.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Integer id;
    private String content;
    private Integer parentId;
    private List<String> userLiked;
    private Date createdDate;
    private Date updatedDate;
    private String fullNameUserCreated;
    private String username;
    private String avatarBase64;
    private List<CommentDto> replyComment;
}
