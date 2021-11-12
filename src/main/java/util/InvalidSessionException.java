package util;

public class InvalidSessionException extends Exception{
    public InvalidSessionException(String errorMessage){
        super(errorMessage);
    }
}
