package fr.vecolo.vekanban.models;

import fr.vecolo.vekanban.utils.DateAudit;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "CARD")
public class Card extends DateAudit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARD_ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "TITLE", updatable = true, nullable = false)
    private String title;

    @Column(name = "CONTENT", updatable = true, nullable = true)
    private String content;

    @Column(name = "STATUS", updatable = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

    @Column(name = "DUE_DATE", updatable = true, nullable = true)
    private LocalDateTime dueDate;

    @ManyToOne(optional = true)
    private User assignedUser;

    @ManyToOne(optional = false)
    private Board assignedBoard;

    @ManyToMany()
    private List<CardLabel> labels;
}
