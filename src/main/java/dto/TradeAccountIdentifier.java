package dto;

import java.util.Objects;

public class TradeAccountIdentifier {

    private String account;
    private String securityIdentifier;

    public String getAccount() {
        return account;
    }

    public TradeAccountIdentifier setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getSecurityIdentifier() {
        return securityIdentifier;
    }

    public TradeAccountIdentifier setSecurityIdentifier(String securityIdentifier) {
        this.securityIdentifier = securityIdentifier;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TradeAccountIdentifier that = (TradeAccountIdentifier) o;

        return Objects.equals(account, that.account) &&
                Objects.equals(securityIdentifier, that.securityIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, securityIdentifier);
    }
}
