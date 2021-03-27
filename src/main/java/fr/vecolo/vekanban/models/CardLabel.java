package fr.vecolo.vekanban.models;

import fr.vecolo.vekanban.utils.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "CARD_LABEL")
public class CardLabel extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARD_LABEL_ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "NAME", updatable = true, nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private Board board;

    public CardLabel() {
    }

    public CardLabel(String name, Board board) {
        this.name = name;
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
}
