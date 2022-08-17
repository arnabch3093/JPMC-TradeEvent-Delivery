import dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import services.tradeeventprocess.ITradeEventProcessing;
import services.tradeeventprocess.TradeEventProcessing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TradeEventServiceTest {

    @Test
    public void tradeEventsException() throws Exception {


        ITradeEventProcessing service = new TradeEventProcessing();
        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateActualResult = new HashMap<>();
        Map<TradeIdAccountIdentifier, TradeOperation> allFinalTradeEvents = new HashMap<>();

        //TestCase 1 - Invalid Trade Operation Entered
        String[] exampleInput1 = {"1234", "1", "XYZ", "100", "BUY", "ACC-1234", "AMD"};
        boolean shouldThrowExceptionForInvalidOperation = false;
        try{
            tradeAggregateActualResult = service.createTradeEvents(exampleInput1, allFinalTradeEvents, tradeAggregateActualResult);
        } catch (Exception e) {
            shouldThrowExceptionForInvalidOperation = true;
        }
        Assertions.assertTrue(shouldThrowExceptionForInvalidOperation);

        //TestCase 2 - Invalid Trade Direction Entered
        String[] exampleInput2 = {"1234", "1", "XYZ", "100", "BY", "ACC-1234", "NEW"};
        boolean shouldThrowExceptionForInvalidTradeDirection = false;
        try{
            tradeAggregateActualResult = service.createTradeEvents(exampleInput1, allFinalTradeEvents, tradeAggregateActualResult);
        } catch (Exception e) {
            shouldThrowExceptionForInvalidTradeDirection = true;
        }
        Assertions.assertTrue(shouldThrowExceptionForInvalidTradeDirection);

        //TestCase 3 - Invalid Trade Version
        String[] exampleInput3 = {"1234", "A", "XYZ", "100", "BUY", "ACC-1234", "NEW"};
        boolean shouldThrowExceptionForInvalidTradeVersion = false;
        try{
            tradeAggregateActualResult = service.createTradeEvents(exampleInput1, allFinalTradeEvents, tradeAggregateActualResult);
        } catch (Exception e) {
            shouldThrowExceptionForInvalidTradeVersion = true;
        }
        Assertions.assertTrue(shouldThrowExceptionForInvalidTradeVersion);

        //TestCase 4 - Invalid TradeID
        String[] exampleInput4 = {"12A34", "1", "XYZ", "100", "BUY", "ACC-1234", "NEW"};
        boolean shouldThrowExceptionForInvalidTradeID = false;
        try{
            tradeAggregateActualResult = service.createTradeEvents(exampleInput1, allFinalTradeEvents, tradeAggregateActualResult);
        } catch (Exception e) {
            shouldThrowExceptionForInvalidTradeID = true;
        }
        Assertions.assertTrue(shouldThrowExceptionForInvalidTradeID);

        //TestCase 5 - Invalid TradeQuantity
        String[] exampleInput5 = {"1234", "1", "XYZ", "100.80", "BUY", "ACC-1234", "NEW"};
        boolean shouldThrowExceptionForInvalidTradeQuantity = false;
        try{
            tradeAggregateActualResult = service.createTradeEvents(exampleInput1, allFinalTradeEvents, tradeAggregateActualResult);
        } catch (Exception e) {
            shouldThrowExceptionForInvalidTradeQuantity = true;
        }
        Assertions.assertTrue(shouldThrowExceptionForInvalidTradeQuantity);

        //TestCase 6 - Null Check
        String[] exampleInput6 = {"1234", "1", "null", "100", "BUY", "ACC-1234", "NEW"};
        boolean shouldThrowExceptionForNullValue = false;
        try{
            tradeAggregateActualResult = service.createTradeEvents(exampleInput1, allFinalTradeEvents, tradeAggregateActualResult);
        } catch (Exception e) {
            shouldThrowExceptionForNullValue = true;
        }
        Assertions.assertTrue(shouldThrowExceptionForNullValue);
    }

    @Test
    public void tradeEventNormalEvent() throws Exception {

        ITradeEventProcessing service = new TradeEventProcessing();
        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateActualResult = new HashMap<>();
        Map<TradeIdAccountIdentifier, TradeOperation> allFinalTradeEvents = new HashMap<>();
        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateExpectedResult = new HashMap<>();

        TradeAccountIdentifier resultTradeAccIdentifier = new TradeAccountIdentifier();
        TradeQuantity resultTradeQuantity = new TradeQuantity();
        TradeEventId resultTradeEvents = new TradeEventId();
        Set<TradeEventId> resultTradeEventSets = new HashSet<>();
        resultTradeAccIdentifier.setAccount("ACC-1234").setSecurityIdentifier("XYZ");
        resultTradeEvents.setTradeEventId(1234L);
        resultTradeEventSets.add(resultTradeEvents);
        resultTradeQuantity.setQuantity(100).setTrades(resultTradeEventSets);

        tradeAggregateExpectedResult.put(resultTradeAccIdentifier, resultTradeQuantity);

        String[] exampleInput = {"1234", "1", "XYZ", "100", "BUY", "ACC-1234", "NEW"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput, allFinalTradeEvents, tradeAggregateActualResult);

        Assertions.assertEquals(tradeAggregateExpectedResult.keySet(), tradeAggregateActualResult.keySet());
    }

    @Test
    public void tradeEventOutOfOrder() throws Exception {

        ITradeEventProcessing service = new TradeEventProcessing();
        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateActualResult = new HashMap<>();
        Map<TradeIdAccountIdentifier, TradeOperation> allFinalTradeEvents = new HashMap<>();
        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateExpectedResult = new HashMap<>();

        TradeAccountIdentifier resultTradeAccIdentifier = new TradeAccountIdentifier();
        TradeQuantity resultTradeQuantity = new TradeQuantity();
        TradeEventId resultTradeEvents = new TradeEventId();
        Set<TradeEventId> resultTradeEventSets = new HashSet<>();
        resultTradeAccIdentifier.setAccount("ACC-1234").setSecurityIdentifier("XYZ");
        resultTradeEvents.setTradeEventId(4323L);
        resultTradeEventSets.add(resultTradeEvents);
        resultTradeQuantity.setQuantity(200).setTrades(resultTradeEventSets);

        tradeAggregateExpectedResult.put(resultTradeAccIdentifier, resultTradeQuantity);

        String[] exampleInput3 = {"4323", "3", "XYZ", "200", "BUY", "ACC-1234", "AMEND"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput3, allFinalTradeEvents, tradeAggregateActualResult);
        String[] exampleInput4 = {"4323", "1", "XYZ", "10", "BUY", "ACC-1234", "NEW"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput4, allFinalTradeEvents, tradeAggregateActualResult);

        Assertions.assertEquals(tradeAggregateExpectedResult.keySet(), tradeAggregateActualResult.keySet());
    }

    @Test
    public void tradeMultipleEventAndOutOfOrder() throws Exception {

        ITradeEventProcessing service = new TradeEventProcessing();
        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateActualResult = new HashMap<>();
        Map<TradeIdAccountIdentifier, TradeOperation> allFinalTradeEvents = new HashMap<>();
        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateExpectedResult = new HashMap<>();

        TradeAccountIdentifier resultTradeAccIdentifier = new TradeAccountIdentifier();
        TradeQuantity resultTradeQuantity = new TradeQuantity();
        TradeEventId resultTradeEvents = new TradeEventId();
        Set<TradeEventId> resultTradeEventSets = new HashSet<>();
        resultTradeAccIdentifier.setAccount("ACC-1234").setSecurityIdentifier("XYZ");
        resultTradeEvents.setTradeEventId(1234L);
        resultTradeEventSets.add(resultTradeEvents);
        resultTradeEvents.setTradeEventId(4323L);
        resultTradeEventSets.add(resultTradeEvents);
        resultTradeQuantity.setQuantity(350).setTrades(resultTradeEventSets);

        tradeAggregateExpectedResult.put(resultTradeAccIdentifier, resultTradeQuantity);

        String[] exampleInput1 = {"1234", "1", "XYZ", "100", "BUY", "ACC-1234", "NEW"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput1, allFinalTradeEvents, tradeAggregateActualResult);
        String[] exampleInput2 = {"1234", "2", "XYZ", "150", "BUY", "ACC-1234", "AMEND"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput2, allFinalTradeEvents, tradeAggregateActualResult);
        String[] exampleInput3 = {"4323", "3", "XYZ", "200", "BUY", "ACC-1234", "AMEND"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput3, allFinalTradeEvents, tradeAggregateActualResult);
        String[] exampleInput4 = {"4323", "1", "XYZ", "10", "BUY", "ACC-1234", "AMEND"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput4, allFinalTradeEvents, tradeAggregateActualResult);

        Assertions.assertEquals(tradeAggregateExpectedResult.keySet(), tradeAggregateActualResult.keySet());
    }

    @Test
    public void tradeEventInOrderMultipleTrade() throws Exception {

        ITradeEventProcessing service = new TradeEventProcessing();
        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateActualResult = new HashMap<>();
        Map<TradeIdAccountIdentifier, TradeOperation> allFinalTradeEvents = new HashMap<>();
        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateExpectedResult = new HashMap<>();

        TradeAccountIdentifier resultTradeAccIdentifier = new TradeAccountIdentifier();
        TradeQuantity resultTradeQuantity = new TradeQuantity();
        TradeEventId resultTradeEvents = new TradeEventId();
        Set<TradeEventId> resultTradeEventSets = new HashSet<>();
        resultTradeAccIdentifier.setAccount("ACC-4567").setSecurityIdentifier("YUI");
        resultTradeEvents.setTradeEventId(8896L);
        resultTradeEventSets.add(resultTradeEvents);
        resultTradeEvents.setTradeEventId(6638L);
        resultTradeEventSets.add(resultTradeEvents);
        resultTradeQuantity.setQuantity(200).setTrades(resultTradeEventSets);

        tradeAggregateExpectedResult.put(resultTradeAccIdentifier, resultTradeQuantity);

        String[] exampleInput1 = {"8896", "1", "YUI", "300", "BUY", "ACC-4567", "NEW"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput1, allFinalTradeEvents, tradeAggregateActualResult);
        String[] exampleInput2 = {"6638", "1", "YUI", "100", "SELL", "ACC-4567", "NEW"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput2, allFinalTradeEvents, tradeAggregateActualResult);


        Assertions.assertEquals(tradeAggregateExpectedResult.keySet(), tradeAggregateActualResult.keySet());
    }

    @Test
    public void tradeEventCancelOrder() throws Exception {

        ITradeEventProcessing service = new TradeEventProcessing();
        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateActualResult = new HashMap<>();
        Map<TradeIdAccountIdentifier, TradeOperation> allFinalTradeEvents = new HashMap<>();
        Map<TradeAccountIdentifier, TradeQuantity> tradeAggregateExpectedResult = new HashMap<>();

        TradeAccountIdentifier resultTradeAccIdentifier = new TradeAccountIdentifier();
        TradeQuantity resultTradeQuantity = new TradeQuantity();
        TradeEventId resultTradeEvents = new TradeEventId();
        Set<TradeEventId> resultTradeEventSets = new HashSet<>();
        resultTradeAccIdentifier.setAccount("ACC-3456").setSecurityIdentifier("RET");
        resultTradeEvents.setTradeEventId(2233L);
        resultTradeEventSets.add(resultTradeEvents);
        resultTradeQuantity.setQuantity(0).setTrades(resultTradeEventSets);

        tradeAggregateExpectedResult.put(resultTradeAccIdentifier, resultTradeQuantity);

        String[] exampleInput1 = {"2233", "1", "RET", "100", "SELL", "ACC-3456", "NEW"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput1, allFinalTradeEvents, tradeAggregateActualResult);
        String[] exampleInput2 = {"2233", "2", "RET", "400", "SELL", "ACC-3456", "AMEND"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput2, allFinalTradeEvents, tradeAggregateActualResult);
        String[] exampleInput3 = {"2233", "3", "RET", "0", "SELL", "ACC-3456", "CANCEL"};
        tradeAggregateActualResult = service.createTradeEvents(exampleInput3, allFinalTradeEvents, tradeAggregateActualResult);

        Assertions.assertEquals(tradeAggregateExpectedResult.keySet(), tradeAggregateActualResult.keySet());
    }

}
