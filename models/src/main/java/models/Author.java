package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.base.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "author")
@EntityListeners(AuditingEntityListener.class)
public class Author extends BaseEntity<Long> {

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "birth_year", nullable = false)
    private Integer birthYear;

    @JsonIgnoreProperties({"author", "publishingHouses"})
    @OneToMany(mappedBy = "author")
    private Set<Book> books = new HashSet<>();


    public Author() {
    }

    public Author(Long Id, String name, Integer birthYear) {
        this.id = Id;
        this.name = name;
        this.birthYear = birthYear;
    }

    public Author(String name, Integer birthYear) {

        this.name = name;
        this.birthYear = birthYear;
    }

    public Author(String name, Integer birthYear, Set<Book> books) {
        this.name = name;
        this.birthYear = birthYear;
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
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
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return getId().equals(author.getId())
                && getName().equals(author.getName())
                && getBirthYear().equals(author.getBirthYear())
                && getBooks().equals(author.getBooks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getBirthYear(), getBooks());
    }

}
