package vn.actvn.onthionline.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Description;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {

    private List<String> recipients;
    private List<String> ccList;
    private List<String> bccList;
    private String subject;
    private String body;
    private Boolean isHtml;
    private String attachmentPath;

}
