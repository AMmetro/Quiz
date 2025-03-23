package com.example.demo.dto.question;

import com.example.demo.dto.validators.ValidBoolean;

import javax.validation.constraints.NotNull;

public class PublishQuestionRequest {

    @NotNull(message = "published field is required")
    @ValidBoolean(message = "published must be a boolean value (true or false), not a string or number")
    private Object published;

    public Boolean getPublished() {
        return published instanceof Boolean ? (Boolean) published : null;
    }

    public void setPublished(Object published) {
        this.published = published;
    }
}
