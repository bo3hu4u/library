package lib_group.library.controllers;

import lib_group.library.LibraryApplication;
import lib_group.library.LibraryApplicationTests;
import lib_group.library.models.Author;
import lib_group.library.models.Book;
import lib_group.library.models.PublishingHouse;
import lib_group.library.repositories.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class BookControllerTest extends LibraryApplicationTests {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void getAllBooks() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/books")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        List<Book> booksFromJson = mapFromJson(json, List.class);
        String books = mapToJson(bookService.getAll());
        assertEquals(4, booksFromJson.size());
        assertEquals(books, json);

    }

    @Test
    public void getBookByIdExists() throws Exception {
        Book bookTest = new Book(2L, "book2", 2010, true);

        MvcResult mvcResult = this.mockMvc.perform(get("/books/2")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        Book bookFromJson = mapFromJson(json, Book.class);

        assertEquals(bookTest, bookFromJson);
    }

    @Test
    public void getBookByIdNotExists() throws Exception {
        Long id = 133333L;
        MvcResult mvcResult = this.mockMvc.perform(get("/books/" + id)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andReturn();
        String contentTest = "Can't find book with id 133333";
        int statusTest = 400;

        assertEquals(contentTest, mvcResult.getResponse().getContentAsString());
        assertEquals(statusTest, mvcResult.getResponse().getStatus());
    }

    @Test
    public void postBookBasicAndAuthor() throws Exception {
        Book bookTest = new Book(5L, "testBook1", 1666, true);
        Author authorTest = new Author(1L, "author1", 111);
        bookTest.setAuthor(authorTest);

        String json = "{\"title\":\"testBook1\",\n" +
                "\"editionYear\":1666,\n" +
                "\"author\":{\"name\":\"author1\"},\n" +
                "\"bookOnHands\":true}";
        MvcResult mvcResult = this.mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        Book bookFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), Book.class);

        assertEquals(bookTest.getAuthor().getAuthorId(), bookFromJson.getAuthor().getAuthorId());
        assertEquals(bookTest, bookFromJson);
    }

    @Test
    public void postBookAuthorNull() throws Exception {
        Book bookTest = new Book(5L, "testBook1", 1666, true);
        bookTest.setAuthor(null);

        String json = "{\"title\":\"testBook1\",\n" +
                "\"editionYear\":1666,\n" +
                "\"author\":null,\n" +
                "\"bookOnHands\":true}";
        MvcResult mvcResult = this.mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        Book bookFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), Book.class);

        assertEquals(null, bookFromJson.getAuthor());
        assertEquals(bookTest, bookFromJson);
    }


    @Test
    public void postBookBasicAndAuthorNotExists() throws Exception {
        String json = "{\"title\":\"testBook1\",\n" +
                "\"editionYear\":1666,\n" +
                "\"author\":{\"name\":\"authorNotExists\"},\n" +
                "\"bookOnHands\":true}";
        MvcResult mvcResult = this.mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();

        String contentTest = "Author with a name 'authorNotExists' not found";
        int statusTest = 400;

        assertEquals(contentTest, mvcResult.getResponse().getContentAsString());
        assertEquals(statusTest, mvcResult.getResponse().getStatus());
    }

    @Test
    public void postBookBasicAndPublishingHouses() throws Exception {
        Book bookTest = new Book(5L, "testBook1", 1666, true);
        PublishingHouse publishingHouseTest = new PublishingHouse(2L, "Publish2");
        bookTest.getPublishingHouses().add(publishingHouseTest);

        String json = "{\"title\":\"testBook1\",\n" +
                "\"editionYear\":1666,\n" +
                "\"publishingHouses\":[{\"name\":\"Publish2\"}]," +
                "\"bookOnHands\":true}";
        MvcResult mvcResult = this.mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        Book bookFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), Book.class);
        PublishingHouse publishingHouseFromJson = bookFromJson.getPublishingHouses().stream().collect(Collectors.toList()).get(0);

        assertEquals(publishingHouseTest.getPublishHouseId(), publishingHouseFromJson.getPublishHouseId());
        assertEquals(publishingHouseTest.getName(), publishingHouseFromJson.getName());
        assertEquals(bookTest, bookFromJson);
    }


    @Test
    public void putBookBasicFields() throws Exception {
        Book bookTest = new Book(2L, "book2changed", 1020, false);

        String json = "{\"title\":\"book2changed\",\n" +
                "\"editionYear\":1020,\n" +
                "\"bookOnHands\":false}";
        MvcResult mvcResult = this.mockMvc.perform(put("/books/2").contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        Book bookFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), Book.class);

        assertEquals(bookTest, bookFromJson);
    }

    @Test
    public void putBookAuthor() throws Exception {
        Book bookTest = new Book(2L, "book2", 2010, true);
        Author authorTest = new Author(4L, "author4", 444);
        bookTest.setAuthor(authorTest);

        String json = "{\"author\":{\"name\":\"author4\"}}";
        MvcResult mvcResult = this.mockMvc.perform(put("/books/2").contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        Book bookFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), Book.class);

        assertEquals(bookTest.getAuthor().getAuthorId(), bookFromJson.getAuthor().getAuthorId());
        assertEquals(bookTest, bookFromJson);
    }


    @Test
    public void putBookAuthorNull() throws Exception {
        Book bookTest = new Book(2L, "book2", 2010, true);
        bookTest.setAuthor(null);

        String json = "{\"author\":null}";
        MvcResult mvcResult = this.mockMvc.perform(put("/books/2").contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        Book bookFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), Book.class);

        assertEquals(bookTest.getAuthor(), bookFromJson.getAuthor());
        assertEquals(bookTest, bookFromJson);
    }

    @Test
    public void putBookPublishingHouses() throws Exception {
        Book bookTest = new Book(2L, "book2", 2010, true);
        Author authorTest = new Author(4L, "author4", 444);
        bookTest.setAuthor(authorTest);


        PublishingHouse publishingHouseTest = new PublishingHouse(2L, "Publish2");
        bookTest.getPublishingHouses().add(publishingHouseTest);

        String json = "{\"publishingHouses\":[{\"name\":\"Publish2\"}]}";

        MvcResult mvcResult = this.mockMvc.perform(put("/books/2")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        Book bookFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), Book.class);
        PublishingHouse publishingHouseFromJson = bookFromJson.getPublishingHouses().stream().collect(Collectors.toList()).get(0);

        assertEquals(publishingHouseTest.getPublishHouseId(), publishingHouseFromJson.getPublishHouseId());
        assertEquals(publishingHouseTest.getName(), publishingHouseFromJson.getName());
        assertEquals(bookTest, bookFromJson);

    }

    @Test
    public void deleteBookByIdExists() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete("/books/2")).andReturn();

        int statusTest = 200;
        assertEquals(statusTest, mvcResult.getResponse().getStatus());

        Book bookCheckExistence = bookRepository.findById(2L).orElse(null);
        assertEquals(null, bookCheckExistence);
    }

    @Test
    public void deleteBookByIdNotExists() throws Exception {
        Long id = 133333L;
        MvcResult mvcResult = this.mockMvc.perform(delete("/books/" + id)).andReturn();
        String contentTest = "Can't find book with id 133333";
        int statusTest = 400;

        assertEquals(contentTest, mvcResult.getResponse().getContentAsString());
        assertEquals(statusTest, mvcResult.getResponse().getStatus());
    }

}