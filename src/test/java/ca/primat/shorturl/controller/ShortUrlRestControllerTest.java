package ca.primat.shorturl.controller;

import ca.primat.shorturl.exception.rest.InternalServerErrorException;
import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.service.ShortUrlService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class ShortUrlRestControllerTest {

    @MockBean
    private ShortUrlService shortUrlServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ShortUrlRestController(shortUrlServiceMock)).build();
    }

    @Test
    public void whenCreateOrFind_thenCreate() throws Exception {
        ShortUrl stubShortUrl = new ShortUrl("http://example.com");
        stubShortUrl.setSlug("test");

        Mockito.when(shortUrlServiceMock.create(anyString())).thenReturn(stubShortUrl);

        this.mockMvc.perform(
                    post("/api/v1/shorturl")
                            .contentType(APPLICATION_JSON)
                            .content("{\"url\":\"" + stubShortUrl.getUrl() + "\"}"))
                .andExpect(status().is(201))
                .andExpect(header().string("Location", "http://localhost" + ShortUrlRestController.ENDPOINT_PATH + "test" ))
                .andExpect(jsonPath("$.url", is(stubShortUrl.getUrl())))
                .andExpect(jsonPath("$.slug", is("test")))
                .andDo(print());

        verify(shortUrlServiceMock, times(1)).create(anyString());
    }

    @Test
    public void whenCreateOrFind_thenFind() throws Exception {
        ShortUrl stubShortUrl = new ShortUrl("http://example.com");
        stubShortUrl.setSlug("test");

        Mockito.when(shortUrlServiceMock.create(anyString())).thenThrow(new DataIntegrityViolationException(""));
        Mockito.when(shortUrlServiceMock.findByUrl(anyString())).thenReturn(stubShortUrl);

        this.mockMvc.perform(
                post("/api/v1/shorturl")
                        .contentType(APPLICATION_JSON)
                        .content("{\"url\":\"" + stubShortUrl.getUrl() + "\"}"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.url", is(stubShortUrl.getUrl())))
                .andExpect(jsonPath("$.slug", is("test")))
                .andDo(print());

        verify(shortUrlServiceMock, times(1)).create(anyString());
        verify(shortUrlServiceMock, times(1)).findByUrl(anyString());
    }

    @Test
    public void whenCreateOrFind_thenThrowInternalServerErrorException1() throws Exception {
        ShortUrl stubShortUrl = new ShortUrl("http://example.com");
        stubShortUrl.setSlug("test");

        Mockito.when(shortUrlServiceMock.create(anyString())).thenThrow(new DataIntegrityViolationException(""));
        Mockito.when(shortUrlServiceMock.findByUrl(anyString())).thenThrow(new InternalServerErrorException(""));

        this.mockMvc.perform(
                post("/api/v1/shorturl")
                        .contentType(APPLICATION_JSON)
                        .content("{\"url\":\"" + stubShortUrl.getUrl() + "\"}"))
                .andExpect(status().is(500))
                //.andExpect(jsonPath("$.message", anyString()))
                .andDo(print());
    }

    @Test
    public void whenCreateOrFind_thenThrowInternalServerErrorException2() throws Exception {
        ShortUrl stubShortUrl = new ShortUrl("http://example.com");
        stubShortUrl.setSlug("test");

        Mockito.when(shortUrlServiceMock.create(anyString())).thenReturn(null);

        this.mockMvc.perform(
                post("/api/v1/shorturl")
                        .contentType(APPLICATION_JSON)
                        .content("{\"url\":\"http://example.com\"}"))
                .andExpect(status().is(500))
                //.andExpect(jsonPath("$.message", ))
                .andDo(print());
    }
}
