package ca.primat.shorturl.model.dao;

import ca.primat.shorturl.model.ShortUrl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShortUrlRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Test
    public void whenFindByUrl_thenReturnShortUrl() {

        ShortUrl shortUrl = new ShortUrl("https://example.com");
        entityManager.persist(shortUrl);
        entityManager.flush();

        // when
        ShortUrl found = shortUrlRepository.findByUrl(shortUrl.getUrl());

        // then
        assertThat(found.getUrl()).isEqualTo(shortUrl.getUrl());
        assertThat(found.getId() > 0L).isTrue();
    }
}
