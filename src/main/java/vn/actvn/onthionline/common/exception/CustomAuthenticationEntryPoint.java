package vn.actvn.onthionline.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import vn.actvn.onthionline.client.dto.BaseDataResponse;
import vn.actvn.onthionline.common.ValidationError;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
        BaseDataResponse response = new BaseDataResponse();
        response.setResponseCode("400");
        response.setResponseMessage("failed");
        List<ValidationErrorResponse> responseEntityMessages = new ArrayList<>();
        responseEntityMessages.add(new ValidationErrorResponse("token", ValidationError.ACCESS_DENIED, "Your Token Access Denied", null));
        response.setResponseEntityMessages(responseEntityMessages);
        response.setBody(null);

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(200);
        res.getWriter().write(convertObjectToJson(response));
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        return mapper.writeValueAsString(object);
    }
}
