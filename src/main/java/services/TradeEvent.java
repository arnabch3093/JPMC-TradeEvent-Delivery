package services;

import services.tradeeventprocess.ITradeEventProcessing;
import services.tradeeventprocess.TradeEventProcessing;

public class TradeEvent {

    public static void main(String[] args) {

        ITradeEventProcessing service = new TradeEventProcessing();
        try{
            service.inputTradeEvent();
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }
}
