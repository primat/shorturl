package ca.primat.shorturl.service;

import ca.primat.shorturl.model.dao.ShortUrlRepository;
import ca.primat.shorturl.model.ShortUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service layer for {@link ShortUrl}s
 */
@Service
public class ShortUrlService {

    private final Base62Service base62Service;
    private final ShortUrlRepository shortUrlRepository;

    @Autowired
    public ShortUrlService(Base62Service base62Service, ShortUrlRepository shortUrlRepository) {
        this.base62Service = base62Service;
        this.shortUrlRepository = shortUrlRepository;
    }

    /**
     * Create a new {@link ShortUrl} from an absolute URL
     * @param url The value to use to find an existing {@link ShortUrl} or to create a new one.
     * @return Returns a ResponseEntity with a {@link ShortUrl} and indicating if it was created or not
     */
    public ShortUrl create(String url) {
        ShortUrl shortUrl;

        shortUrl = shortUrlRepository.save(new ShortUrl(url));

        if (shortUrl != null) {
            shortUrl.setSlug(base62Service.encode(shortUrl.getId()));
        }

        return shortUrl;
    }

    /**
     * Get a {@link ShortUrl} by its slug
     * @param slug The slug of the {@link ShortUrl}
     * @return The {@link ShortUrl} object
     */
    public ShortUrl findBySlug(String slug) {
        long lookupId = base62Service.decode(slug);
        ShortUrl shortUrl = shortUrlRepository.findOne(lookupId);

        // Don't recalculate the slug if the query was successful
        if (shortUrl != null) {
            shortUrl.setSlug(slug);
        }

        return shortUrl;
    }

    /**
     * Finds a ShortUrl from a given URL
     * @param url The search criteria
     * @return The found {@link ShortUrl} or null if none was found
     */
    public ShortUrl findByUrl(String url) {
        ShortUrl shortUrl = shortUrlRepository.findByUrl(url);

        if (shortUrl != null) {
            shortUrl.setSlug(base62Service.encode(shortUrl.getId()));
        }

        return shortUrl;
    }

    /**
     * Validates a short URL slug
     * @param slug The string/slug to validate
     */
    boolean isValidSlug(String slug) {
        String pattern = "^[A-Za-z0-9]{1,10}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(slug);
        return m.find();
    }
}
