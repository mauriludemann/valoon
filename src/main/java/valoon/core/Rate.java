package valoon.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "rate")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rate {

    @JsonProperty(value = "rate_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne()
    @JoinColumn(name = "dealer_id")
    private Dealer dealer;

    public Rate() { }

    public Rate(int score, String comment, Dealer dealer) {
        this.score = score;
        this.comment = comment;
        this.dealer = dealer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }
}
