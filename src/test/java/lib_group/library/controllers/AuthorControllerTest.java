package lib_group.library.controllers;

import lib_group.library.LibraryApplicationTests;
import lib_group.library.models.Author;
import lib_group.library.models.Book;
import lib_group.library.repositories.AuthorRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuthorControllerTest extends LibraryApplicationTests {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void getAllAuthors() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/authors")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        List<Author> authorsFromJson = mapFromJson(json, List.class);
        String authors = mapToJson(authorService.getAll());
        assertEquals(4, authorsFromJson.size());
        assertEquals(json, authors);

    }

    @Test
    public void getAuthorByIdExists() throws Exception {
        Set<Book> books = new HashSet<>();
        books.add(new Book(1L, "book1", 2000, false));
        books.add(new Book(2L, "book2", 2010, true));
        Author authorTest = new Author(1L, "author1", 111);
        authorTest.setBooks(books);

        MvcResult mvcResult = this.mockMvc.perform(get("/authors/1")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        Author authorFromJson = mapFromJson(json, Author.class);

        assertEquals(authorTest, authorFromJson);
    }

    @Test
    public void getAuthorByIdNotExists() throws Exception {
        Long id = 133333L;
        MvcResult mvcResult = this.mockMvc.perform(get("/authors/" + id)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andReturn();

        String contentTest = "Can't find author with id 133333";
        int statusTest = 400;

        assertEquals(contentTest, mvcResult.getResponse().getContentAsString());
        assertEquals(statusTest, mvcResult.getResponse().getStatus());
    }

    @Test
    public void postAuthorWithoutBooks() throws Exception {
        Author authorTest = new Author(5L, "author22", 1000);

        String json = "{\"name\":\"author22\",\n" +
                "\"birthYear\":1000,\"books\":[]}";
        MvcResult mvcResult = this.mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        Author author = mapFromJson(mvcResult.getResponse().getContentAsString(), Author.class);

        Assert.assertEquals(authorTest, author);
    }

    @Test
    public void postAuthorWithNullBooks() throws Exception {
        Author authorTest = new Author(5L, "author22", 1000);

        String json = "{\"name\":\"author22\",\n" +
                "\"birthYear\":1000,\"books\":null}";
        MvcResult mvcResult = this.mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        Author author = mapFromJson(mvcResult.getResponse().getContentAsString(), Author.class);

        Assert.assertEquals(authorTest, author);
    }

    @Test
    public void postAuthorFull() throws Exception {
        Set<Book> books = new HashSet<>();
        books.add(new Book(1L, "book1", 2000, false));
        books.add(new Book(2L, "book2", 2010, true));
        Author authorTest = new Author(5L, "author22", 1234);
        authorTest.setBooks(books);

        String json = "{\"name\":\"author22\",\n" +
                "\"birthYear\":1234,\n" +
                "\"books\":[{\"title\":\"book1\"},{\"title\":\"book2\"}]}";
        MvcResult mvcResult = this.mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        Author author = mapFromJson(mvcResult.getResponse().getContentAsString(), Author.class);

        Assert.assertEquals(authorTest, author);
    }

    @Test
    public void postAuthorBooksNotExists() throws Exception {
        String json = "{\"name\":\"author22\",\n" +
                "\"birthYear\":1234,\n" +
                "\"books\":[{\"title\":\"bookNo\"},{\"title\":\"IncorrectBook\"}]}";
        MvcResult mvcResult = this.mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();

        String contentTest = "These books not found: [bookNo, IncorrectBook]";
        int statusTest = 400;

        Assert.assertEquals(contentTest, mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(statusTest, mvcResult.getResponse().getStatus());
    }

    @Test
    public void postAuthorNameNull() throws Exception {
        String json = "{\"name\":null,\n" +
                "\"birthYear\":666}";
        MvcResult mvcResult = this.mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();

        String contentTest = "Column 'name' cannot be null";
        int statusTest = 400;

        assertEquals(contentTest, mvcResult.getResponse().getContentAsString());
        assertEquals(statusTest, mvcResult.getResponse().getStatus());
    }


    @Test
    public void putAuthorName() throws Exception {
        Author authorTest = new Author(3L, "author3changed", 333);
        MvcResult mvcResult = this.mockMvc.perform(put("/authors/3?name=author3changed")).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        Author authorFromJson = mapFromJson(json, Author.class);

        assertEquals(authorTest.getId(), authorFromJson.getId());
        assertEquals(authorTest.getName(), authorFromJson.getName());
    }

    @Test
    public void putAuthorBirthYear() throws Exception {
        Author authorTest = new Author(3L, "author3", 555);

        MvcResult mvcResult = this.mockMvc.perform(put("/authors/3?birthYear=555")).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        Author authorFromJson = mapFromJson(json, Author.class);

        assertEquals(authorTest.getId(), authorFromJson.getId());
        assertEquals(authorTest.getBirthYear(), authorFromJson.getBirthYear());
    }

    @Test
    public void putAuthorBirthYearIncorrect() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(put("/authors/3?birthYear=string")).andReturn();
        String contentTest = "'string' is incorrect value for 'birthYear'. It should be Integer";
        int statusTest = 400;

        Assert.assertEquals(contentTest, mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(statusTest, mvcResult.getResponse().getStatus());
    }

    @Test
    public void putAuthorBooks() throws Exception {
        Set<Book> books = new HashSet<>();
        books.add(new Book(1L, "book1", 2000, false));
        books.add(new Book(2L, "book2", 2010, true));
        books.add(new Book(4L, "book4", 2070, true));
        Author authorTest = new Author(3L, "author3", 333);
        authorTest.setBooks(books);

        MvcResult mvcResult = this.mockMvc.perform(put("/authors/3?books=book1&books=book2")).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        Author authorFromJson = mapFromJson(json, Author.class);

        Assert.assertEquals(authorTest, authorFromJson);
    }

    @Test
    public void putAuthorBooksNotExists() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(put("/authors/3?books=pango&books=book44")).andReturn();

        String contentTest = "These books not found: [pango, book44]";
        int statusTest = 400;

        Assert.assertEquals(contentTest, mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(statusTest, mvcResult.getResponse().getStatus());
    }

    @Test
    public void deleteAuthorByIdExists() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete("/authors/2")).andReturn();

        int statusTest = 200;
        Assert.assertEquals(statusTest, mvcResult.getResponse().getStatus());

        Author authorCheckExistence = authorRepository.findById(2L).orElse(null);
        assertEquals(null, authorCheckExistence);
    }

    @Test
    public void deleteAuthorByIdNotExists() throws Exception {
        Long id = 133333L;
        MvcResult mvcResult = this.mockMvc.perform(delete("/authors/" + id)).andReturn();
        String contentTest = "Can't find author with id 133333";
        int statusTest = 400;

        assertEquals(contentTest, mvcResult.getResponse().getContentAsString());
        assertEquals(statusTest, mvcResult.getResponse().getStatus());
    }
}
