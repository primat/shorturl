package ca.primat.shorturl.service;

import ca.primat.shorturl.model.dao.ShortUrlRepository;
import ca.primat.shorturl.model.ShortUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
     * Calculates a {@link ShortUrl}'s slug and assigns it, assuming it has a non-zero ID
     * @param shortUrl the {@link ShortUrl} to assign its slug
     */
    void assignSlug(ShortUrl shortUrl) {
        if (shortUrl != null && shortUrl.getId() >= 0) {
            shortUrl.setSlug(base62Service.encode(shortUrl.getId()));
        }
    }

    /**
     * Get a {@link ShortUrl} by its slug
     * @param slug The slug of the {@link ShortUrl}
     * @return The {@link ShortUrl} object
     */
    public ShortUrl getBySlug(String slug) {
        long lookupId = base62Service.decode(slug);
        ShortUrl shortUrl = shortUrlRepository.findOne(lookupId);
        this.assignSlug(shortUrl);
        return shortUrl;
    }

    /**
     * Get an existing {@link ShortUrl}, or create a new one
     * @param newShortUrl The input {@link ShortUrl} to get or create
     * @return Returns an existing or new {@link ShortUrl}.
     */
    @Transactional
    public ResponseEntity<ShortUrl> getOrCreate(ShortUrl newShortUrl) {

        ShortUrl shortUrl = shortUrlRepository.findByUrl(newShortUrl.getUrl());

        if (shortUrl == null) { // No such shortUrl exists. Create one.
            shortUrlRepository.save(newShortUrl);
            this.assignSlug(newShortUrl);

            HttpHeaders headers = new HttpHeaders();
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{slug}")
                    .buildAndExpand(newShortUrl.getSlug())
                    .toUri();
            headers.setLocation(location);
            return new ResponseEntity<>(newShortUrl, headers, HttpStatus.CREATED);
        }

        this.assignSlug(shortUrl);
        return ResponseEntity.ok(shortUrl);
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
