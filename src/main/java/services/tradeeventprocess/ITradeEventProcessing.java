package services.tradeeventprocess;

import dto.TradeAccountIdentifier;
import dto.TradeIdAccountIdentifier;
import dto.TradeOperation;
import dto.TradeQuantity;
import exception.CustomException;

import java.io.IOException;
import java.util.Map;

public interface ITradeEventProcessing {

    void inputTradeEvent() throws CustomException, IOException;
    void tradeEventProcess(String[] tradeEventArr, Map<TradeAccountIdentifier, TradeQuantity> tradeUpdate) throws CustomException;
    Map<TradeAccountIdentifier, TradeQuantity> createTradeEvents(String[] tradeEventArr, Map<TradeIdAccountIdentifier, TradeOperation> tradeUpdatedEvents,
                           Map<TradeAccountIdentifier, TradeQuantity> tradeRemove) throws CustomException;
}
