package com.marketlogicsoftware.pollingservice.model.audit;

public class AppError {

    private String ErrorMessage;

    public AppError(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
