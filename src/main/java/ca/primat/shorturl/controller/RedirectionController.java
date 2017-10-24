package ca.primat.shorturl.controller;

import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Handles HTTP redirections for shortened URLs
 */
@Controller
public class RedirectionController {

    private final ShortUrlService shortUrlService;

    @Autowired
    public RedirectionController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    /**
     * Grabs the first segment of the request URL (i.e. a slug) and uses it to find a {@link ShortUrl}.
     * If the {@link ShortUrl} exists, redirect to its mapped absolute URL. Otherwise, return a HTTP 404 not found.
     * @param slug The slug used in the lookup
     * @return Returns returns a {@link ResponseEntity} with HTTP status 404 if the slug doesn't exist or an
     * HTTP 303 with location header set to the mapped absolute URL of the found {@link ShortUrl}
     */
    @GetMapping(value = "/{slug:^[A-Za-z0-9]{1,10}$}")
    @ResponseBody
    public ResponseEntity redirect(@PathVariable String slug) {

        // Get the short URL by its slug and return a 404 if no such slug exists
        ShortUrl shortUrl = shortUrlService.findBySlug(slug);

        if (shortUrl == null) {
            return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
        }

        // Otherwise, redirect to the mapped URL. If the mapped URL is not valid, then return an HTTP 500 status
        HttpHeaders headers = new HttpHeaders();
        URI location = ServletUriComponentsBuilder.fromUriString(shortUrl.getUrl()).build().toUri();
        headers.setLocation(location);
        return new ResponseEntity<String>(headers, HttpStatus.SEE_OTHER);
    }
}
