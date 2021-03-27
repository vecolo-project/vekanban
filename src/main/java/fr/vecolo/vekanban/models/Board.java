package fr.vecolo.vekanban.models;

import fr.vecolo.vekanban.utils.DateAudit;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "BOARD")
public class Board extends DateAudit implements Serializable {
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "PROJECT_NAME", updatable = true, nullable = false)
    private String name;

    @Column(name = "CARD_ID_PREFIX", updatable = false, nullable = false)
    private String cardIdPrefix;

    @ManyToOne()
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