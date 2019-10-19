package com.microservices.ordersservice.model;

public class ErrorMessageDto {

    public String getError_message() {
        return error_message;
    }

    private final String error_message;

    public ErrorMessageDto(String errorMessage) {
        this.error_message = errorMessage;
    }

}
