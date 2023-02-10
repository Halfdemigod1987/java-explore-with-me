package ru.practicum.ewm.ewmservice.model;

import lombok.*;
import org.hibernate.proxy.HibernateProxyHelper;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @NotBlank
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "initiator")
    @ToString.Exclude
    private Set<Event> events;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass()  != HibernateProxyHelper.getClassWithoutInitializingProxy(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
