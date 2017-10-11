package ca.primat.shorturl.controller;

import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest interface for the Short URL entity
 */
@RestController
public class ShortUrlRestController {

    private UrlService urlService;

    @Autowired
    public ShortUrlRestController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * Handles a "get or create" type operation on ShortUrl
     * @param absoluteUrl The absolute URL to associate with the short URL
     * @return Returns a ShortUrl as JSON. If the maximum number of items has been reached, then a HTTP 403 is
     * returned. If the URL passed is invalid, returns a HTTP 400.
     */
    @PostMapping(value = "/api/v1/shorturl")
    public ResponseEntity<ShortUrl> getOrCreate(@RequestParam(value="url") String absoluteUrl) {

        // TODO: Reject invalid URLs and add error handling

        ShortUrl shortUrl = urlService.getOrCreateShortUrl(absoluteUrl);
        return ResponseEntity.ok(shortUrl);
    }
}
