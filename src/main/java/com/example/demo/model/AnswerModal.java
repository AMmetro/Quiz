package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerModal {
    String questionId;
    String answerStatus;
    String addedAt;

    public static AnswerModal toModalMapper(String questionId, boolean status, String addedAt){
        AnswerModal answer = new AnswerModal();
        answer.setQuestionId(questionId);
        answer.setAnswerStatus(status);
        answer.setAddedAt(addedAt);
        return answer;

    }


    public void setQuestionId (String questionId) {
        this.questionId = questionId;
    }

    public void setAnswerStatus (boolean status) {
        this.answerStatus = status ? "Correct" : "Incorrect" ;
    }

    public void setAddedAt (String addedAt) {
        this.addedAt = addedAt;
    }

}
