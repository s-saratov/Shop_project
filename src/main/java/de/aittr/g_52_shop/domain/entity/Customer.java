package de.aittr.g_52_shop.domain.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Customer {

    private Long id;
    private String name;
    private boolean active;
    // TODO: добавить конструктор объекта класса корзины

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Customer customer)) return false;
        return active == customer.active && Objects.equals(id, customer.id) && Objects.equals(name, customer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, active);
    }

    @Override
    public String toString() {
        return String.format("Покупатель: id - %d, имя - %s, активен - %s.",
                id, name, active ? "да" : "нет");
    }
}
