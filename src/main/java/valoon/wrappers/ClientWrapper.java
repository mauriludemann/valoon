package valoon.wrappers;

public class ClientWrapper extends ValoonUserWrapper {

    private Double credit;

    public ClientWrapper() { }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }
}
