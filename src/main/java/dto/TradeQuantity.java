package dto;

import java.util.Set;

public class TradeQuantity {

    private int quantity;
    private Set<TradeEventId> trades;

    public int getQuantity() {
        return quantity;
    }

    public TradeQuantity setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Set<TradeEventId> getTrades() {
        return trades;
    }

    public TradeQuantity setTrades(Set<TradeEventId> trades) {
        this.trades = trades;
        return this;
    }
}
