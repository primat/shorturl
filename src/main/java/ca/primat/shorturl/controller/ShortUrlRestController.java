package ca.primat.shorturl.controller;

import ca.primat.shorturl.exception.EntityNotFoundException;
import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Rest controller for {@link ShortUrl}
 */
@RestController
@RequestMapping("/api/v1/shorturl")
public class ShortUrlRestController {

    private final ShortUrlService shortUrlService;

    @Autowired
    public ShortUrlRestController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    /**
     * Handles a "get or create" type operation on {@link ShortUrl}
     * @param shortUrl The {@link ShortUrl} to retrieve or create, based on the request data
     * @return Returns a {@link ShortUrl} as JSON. If the item was created, return an HTTP 201.
     * If a item already exists, return the item and a HTTP 200. If the URL passed is invalid, returns a HTTP 400.
     */
    @PostMapping(value = "")
    public ResponseEntity<ShortUrl> getOrCreate(@Valid @RequestBody ShortUrl shortUrl) {

        return shortUrlService.getOrCreate(shortUrl);
    }

    /**
     * Gets a {@link ShortUrl} using its slug as the lookup value
     * @param slug The slug used in finding the {@link ShortUrl}
     * @return Returns a {@link ShortUrl} as JSON. Return a HTTP 404 if no ShortUrl is found.
     */
    @GetMapping(value = "/{slug:^[A-Za-z0-9]{1,10}$}")
    public ResponseEntity<ShortUrl> get(@PathVariable String slug) {
        ShortUrl shortUrl = shortUrlService.getBySlug(slug);
        if (shortUrl == null) {
            throw new EntityNotFoundException();
            //return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shortUrl);
    }
}
