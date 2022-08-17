package exception;

public enum CustomExceptionType {

    INVALID_VALUE( "Invalid TradeID, Version or tradeQuantity"),
    INVALID_INPUT("Null Value in Input"),
    INVALID_TRADE_DIRECTION("Invalid Trade Direction Entered"),
    INVALID_TRADE_OPERATION("Invalid Trade operation Entered"),
    DEFAULT("System Error");

    private String message;

    private CustomExceptionType(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public CustomExceptionType setMessage(String message) {
        this.message = message;
        return this;
    }
}
