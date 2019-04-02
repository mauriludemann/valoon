package valoon.core;


import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

@Entity
@DiscriminatorValue("CLIENT")
@Table(name = "client")
public class Client extends ValoonUser {

    @Column(name = "credit", nullable = false)
    private Double credit = 0d;

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Boolean hasEnoughCredit(Double amount) {
        return this.credit >= amount;
    }

}
