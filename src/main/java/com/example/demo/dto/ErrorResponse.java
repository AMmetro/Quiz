package com.example.demo.dto;

import java.util.List;

public class ErrorResponse {
    private List<ErrorMessage> errorsMessages;

    public ErrorResponse(List<ErrorMessage> errorsMessages) {
        this.errorsMessages = errorsMessages;
    }

    public List<ErrorMessage> getErrorsMessages() {
        return errorsMessages;
    }

    public void setErrorsMessages(List<ErrorMessage> errorsMessages) {
        this.errorsMessages = errorsMessages;
    }

    public static class ErrorMessage {
        private String message;
        private String field;

        public ErrorMessage(String message, String field) {
            this.message = message;
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }
} 