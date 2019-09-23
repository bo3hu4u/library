package lib_group.library.models;

import com.fasterxml.jackson.annotation.*;
import lib_group.library.models.base.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "book")
@EntityListeners(AuditingEntityListener.class)
public class Book extends BaseEntity<Long> {

    @Column(name = "title", nullable = false, unique = true, length = 50)
    private String title;

    @JsonIgnoreProperties("books")
    @ManyToOne
    @JoinColumn(name = "authorId")
    private Author author;

    private transient Description description;

    @JsonIgnoreProperties("books")
    @ManyToMany(mappedBy = "books")
    private Set<PublishingHouse> publishingHouses = new HashSet<>();

    @Column(name = "edition_year", nullable = false)
    private Integer editionYear;

    @Column(name = "book_on_hands", nullable = false)
    private Boolean bookOnHands;

    public Book() {
    }

    public Book(String title) {

        this.title = title;
    }

    public Book(Long Id, String title, Integer editionYear, boolean bookOnHands) {
        this.id = Id;
        this.title = title;
        this.editionYear = editionYear;
        this.bookOnHands = bookOnHands;
    }

    public Book(String title, Integer editionYear, boolean bookOnHands) {
        this.title = title;
        this.editionYear = editionYear;
        this.bookOnHands = bookOnHands;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Set<PublishingHouse> getPublishingHouses() {
        return publishingHouses;
    }

    public void setPublishingHouses(Set<PublishingHouse> publishingHouses) {
        this.publishingHouses = publishingHouses;
    }

    public Integer getEditionYear() {
        return editionYear;
    }

    public void setEditionYear(Integer editionYear) {
        this.editionYear = editionYear;
    }

    public Boolean isBookOnHands() {
        return bookOnHands;
    }

    public void setBookOnHands(Boolean bookOnHands) {
        this.bookOnHands = bookOnHands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return getId().equals(book.getId())
                && getTitle().equals(book.getTitle())
                && getEditionYear().equals(book.getEditionYear())
                && isBookOnHands().equals(book.isBookOnHands());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getEditionYear(), isBookOnHands());
    }

    @Override
    public String toString() {
        return this.title;
    }
}
