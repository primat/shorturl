package ca.primat.shorturl.service;

import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.model.dao.ShortUrlRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
public class ShortUrlServiceTest {

    private ShortUrlService shortUrlService;
    private Base62Service base62ServiceMock;
    private ShortUrlRepository shortUrlRepositoryMock;

    // Setup the mocks, dependencies and request context
    @Before
    public void setUp() {
        base62ServiceMock = Mockito.mock(Base62Service.class);
        shortUrlRepositoryMock = Mockito.mock(ShortUrlRepository.class);
        shortUrlService = new ShortUrlService(base62ServiceMock, shortUrlRepositoryMock);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testIsValidSlug() {
        assertThat(shortUrlService.isValidSlug("")).isFalse(); // Less than 1 character
        assertThat(shortUrlService.isValidSlug("0")).isTrue();
        assertThat(shortUrlService.isValidSlug("1")).isTrue();
        assertThat(shortUrlService.isValidSlug("ZZZZZZZZZZ")).isTrue();
        assertThat(shortUrlService.isValidSlug("1000000000")).isTrue();
        assertThat(shortUrlService.isValidSlug("ZZZZ-ZZZZZ")).isFalse(); // Invalid character
        assertThat(shortUrlService.isValidSlug("*ZZZZZZZZZ")).isFalse(); // Invalid character
        assertThat(shortUrlService.isValidSlug("12345678901")).isFalse(); // More than 10 characters
    }

    @Test
    public void testAssignSlug() {
        ShortUrl shortUrl = new ShortUrl();

        // Test that the slug doesn't get overwritten
        shortUrl.setSlug("aaa");
        Mockito.when(base62ServiceMock.encode(99L)).thenReturn("zZZ");
        shortUrlService.assignSlug(shortUrl);
        assertThat(shortUrl.getSlug()).isEqualTo("aaa");

        // Test that the slug gets overwritten
        shortUrl.setId(10L);
        Mockito.when(base62ServiceMock.encode(10L)).thenReturn("z");
        shortUrlService.assignSlug(shortUrl);
        assertThat(shortUrl.getSlug()).isEqualTo("z");
    }

    @Test
    public void testGetOrCreate() {
        ShortUrl shortUrl = new ShortUrl("http://example.com");
        shortUrl.setId(10L);

        // Test HttpStatus.CREATED (ShortUrl was created)
        Mockito.when(shortUrlRepositoryMock.findByUrl(shortUrl.getUrl())).thenReturn(null);
        Mockito.when(shortUrlRepositoryMock.save(any(ShortUrl.class))).thenReturn(null);
        ResponseEntity<ShortUrl> re = shortUrlService.getOrCreate(shortUrl);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Test HttpStatus.OK (ShortUrl already exists)
        Mockito.when(shortUrlRepositoryMock.findByUrl(shortUrl.getUrl())).thenReturn(shortUrl);
        re = shortUrlService.getOrCreate(shortUrl);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
