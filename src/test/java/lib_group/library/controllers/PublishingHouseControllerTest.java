package lib_group.library.controllers;

import lib_group.library.LibraryApplication;
import lib_group.library.LibraryApplicationTests;
import lib_group.library.exceptions.NotFoundEntityException;
import lib_group.library.models.Author;
import lib_group.library.models.Location;
import lib_group.library.models.Book;
import lib_group.library.models.PublishingHouse;
import lib_group.library.repositories.PublishingHouseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringJUnit4ClassRunner.class)
public class PublishingHouseControllerTest extends LibraryApplicationTests {
    @Autowired
    private PublishingHouseRepository publishingHouseRepository;

    @Test
    public void getAllPublishingHouses() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/publish_houses")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        List<PublishingHouse> publishingHouseFromJson = mapFromJson(json, List.class);
        String publishingHouses = mapToJson(publishingHouseRepository.findAll());
        assertEquals(5, publishingHouseFromJson.size());
        assertEquals(publishingHouses, json);
    }


    @Test
    public void getPublishingHouseByIdExists() throws Exception {
        PublishingHouse publishingHouseTest = new PublishingHouse(2L, "Publish2");
        Location locationTest = new Location(2L, "address2");
        publishingHouseTest.setLocation(locationTest);

        MvcResult mvcResult = this.mockMvc.perform(get("/publish_houses/2")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        PublishingHouse publishingHouseFromJson = mapFromJson(json, PublishingHouse.class);

        assertEquals(publishingHouseTest, publishingHouseFromJson);
    }

    @Test
    public void getPublishingHouseByIdNotExists() throws Exception {
        Long id = 133333L;
        MvcResult mvcResult = this.mockMvc.perform(get("/publish_houses/" + id)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andReturn();

        String contentTest = "Can't find publishing house with id 133333";
        int statusTest = 400;

        assertEquals(contentTest, mvcResult.getResponse().getContentAsString());
        assertEquals(statusTest, mvcResult.getResponse().getStatus());
    }

    @Test
    public void postPublishingHouseWithFreeLocation() throws Exception {
        PublishingHouse publishingHouseTest = new PublishingHouse(6L, "testPublish");
        Location locationTest = new Location(5L, "address5");
        publishingHouseTest.setLocation(locationTest);

        String json = "{\"name\":\"testPublish\",\n" +
                "\"location\":{\"address\":\"address5\"}}";
        MvcResult mvcResult = this.mockMvc.perform(post("/publish_houses")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        PublishingHouse publishingHouseFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), PublishingHouse.class);

        assertEquals(publishingHouseTest, publishingHouseFromJson);
    }

    @Test
    public void putPublishingHouseName() throws Exception {
        PublishingHouse publishingHouseTest = new PublishingHouse(2L, "Publish2Changed");
        Location locationTest = new Location(2L, "address2");
        publishingHouseTest.setLocation(locationTest);

        String json = "{\"name\":\"Publish2Changed\"}";
        MvcResult mvcResult = this.mockMvc.perform(put("/publish_houses/2")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        PublishingHouse publishingHouseFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), PublishingHouse.class);

        assertEquals(publishingHouseTest, publishingHouseFromJson);
    }

    @Test
    public void putPublishingHouseAddressExists() throws Exception {
        PublishingHouse publishingHouseTest = new PublishingHouse(2L, "Publish2");
        Location locationTest = new Location(5L, "address5");
        publishingHouseTest.setLocation(locationTest);

        String json = "{\"location\":{\"address\":\"address5\"}}";
        MvcResult mvcResult = this.mockMvc.perform(put("/publish_houses/2")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        PublishingHouse publishingHouseFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), PublishingHouse.class);
        assertEquals(publishingHouseTest, publishingHouseFromJson);
    }

    @Test
    public void putPublishingHouseBooksFlagUpd() throws Exception {
        PublishingHouse publishingHouseTest = new PublishingHouse(2L, "Publish2");
        Location locationTest = new Location(2L, "address2");
        Set<Book> books = new HashSet<>();
        books.add(new Book(1L, "book1", 2000, false));
        publishingHouseTest.setLocation(locationTest);
        publishingHouseTest.setBooks(books);

        String json = "{\"books\":[{\"title\":\"book1\"}]}";
        MvcResult mvcResult = this.mockMvc.perform(put("/publish_houses/2?booksFlag=upd")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        PublishingHouse publishingHouseFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), PublishingHouse.class);

        assertEquals(publishingHouseTest, publishingHouseFromJson);
    }

    @Test
    public void putPublishingHouseBooksFlagDel() throws Exception {
        PublishingHouse publishingHouseTest = new PublishingHouse(2L, "Publish2");
        Location locationTest = new Location(2L, "address2");
        Set<Book> books = new HashSet<>();
        books.add(new Book(3L, "book3", 2050, false));
        publishingHouseTest.setLocation(locationTest);
        publishingHouseTest.setBooks(books);

        String json = "{\"books\":[{\"title\":\"book1\"}]}";
        MvcResult mvcResult = this.mockMvc.perform(put("/publish_houses/2?booksFlag=del")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        PublishingHouse publishingHouseFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), PublishingHouse.class);

        assertEquals(publishingHouseTest, publishingHouseFromJson);
    }

    @Test
    public void putPublishingHouseBooksFlagAdd() throws Exception {
        PublishingHouse publishingHouseTest = new PublishingHouse(2L, "Publish2");
        Location locationTest = new Location(2L, "address2");
        Set<Book> books = new HashSet<>();
        books.add(new Book(2L, "book2", 2010, true));
        publishingHouseTest.setLocation(locationTest);
        publishingHouseTest.setBooks(books);

        String json = "{\"books\":[{\"title\":\"book2\"}]}";
        MvcResult mvcResult = this.mockMvc.perform(put("/publish_houses/2?booksFlag=add")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andReturn();
        PublishingHouse publishingHouseFromJson = mapFromJson(mvcResult.getResponse().getContentAsString(), PublishingHouse.class);

        assertEquals(publishingHouseTest, publishingHouseFromJson);
    }

    @Test
    public void deletePublishingHouseExists() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete("/publish_houses/2")).andReturn();

        int statusTest = 200;
        assertEquals(statusTest, mvcResult.getResponse().getStatus());

        PublishingHouse authorCheckExistence = publishingHouseRepository.findById(2L).orElse(null);
        assertEquals(null, authorCheckExistence);

        try {
            locationService.getById(2L);
        } catch (Exception exc) {
            assertEquals("Can't find location with id 2", exc.getMessage());
            assertTrue(exc instanceof NotFoundEntityException);
        }
    }

    @Test
    public void deletePublishingHouseNotExists() throws Exception {
        Long id = 133333L;
        MvcResult mvcResult = this.mockMvc.perform(delete("/publish_houses/" + id)).andReturn();
        String contentTest = "Can't find publishing house with id 133333";
        int statusTest = 400;

        assertEquals(contentTest, mvcResult.getResponse().getContentAsString());
        assertEquals(statusTest, mvcResult.getResponse().getStatus());
    }
}