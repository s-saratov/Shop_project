package de.aittr.g_52_shop.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

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

    /*
    Мы хотим, чтобы название продукта соответствовало требованиям:
    1. Не должно быть короче трёх символов;
    2. Не должно содержать цифры и спецсимволы;
    3. Первая буква должна быть в верхнем регистре;
    4. Остальные буквы должны быть в нижнем регистре.
     */

    @Column(name = "title")
    @NotNull(message = "Product title cannot be null")
    @NotBlank(message = "Product title cannot be empty")
    @Pattern(
            regexp = "[A-Z][a-z ]{2,}",
            message = "Product title should be at least three characters length and start with capital letter"
    )
    private String title;

    @Column(name = "price")
    @DecimalMin(
            value = "1.00",
            message = "Product price should be greater or equal than 1"
    )
    @DecimalMax(
            value = "1000.00",
            inclusive = false,
            message = "Product price should lesser than 1000"
    )
    private BigDecimal price;

    @Column(name = "active")
    private boolean active;

    @Column(name = "image")
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return active == product.active && Objects.equals(id, product.id) && Objects.equals(title, product.title) && Objects.equals(price, product.price) && Objects.equals(image, product.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, active, image);
    }

    @Override
    public String toString() {
        return String.format("Продукт: id - %d, наименование - %s, цена - %.2f, активен - %s.",
                id, title, price, active ? "да" : "нет");
    }
}
