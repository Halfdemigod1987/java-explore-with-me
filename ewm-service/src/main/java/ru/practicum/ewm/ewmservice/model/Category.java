package ru.practicum.ewm.ewmservice.model;

import lombok.*;
import org.hibernate.proxy.HibernateProxyHelper;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private Set<Event> events;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass()  != HibernateProxyHelper.getClassWithoutInitializingProxy(o)) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
