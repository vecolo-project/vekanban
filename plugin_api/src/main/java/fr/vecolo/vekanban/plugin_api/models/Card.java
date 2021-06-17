package fr.vecolo.vekanban.plugin_api.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "CARD")
public class Card extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARD_ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "TITLE", updatable = true, nullable = false)
    private String title;

    @Column(name = "CONTENT", length = 2000, updatable = true, nullable = true)
    private String content;

    @Column(name = "DUE_DATE", updatable = true, nullable = true)
    private LocalDate dueDate;

    @ManyToOne(optional = false)
    private CardStatus status;

    @ManyToOne(optional = true)
    private User assignedUser;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board assignedBoard;

    @ManyToMany()
    private List<CardLabel> labels;

    public Card() {
    }

    public Card(String title, String content, CardStatus status, LocalDate dueDate) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public Board getAssignedBoard() {
        return assignedBoard;
    }

    public void setAssignedBoard(Board assignedBoard) {
        this.assignedBoard = assignedBoard;
    }

    public List<CardLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<CardLabel> labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", dueDate=" + dueDate +
                ", status=" + status +
                ", assignedUser=" + assignedUser +
                ", assignedBoard=" + assignedBoard +
                ", labels=" + labels +
                "} " + super.toString();
    }
}
