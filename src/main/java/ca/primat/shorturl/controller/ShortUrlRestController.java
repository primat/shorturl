package ca.primat.shorturl.controller;

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
     * @return Returns a {@link ShortUrl} as JSON. If the item was creates, return an HTTP 201.
     * If a item already exists, return the item and a HTTP 200. If the URL passed is invalid, returns a HTTP 400.
     */
    @PostMapping(value = "")
    public ResponseEntity<ShortUrl> getOrCreate(@Valid @RequestBody ShortUrl shortUrl) {

        return shortUrlService.getOrCreate(shortUrl);
    }
}
