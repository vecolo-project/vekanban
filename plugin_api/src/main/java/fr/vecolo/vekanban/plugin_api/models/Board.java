package fr.vecolo.vekanban.plugin_api.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "BOARD")
public class Board extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "PROJECT_NAME", updatable = true, nullable = false)
    private String name;

    @Column(name = "PROJECT_DESCRIPTION", updatable = true, nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "CARD_ID_PREFIX", updatable = true, nullable = true)
    private String cardIdPrefix;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @ManyToMany()
    private List<User> members;

    @OneToMany(mappedBy = "assignedBoard")
    private List<Card> cards;

    @OneToMany(mappedBy = "board")
    private List<CardLabel> labels;

    public Board() {
    }

    public Board(long id, String name, User owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public Board(String name, User owner) {
        this.name = name;
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getCardIdPrefix() {
        return cardIdPrefix;
    }

    public void setCardIdPrefix(String cardIdPrefix) {
        this.cardIdPrefix = cardIdPrefix;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<CardLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<CardLabel> labels) {
        this.labels = labels;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                ", members=" + members +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Board other = (Board) obj;
        return other.id == id &&
                other.name.equals(name) &&
                other.owner.equals(owner);
    }
}
