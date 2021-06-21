package vn.actvn.onthionline.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingDto {
    private String fullName;
    private String avatarBase64;
    private Integer numCorrectAns;
    private Integer totalQuestion;
}
