package ca.primat.shorturl.service;

import ca.primat.shorturl.model.dao.ShortUrlRepository;
import ca.primat.shorturl.model.ShortUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Provides an interface to the controllers for manipulating {@link ShortUrl}s
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
     * Get an existing {@link ShortUrl}, or create a new one
     * @param absoluteUrl The absolute URL that will be mapped to an existing or newly created {@link ShortUrl}
     * @return Returns an existing or new {@link ShortUrl}.
     */
    @Transactional
    public ShortUrl getOrCreateShortUrl(String absoluteUrl) {

        ShortUrl shortUrl;
        List<ShortUrl> urls = shortUrlRepository.findByUrl(absoluteUrl);

        // Get the existing URL or create a new one if none exists
        if (urls.size() > 0) {
            shortUrl = urls.get(0);
        }
        else {
            shortUrl = new ShortUrl(absoluteUrl);
            shortUrlRepository.save(shortUrl);
        }

        // Set the short URL's slug - calculated from its ID
        shortUrl.setSlug(base62Service.encode(shortUrl.getId()));

        return shortUrl;
    }

    /**
     * Get a {@link ShortUrl} by its slug
     * @param slug The slug of the {@link ShortUrl}
     * @return The {@link ShortUrl} object
     */
    public ShortUrl getBySlug(String slug) {
        long lookupId = base62Service.decode(slug);
        return shortUrlRepository.findOne(lookupId);
    }
}
