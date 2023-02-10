package ru.practicum.ewm.ewmservice.model;

import lombok.*;
import org.hibernate.proxy.HibernateProxyHelper;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location {
    Double lat;
    Double lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass()  != HibernateProxyHelper.getClassWithoutInitializingProxy(o)) return false;
        Location location = (Location) o;
        return Objects.equals(lat, location.getLat()) && Objects.equals(lon, location.getLon());
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}
