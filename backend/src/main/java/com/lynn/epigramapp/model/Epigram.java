package com.lynn.epigramapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA entity representing an epigram stored in the system.
 * An epigram belongs to the user that posted it, and contains the quoted text, and
 * its attributed author
 */
@Entity
@Table(name = "epigrams") // Class name differs from table name
public class Epigram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String content;

    @Column
    private String author; // Perhaps an object for author

    @Column(nullable = false)
    private boolean mine;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

}
