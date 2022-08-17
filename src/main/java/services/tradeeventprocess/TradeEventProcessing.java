package services.tradeeventprocess;

import dto.*;
import exception.CustomException;
import exception.CustomExceptionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TradeEventProcessing implements ITradeEventProcessing{

    @Override
    public void inputTradeEvent() throws CustomException, IOException {

        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateResult = new HashMap<>();
        Map<TradeIdAccountIdentifier, TradeOperation> allFinalTradeEvents = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter \"exit\" at any point of time to execution and get the output");

            while(true){
                String[] tradeEventEach = bufferedReader.readLine().split(" ");
                if(tradeEventEach.length != 7){
                    displayTradeEvent(tradeAggregateResult);
                    break;
                }
                tradeAggregateResult = createTradeEvents(tradeEventEach, allFinalTradeEvents, tradeAggregateResult);
            }
    }

    @Override
    public void tradeEventProcess(String[] tradeEventArr, Map<TradeAccountIdentifier, TradeQuantity> tradeUpdate) throws CustomException {

        TradeAccountIdentifier currentTradeAccIdentifier = new TradeAccountIdentifier();
        TradeQuantity currentTradeQuantity = new TradeQuantity();
        TradeEventId currentTradeEventId = new TradeEventId();
        Set<TradeEventId> currentTradeEventSet = new HashSet<>();

        currentTradeAccIdentifier.setAccount(tradeEventArr[5]);
        currentTradeAccIdentifier.setSecurityIdentifier(tradeEventArr[2]);
        currentTradeQuantity.setQuantity(Integer.parseInt(tradeEventArr[3]));
        currentTradeEventId.setTradeEventId(Long.parseLong(tradeEventArr[0]));
        currentTradeEventSet.add(currentTradeEventId);
        currentTradeQuantity.setTrades(currentTradeEventSet);

        if(tradeUpdate.containsKey(currentTradeAccIdentifier)){
            TradeQuantity previousTradeQuantity = tradeUpdate.get(currentTradeAccIdentifier);
            TradeQuantity finalTradeQuantity = new TradeQuantity();
            Set<TradeEventId> previousTradeEventSet = previousTradeQuantity.getTrades();
            if(tradeEventArr[4].equalsIgnoreCase("BUY")){
                if(tradeEventArr[6].equalsIgnoreCase("CANCEL")){
                    finalTradeQuantity.setQuantity(0);
                    previousTradeEventSet.add(currentTradeEventId);
                    finalTradeQuantity.setTrades(previousTradeEventSet);
                } else if(tradeEventArr[6].equalsIgnoreCase("NEW") || tradeEventArr[6].equalsIgnoreCase("AMEND")){
                    finalTradeQuantity.setQuantity(previousTradeQuantity.getQuantity() + currentTradeQuantity.getQuantity());
                    previousTradeEventSet.add(currentTradeEventId);
                    finalTradeQuantity.setTrades(previousTradeEventSet);
                }
            } else if(tradeEventArr[4].equalsIgnoreCase("SELL")){
                if(tradeEventArr[6].equalsIgnoreCase("CANCEL")){
                    finalTradeQuantity.setQuantity(0);
                    previousTradeEventSet.add(currentTradeEventId);
                    finalTradeQuantity.setTrades(previousTradeEventSet);
                } else if(tradeEventArr[6].equalsIgnoreCase("NEW") || tradeEventArr[6].equalsIgnoreCase("AMEND")){
                    finalTradeQuantity.setQuantity(previousTradeQuantity.getQuantity() - currentTradeQuantity.getQuantity());
                    previousTradeEventSet.add(currentTradeEventId);
                    finalTradeQuantity.setTrades(previousTradeEventSet);
                }
            }
            tradeUpdate.put(currentTradeAccIdentifier, finalTradeQuantity);
        } else{
            if(tradeEventArr[4].equalsIgnoreCase("SELL"))
                currentTradeQuantity.setQuantity(-currentTradeQuantity.getQuantity());
            tradeUpdate.put(currentTradeAccIdentifier, currentTradeQuantity);
        }

    }


    @Override
    public Map<TradeAccountIdentifier, TradeQuantity> createTradeEvents(String[] tradeEventArr, Map<TradeIdAccountIdentifier, TradeOperation> tradeUpdatedEvents,
                                  Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateUpdate) throws CustomException {

        validateInputs(tradeEventArr, tradeAggregateUpdate);
        TradeIdAccountIdentifier currentTradeIdAccIdentifier = new TradeIdAccountIdentifier();
        TradeOperation currentTradeOperation = new TradeOperation();
        TradeAccountIdentifier currentTradeAccountIdentifier = new TradeAccountIdentifier();

        currentTradeIdAccIdentifier.setAccount(tradeEventArr[5])
                .setSecurityIdentifier(tradeEventArr[2])
                .setTradeId(Long.parseLong(tradeEventArr[0]));

        currentTradeOperation.setVersion(Integer.parseInt(tradeEventArr[1]))
                .setOperation(tradeEventArr[6])
                .setTradeDirection(tradeEventArr[4])
                .setTradeQuantity(Integer.parseInt(tradeEventArr[3]));

        currentTradeAccountIdentifier.setAccount(tradeEventArr[5])
                .setSecurityIdentifier(tradeEventArr[2]);

        if(tradeUpdatedEvents.containsKey(currentTradeIdAccIdentifier)){

            TradeOperation previousTradeOperation = tradeUpdatedEvents.get(currentTradeIdAccIdentifier);
            if(previousTradeOperation.getVersion()< currentTradeOperation.getVersion()){

                tradeAggregateUpdate.remove(currentTradeAccountIdentifier);
                tradeUpdatedEvents.remove(currentTradeIdAccIdentifier);
                tradeEventProcess(tradeEventArr, tradeAggregateUpdate);

                for(Map.Entry<TradeIdAccountIdentifier, TradeOperation> tradeUpdateEventsReprocess : tradeUpdatedEvents.entrySet()){
                    TradeIdAccountIdentifier tradeIdAccountIdentifyReprocess = tradeUpdateEventsReprocess.getKey();
                    TradeOperation tradeOperationReprocess = tradeUpdateEventsReprocess.getValue();

                    if(tradeIdAccountIdentifyReprocess.getAccount().equals(currentTradeIdAccIdentifier.getAccount()) &&
                            tradeIdAccountIdentifyReprocess.getSecurityIdentifier().equals(currentTradeIdAccIdentifier.getSecurityIdentifier())){
                        String[] tradeEvents = new String[7];
                        tradeEvents[0] = String.valueOf(tradeIdAccountIdentifyReprocess.getTradeId());
                        tradeEvents[1] = String.valueOf(tradeOperationReprocess.getVersion());
                        tradeEvents[2] = tradeIdAccountIdentifyReprocess.getSecurityIdentifier();
                        tradeEvents[3] = String.valueOf(tradeOperationReprocess.getTradeQuantity());
                        tradeEvents[4] = tradeOperationReprocess.getTradeDirection();
                        tradeEvents[5] = tradeIdAccountIdentifyReprocess.getAccount();
                        tradeEvents[6] = tradeOperationReprocess.getOperation();

                        tradeEventProcess(tradeEvents, tradeAggregateUpdate);
                    }
                }

                tradeUpdatedEvents.put(currentTradeIdAccIdentifier, currentTradeOperation);
            }
        } else{
            if(tradeEventArr[4].equalsIgnoreCase("SELL"))
                currentTradeOperation.setTradeQuantity(-currentTradeOperation.getTradeQuantity());

            tradeUpdatedEvents.put(currentTradeIdAccIdentifier, currentTradeOperation);
            tradeEventProcess(tradeEventArr,tradeAggregateUpdate);
        }

        return tradeAggregateUpdate;
    }

    private void validateInputs(String[] tradeEventArr, Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateResult) throws CustomException {

        for(String eachTradeItem : tradeEventArr){
            if(eachTradeItem == null || eachTradeItem.isEmpty()) {
                displayTradeEvent(tradeAggregateResult);
                throw new CustomException(CustomExceptionType.INVALID_INPUT);
            }
        }

        if(!tradeEventArr[4].equalsIgnoreCase("BUY") && !tradeEventArr[4].equalsIgnoreCase("SELL")) {
            displayTradeEvent(tradeAggregateResult);
            throw new CustomException(CustomExceptionType.INVALID_TRADE_DIRECTION);
        }

        if(!tradeEventArr[6].equalsIgnoreCase("NEW") && !tradeEventArr[6].equalsIgnoreCase("AMEND")
                && !tradeEventArr[6].equalsIgnoreCase("CANCEL")){
            displayTradeEvent(tradeAggregateResult);
            throw new CustomException(CustomExceptionType.INVALID_TRADE_OPERATION);
        }

        try{
            Long.parseLong(tradeEventArr[0]);
            Integer.parseInt(tradeEventArr[1]);
            Integer.parseInt(tradeEventArr[3]);
        } catch (Exception ce){
            displayTradeEvent(tradeAggregateResult);
            throw new CustomException(CustomExceptionType.INVALID_VALUE);
        }
    }

    private static void displayTradeEvent(Map<TradeAccountIdentifier, TradeQuantity> tradeFinal){

        for(Map.Entry<TradeAccountIdentifier, TradeQuantity> TradeEntry : tradeFinal.entrySet()){
            TradeAccountIdentifier displayTradeAccIdentifier = TradeEntry.getKey();
            TradeQuantity displayTradeQuantity = TradeEntry.getValue();

            System.out.print(displayTradeAccIdentifier.getAccount() + " " + displayTradeAccIdentifier.getSecurityIdentifier() + " ");
            System.out.print(displayTradeQuantity.getQuantity() + " ");
            for(TradeEventId displayTradeEvents : displayTradeQuantity.getTrades())
                System.out.print(displayTradeEvents.getTradeEventId() + ",");

            System.out.println("");
        }

    }
}
