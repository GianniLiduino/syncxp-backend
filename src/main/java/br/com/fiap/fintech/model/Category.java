package br.com.fiap.fintech.model;

import br.com.fiap.fintech.values.CategoryType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "TB_CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type")
    private CategoryType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Category() {}

    public Category(String name, CategoryType type, User user) {
        this.name = name;
        this.type = type;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
