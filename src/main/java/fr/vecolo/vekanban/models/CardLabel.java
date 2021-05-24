package fr.vecolo.vekanban.models;

import fr.vecolo.vekanban.utils.DateAudit;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CARD_LABEL")
public class CardLabel extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARD_LABEL_ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "NAME", updatable = true, nullable = false)
    private String name;

    @Column(name = "COLOR", updatable = true)
    private String color;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    @ManyToMany(mappedBy = "labels")
    private List<Card> associatedCards;

    public CardLabel() {
    }

    public CardLabel(String name, Board board) {
        this.name = name;
        this.board = board;
    }

    public CardLabel(String name, String color, Board board) {
        this.name = name;
        this.color = color;
        this.board = board;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Card> getAssociatedCards() {
        return associatedCards;
    }

    public void setAssociatedCards(List<Card> associatedCards) {
        this.associatedCards = associatedCards;
    }

    @Override
    public String toString() {
        return "CardLabel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", board=" + board +
                "} " + super.toString();
    }
}
