package ca.primat.shorturl.controller;

import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Rest interface for the Short URL entity
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
     * Handles a "get or create" type operation on ShortUrl
     * @param newShortUrl The {@link ShortUrl} to create based on the request data
     * @return Returns a ShortUrl as JSON. If the maximum number of items has been reached, then a HTTP 403 is
     * returned. If the URL passed is invalid, returns a HTTP 400.
     */
    @PostMapping(value = "")
    public ResponseEntity<ShortUrl> getOrCreate(@Valid @RequestBody ShortUrl newShortUrl) {

        return shortUrlService.getOrCreate(newShortUrl);
    }
}
