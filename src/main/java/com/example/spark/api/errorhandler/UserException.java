package com.example.spark.api.errorhandler;

public class UserException extends RuntimeException  {

    private String message;
    private int error_code = 404;

    public String getMessage(){ return message; }
    public int getError_code(){ return error_code; }
    public UserException(String message){
       // super(message);
        this.message=message;
    }
    public UserException(String message, int error_code){
        this.message=message;
        this.error_code=error_code;
    }
    public UserException(){ super(); }

}
