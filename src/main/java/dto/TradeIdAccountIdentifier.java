package dto;

import java.util.Objects;

public class TradeIdAccountIdentifier {

    private String account;
    private String securityIdentifier;
    private Long tradeId;

    public String getAccount() {
        return account;
    }

    public TradeIdAccountIdentifier setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getSecurityIdentifier() {
        return securityIdentifier;
    }

    public TradeIdAccountIdentifier setSecurityIdentifier(String securityIdentifier) {
        this.securityIdentifier = securityIdentifier;
        return this;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public TradeIdAccountIdentifier setTradeId(Long tradeId) {
        this.tradeId = tradeId;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TradeIdAccountIdentifier that = (TradeIdAccountIdentifier) o;

        return Objects.equals(account, that.account) &&
                Objects.equals(securityIdentifier, that.securityIdentifier) &&
                Objects.equals(tradeId, that.tradeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, securityIdentifier, tradeId);
    }
}
