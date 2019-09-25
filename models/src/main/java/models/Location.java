package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.base.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "location")
@EntityListeners(AuditingEntityListener.class)
public class Location extends BaseEntity<Long> {
    @Column(name = "address", unique = true, nullable = false, length = 150)
    private String address;

    @Cascade({CascadeType.SAVE_UPDATE})
    @JsonIgnoreProperties({"location", "books"})
    @OneToOne(mappedBy = "location")
    private PublishingHouse publishingHouse;

    public Location() {
    }

    public Location(Long Id, String address) {
        this.id = Id;
        this.address = address;
    }

    public Location(String address) {
        this.address = address;
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
        return getId().equals(location1.getId()) &&
                getAddress().equals(location1.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAddress());
    }
}
