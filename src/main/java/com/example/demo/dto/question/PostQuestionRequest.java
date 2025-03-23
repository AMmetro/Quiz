package com.example.demo.dto.question;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class PostQuestionRequest {
    @NotBlank(message = "question body can not be empty")
    @Size(min = 10, max = 500, message = "Length of body must be from 10 to 500 symbol")
    private String body;

    @NotEmpty(message = "correctAnswers can not be empty")
    private List<@NotBlank(message = "Each answer must not be blank") String> correctAnswers;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
