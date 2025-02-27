package de.aittr.g_52_shop.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
/*
Аннотация @Entity сообщает Spring о том, что перед нами такая сущность, для которой существует таблица в БД.
И надо объекты этого класса сопоставлять с БД.
 */
@Table(name = "product")
public class Product {

    // Аннотация @Id указывает, что именно это поле является идентификатором
    @Id
    // Аннотация @GeneratedValue указывает, что генерацией идентификатора занимается сама БД
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    // Аннотация @Column указывает, в какой именно колонке таблицы лежат значения этого поля
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "active")
    private boolean active;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;
        return active == product.active && Objects.equals(id, product.id) && Objects.equals(title, product.title) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, active);
    }

    @Override
    public String toString() {
        return String.format("Продукт: id - %d, наименование - %s, цена - %.2f, активен - %s.",
                id, title, price, active ? "да" : "нет");
    }
}
