package dto;

public class TradeOperation {

    private int version;
    private int tradeQuantity;
    private String tradeDirection;
    private String operation;

    public int getVersion() {
        return version;
    }

    public TradeOperation setVersion(int version) {
        this.version = version;
        return this;
    }

    public int getTradeQuantity() {
        return tradeQuantity;
    }

    public TradeOperation setTradeQuantity(int tradeQuantity) {
        this.tradeQuantity = tradeQuantity;
        return this;
    }

    public String getTradeDirection() {
        return tradeDirection;
    }

    public TradeOperation setTradeDirection(String tradeDirection) {
        this.tradeDirection = tradeDirection;
        return this;
    }

    public String getOperation() {
        return operation;
    }

    public TradeOperation setOperation(String operation) {
        this.operation = operation;
        return this;
    }
}
