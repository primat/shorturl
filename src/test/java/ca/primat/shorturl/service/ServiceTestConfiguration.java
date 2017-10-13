package ca.primat.shorturl.service;

import ca.primat.shorturl.model.dao.ShortUrlRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class ServiceTestConfiguration {
    @Bean
    @Primary
    public Base62Service base62Service() {
        return Mockito.mock(Base62Service.class);
    }

    @Bean
    @Primary
    public ShortUrlRepository ShortUrlRepository() {
        return Mockito.mock(ShortUrlRepository.class);
    }
}
