package com.example.voting_app.utils.exceptions;

public class InvalidDetails extends IllegalArgumentException{
        public InvalidDetails(String message){
            super(message);
        }
}
