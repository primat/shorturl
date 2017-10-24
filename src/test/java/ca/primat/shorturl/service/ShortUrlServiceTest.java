package ca.primat.shorturl.service;

import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.model.dao.ShortUrlRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

@RunWith(SpringRunner.class)
public class ShortUrlServiceTest {

    private ShortUrlService shortUrlService;
    private Base62Service base62ServiceMock;
    private ShortUrlRepository shortUrlRepositoryMock;

    @Before
    public void setUp() {
        base62ServiceMock = Mockito.mock(Base62Service.class);
        shortUrlRepositoryMock = Mockito.mock(ShortUrlRepository.class);
        shortUrlService = new ShortUrlService(base62ServiceMock, shortUrlRepositoryMock);

        // Mock a servlet to test the shortened URL
        //MockHttpServletRequest request = new MockHttpServletRequest();
        //RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
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
    public void whenCreate_ThenReturnNewShortUrl() {
        ShortUrl shortUrlInput = new ShortUrl("https://example.com");
        shortUrlInput.setId(10L);
        Mockito.when(shortUrlRepositoryMock.save(any(ShortUrl.class))).thenReturn(shortUrlInput);
        Mockito.when(base62ServiceMock.encode(anyLong())).thenReturn("aaa");

        ShortUrl shortUrl = shortUrlService.create("https:example.com");

        assertThat(shortUrl.getUrl()).isEqualTo("https://example.com");
        assertThat(shortUrl.getSlug()).isEqualTo("aaa");
    }

    @Test
    public void whenCreate_ThenReturnNull() {
        ShortUrl shortUrlInput = new ShortUrl("https://example.com");
        shortUrlInput.setId(10L);
        Mockito.when(shortUrlRepositoryMock.save(any(ShortUrl.class))).thenReturn(null);
        Mockito.when(base62ServiceMock.encode(anyLong())).thenReturn("aaa");

        ShortUrl shortUrl = shortUrlService.create("https://example.com");

        assertThat(shortUrl).isNull();
    }

    @Test(expected=DataIntegrityViolationException.class)
    public void whenCreate_ThenThrowDataIntegrityException() {
        Mockito.when(shortUrlRepositoryMock.save(any(ShortUrl.class)))
                .thenThrow(new DataIntegrityViolationException("test"));
        shortUrlService.create("https://example.com");
    }

    @Test
    public void whenFindBySlug_ThenReturnShortUrl() {
        // Return anything, it doesn't matter since we're mocking the next call to the repo
        Mockito.when(base62ServiceMock.decode(anyString())).thenReturn(10L);
        Mockito.when(shortUrlRepositoryMock.findOne(anyLong())).thenReturn(new ShortUrl("https://example.com"));
        ShortUrl shortUrl = shortUrlService.findBySlug("anySlug");
        assertThat(shortUrl.getSlug()).isEqualTo("anySlug");
    }

    @Test
    public void whenFindBySlug_ThenReturnNull() {
        Mockito.when(base62ServiceMock.decode(anyString())).thenReturn(10L);
        Mockito.when(shortUrlRepositoryMock.findOne(anyLong())).thenReturn(null);

        ShortUrl shortUrl = shortUrlService.findBySlug("anySlug");

        assertThat(shortUrl).isNull();
    }

    @Test
    public void whenFindByUrl_ThenReturnShortUrl() {
        ShortUrl shortUrlInput = new ShortUrl("https://example.com");
        shortUrlInput.setId(10L);
        Mockito.when(base62ServiceMock.encode(anyLong())).thenReturn("aaa");
        Mockito.when(shortUrlRepositoryMock.findByUrl(anyString())).thenReturn(shortUrlInput);

        ShortUrl shortUrl = shortUrlService.findByUrl("https://example.com");

        //verify(shortUrl.setSlug("aaa"));
        assertThat(shortUrl.getUrl()).isEqualTo("https://example.com");
        assertThat(shortUrl.getSlug()).isEqualTo("aaa");
    }

    @Test
    public void whenFindByUrl_ThenReturnNull() {
        Mockito.when(shortUrlRepositoryMock.findByUrl(anyString())).thenReturn(null);

        ShortUrl shortUrl = shortUrlService.findByUrl("https://example.com");

        assertThat(shortUrl).isNull();
    }
}
