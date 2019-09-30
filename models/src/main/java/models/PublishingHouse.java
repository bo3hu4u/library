<<<<<<< HEAD
package models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.base.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;

import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "publish_house")
@EntityListeners(AuditingEntityListener.class)
public class PublishingHouse extends BaseEntity<Long> {
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnoreProperties("publishingHouse")
    @OneToOne
    @JoinColumn(name = "locationId")
    private Location location;

    @JsonIgnoreProperties({"author", "publishingHouses"})
    @ManyToMany
    @JoinTable(name = "publhouse_book",
            joinColumns = @JoinColumn(name = "Id"),
            inverseJoinColumns = @JoinColumn(name = "bookId"))
    private Set<Book> books = new HashSet<>();


    public PublishingHouse() {
    }

    public PublishingHouse(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public PublishingHouse(Long Id, String name) {
        this.id = Id;
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
            return getId().equals(that.getId())
                    && getName().equals(that.getName());
        } else {
            return getId().equals(that.getId())
                    && getName().equals(that.getName())
                    && getLocation().equals(that.getLocation());
        }


    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLocation());
    }

    @Override
    public String toString() {
        return this.name;
    }
}
=======
package models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.base.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;

import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "publish_house")
@EntityListeners(AuditingEntityListener.class)
public class PublishingHouse extends BaseEntity<Long> {
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnoreProperties("publishingHouse")
    @OneToOne
    @JoinColumn(name = "locationId")
    private Location location;

    @JsonIgnoreProperties({"author", "publishingHouses"})
    @ManyToMany
    @JoinTable(name = "publhouse_book",
            joinColumns = @JoinColumn(name = "Id"),
            inverseJoinColumns = @JoinColumn(name = "bookId"))
    private Set<Book> books = new HashSet<>();


    public PublishingHouse() {
    }

    public PublishingHouse(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public PublishingHouse(Long Id, String name) {
        this.id = Id;
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
            return getId().equals(that.getId())
                    && getName().equals(that.getName());
        } else {
            return getId().equals(that.getId())
                    && getName().equals(that.getName())
                    && getLocation().equals(that.getLocation());
        }


    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLocation());
    }

    @Override
    public String toString() {
        return this.name;
    }
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
