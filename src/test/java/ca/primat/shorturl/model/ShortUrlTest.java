package ca.primat.shorturl.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ShortUrlTest {

    @Test
    public void testShortUrl() {
        ShortUrl shortUrl = new ShortUrl();

        assertThat(shortUrl.getId()).isNull();
        assertThat(shortUrl.getUrl()).isEqualTo("");
        assertThat(shortUrl.getSlug()).isEqualTo("");

        shortUrl.setId(10L);
        assertThat(shortUrl.getId()).isEqualTo(10L);

        shortUrl.setUrl("https://example.com");
        assertThat(shortUrl.getUrl()).isEqualTo("https://example.com");

        shortUrl.setSlug("a");
        assertThat(shortUrl.getSlug()).isEqualTo("a");
    }

    @Test
    public void testShortUrlConstructor() {
        ShortUrl shortUrl = new ShortUrl("https://example.com");
        assertThat(shortUrl.getUrl()).isEqualTo("https://example.com");
    }
}
