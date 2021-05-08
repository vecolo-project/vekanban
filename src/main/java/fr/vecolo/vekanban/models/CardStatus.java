package fr.vecolo.vekanban.models;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CARD_STATUS")
public class CardStatus {

    public static final CardStatus TODO = new CardStatus(1, "TODO");
    public static final CardStatus IN_PROGRESS = new CardStatus(2, "IN PROGRESS");
    public static final CardStatus DONE = new CardStatus(3, "DONE");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARD_STATUS_ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "STATUS", unique = true, updatable = true, nullable = false)
    private String status;

    @OneToMany(mappedBy = "status")
    private List<Card> cards;


    public CardStatus() {
    }

    public CardStatus(long id, String status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public boolean equals(CardStatus other) {
        if (other == null) {
            return false;
        }
        return other.id == this.id && other.status.equals(this.status);
    }

    @Override
    public String toString() {
        return "CardStatus{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", cards=" + cards +
                '}';
    }
}
