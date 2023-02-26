package ru.practicum.ewm.ewmservice.model;

import lombok.*;
import org.hibernate.proxy.HibernateProxyHelper;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ToString.Exclude
    private Category category;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Embedded
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
    private Integer views;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    @ToString.Exclude
    private User initiator;
    @Column(name = "created_date")
    private LocalDateTime createdOn = LocalDateTime.now();
    @Column(name = "published_date")
    private LocalDateTime publishedOn;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @ManyToMany(mappedBy = "eventsCompilations")
    @ToString.Exclude
    private List<Compilation> compilations;
    @OneToMany(mappedBy = "event")
    @ToString.Exclude
    private Set<Request> requests;
    @OneToMany(mappedBy = "event")
    @ToString.Exclude
    private Set<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass()  != HibernateProxyHelper.getClassWithoutInitializingProxy(o)) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
