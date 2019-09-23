package lib_group.library.models;


import lib_group.library.models.base.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "description")
public class Description extends BaseEntity<String> {

    private Long bookId;
    private String bookDescription;

    public Description() {
    }

    public Description(Long bookId, String bookDescription) {
        this.bookId = bookId;
        this.bookDescription = bookDescription;
    }

    public Description(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    @Override
    public String toString() {
        return "_id:" + id + " bookId:" + bookId + " description:'" + bookDescription + "'";
    }
}
