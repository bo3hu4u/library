package lib_group.library.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "location")
@EntityListeners(AuditingEntityListener.class)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @Column(name = "address", unique = true, nullable = false, length = 150)
    private String address;

    @Cascade({CascadeType.SAVE_UPDATE})
    @JsonIgnoreProperties({"location", "books"})
    @OneToOne(mappedBy = "location")
    private PublishingHouse publishingHouse;

    public Location() {
    }

    public Location(Long locationId, String address) {
        this.locationId = locationId;
        this.address = address;
    }

    public Location(String address) {
        this.address = address;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PublishingHouse getPublishingHouse() {
        return publishingHouse;
    }

    public void setPublishingHouse(PublishingHouse publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location1 = (Location) o;
        return getLocationId().equals(location1.getLocationId()) &&
                getAddress().equals(location1.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLocationId(), getAddress());
    }
}
