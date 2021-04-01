package fr.vecolo.vekanban.models;

import fr.vecolo.vekanban.utils.DateAudit;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "USER")
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "USER_EMAIL", unique = true, updatable = true, nullable = false)
    private String email;

    @Column(name = "USER_PSEUDO", unique = true, updatable = true, nullable = false)
    private String pseudo;

    @Column(name = "USER_PASSWORD", unique = true, updatable = true, nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner")
    private List<Board> owning;

    @ManyToMany(mappedBy = "members")
    private List<Board> boardsMember;

    @OneToMany(mappedBy = "assignedUser")
    private List<Card> Assignedcards;

    public User() {
    }

    public User(long id, String email, String pseudo, String password) {
        this.id = id;
        this.email = email;
        this.pseudo = pseudo;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String pseudo, String password) {
        this.email = email;
        this.pseudo = pseudo;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Board> getOwning() {
        return owning;
    }

    public List<Board> getBoardsMember() {
        return boardsMember;
    }

    public List<Card> getAssignedcards() {
        return Assignedcards;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", password='" + "X".repeat(password.length()) + '\'' +
                '}' + super.toString();
    }
}