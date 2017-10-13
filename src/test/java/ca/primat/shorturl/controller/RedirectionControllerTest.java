package ca.primat.shorturl.controller;

import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.service.ShortUrlService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class RedirectionControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ShortUrlService shortUrlService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RedirectionController(shortUrlService)).build();
    }

    @Test
    public void testRedirect() throws Exception{

        // Test when a record is returned
        ShortUrl stubShortUrl = new ShortUrl("http://example.com");
        Mockito.when(shortUrlService.getBySlug(anyString())).thenReturn(stubShortUrl);
        this.mockMvc.perform(get("/a"))
                .andExpect(status().is(303))
                .andExpect(header().string("Location",stubShortUrl.getUrl() ))
                .andDo(print());

        // Test when no record is returned
        Mockito.when(shortUrlService.getBySlug(anyString())).thenReturn(null);
        this.mockMvc.perform(get("/a"))
                .andExpect(status().isNotFound())
                .andDo(print());

        // Test when we check for slugs outside the range
        this.mockMvc.perform(get("/1aaaaaaaaaa"))
                .andExpect(status().isNotFound())
                .andDo(print());

        // Test when we check for slugs outside the range
        this.mockMvc.perform(get("/1a-aaa"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
