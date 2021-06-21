package vn.actvn.onthionline.repository.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.actvn.onthionline.domain.QuestionResult;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.List;

public class QuestionResultConverter implements AttributeConverter<List<QuestionResult>, String> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<QuestionResult> attribute) {
        String examAnswerJson = null;
        try {
            examAnswerJson = objectMapper.writeValueAsString(attribute);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }

        return examAnswerJson;
    }

    @Override
    public List<QuestionResult> convertToEntityAttribute(String dbData) {
        List<QuestionResult> questionResults = null;
        try {
            questionResults = objectMapper.readValue(
                    dbData,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, QuestionResult.class));
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return questionResults;
    }
}
