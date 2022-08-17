package exception;

public class CustomException extends Exception{

    public CustomException(CustomExceptionType exceptionType) {
        super(exceptionType.getMessage());

    }
}
