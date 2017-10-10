package ca.primat.shorturl.service;

import ca.primat.shorturl.model.dao.ShortUrlRepository;
import ca.primat.shorturl.model.ShortUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UrlService provides an interface to the controllers for manipulating short URLs
 */
@Service
public class UrlService {

    private Base62Service base62Service;
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    public UrlService(Base62Service base62Service, ShortUrlRepository shortUrlRepository) {
        this.base62Service = base62Service;
        this.shortUrlRepository = shortUrlRepository;
    }

    /**
     * Get an existing or create a new short URL
     * @param absoluteUrl The absolute URL that will be mapped to an existing or newly created short URL
     * @return Returns the existing URL or a new one along with it's short URL.
     */
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

        // Set the slug of the short, calculated from the ShortUrl's ID
        shortUrl.setSlug(base62Service.encode(shortUrl.getId()));

        return shortUrl;
    }

    /**
     * Get a short URL by its ID
     * @param id The ID of the short URL to retrieve
     * @return The short URL object
     */
    public ShortUrl getById(long id) {
        return shortUrlRepository.findOne(id);
    }
}
