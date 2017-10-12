package ca.primat.shorturl.service;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShortUrlServiceTest {

    @Autowired
    private ShortUrlService shortUrlService;

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
}
