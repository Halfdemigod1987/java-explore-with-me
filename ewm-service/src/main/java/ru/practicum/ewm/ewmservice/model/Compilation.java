package ru.practicum.ewm.ewmservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxyHelper;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(
            name = "events_compilations",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @ToString.Exclude
    private List<Event> eventsCompilations;
    private Boolean pinned;
    @NotBlank
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass()  != HibernateProxyHelper.getClassWithoutInitializingProxy(o)) return false;
        Compilation compilation = (Compilation) o;
        return Objects.equals(id, compilation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
