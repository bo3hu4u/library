package lib_group.library.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;

import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "publish_house")
@EntityListeners(AuditingEntityListener.class)
public class PublishingHouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publishHouseId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnoreProperties("publishingHouse")
    @OneToOne
    @JoinColumn(name = "locationId")
    private Location location;

    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE})
    @JsonIgnoreProperties({"author", "publishingHouses"})
    @ManyToMany
    @JoinTable(name = "publhouse_book",
            joinColumns = @JoinColumn(name = "publishHouseId"),
            inverseJoinColumns = @JoinColumn(name = "bookId"))
    private Set<Book> books = new HashSet<>();


    public PublishingHouse() {
    }

    public PublishingHouse(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public PublishingHouse(Long publishHouseId, String name) {
        this.publishHouseId = publishHouseId;
        this.name = name;
    }

    public PublishingHouse(String name) {
        this.name = name;
    }

    public PublishingHouse(String name, Set<Book> books) {
        this.name = name;
        this.books = books;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getPublishHouseId() {
        return publishHouseId;
    }

    public void setPublishHouseId(Long publishHouseId) {
        this.publishHouseId = publishHouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublishingHouse)) return false;
        PublishingHouse that = (PublishingHouse) o;
        if (getLocation() == null && that.getLocation() != null) return false;
        if (getLocation() != null && that.getLocation() == null) return false;
        if (getLocation() == null && that.getLocation() == null) {
            return getPublishHouseId().equals(that.getPublishHouseId())
                    && getName().equals(that.getName());
        } else {
            return getPublishHouseId().equals(that.getPublishHouseId())
                    && getName().equals(that.getName())
                    && getLocation().equals(that.getLocation());
        }


    }

    @Override
    public int hashCode() {
        return Objects.hash(getPublishHouseId(), getName(), getLocation());
    }

    @Override
    public String toString() {
        return this.name;
    }
}
