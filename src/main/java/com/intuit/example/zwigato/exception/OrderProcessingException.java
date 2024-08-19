package com.intuit.example.zwigato.exception;

public class OrderProcessingException extends RuntimeException{

    public OrderProcessingException(String message) {
        super(message);
    }

}
