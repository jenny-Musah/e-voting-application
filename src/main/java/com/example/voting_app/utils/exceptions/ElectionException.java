package com.example.voting_app.utils.exceptions;

public class ElectionException extends IllegalArgumentException{

    public ElectionException(String message){
        super(message);
    }
}
