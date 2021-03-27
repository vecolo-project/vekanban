package fr.vecolo.vekanban.models;

import javax.persistence.*;

@Entity
@Table(name = "CARD_LABEL")
public class CardLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARD_LABEL_ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "NAME", updatable = true, nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private Board board;
}
