package lib_group.library.models;


import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "description")
public class Description {

    @Id
    private String id;
    private String bookTitle;
    private String bookDescription;

    public Description(String bookTitle, String bookDescription) {
        this.bookTitle = bookTitle;
        this.bookDescription = bookDescription;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    @Override
    public String toString() {
        return "_id:" + this.id + " bookTitle:" + bookTitle + " description:'" + bookDescription + "'";
    }
}
