package com.example.demo.dto.question;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class AnswerForQuestionRequest {
    @NotBlank(message = "answer body can not be empty")
    @Size(min = 1, max = 1000, message = "Length of body must be from 1 to 1000 symbol")
    private String answer;

//    @NotEmpty(message = "answer can not be empty")
//    private List<@NotBlank(message = "Each answer must not be blank") String> answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
