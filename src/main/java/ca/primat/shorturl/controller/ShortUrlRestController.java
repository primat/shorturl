package ca.primat.shorturl.controller;

import ca.primat.shorturl.exception.rest.InternalServerErrorException;
import ca.primat.shorturl.exception.rest.NotFoundException;
import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Rest controller for {@link ShortUrl}
 */
@RestController
@RequestMapping("/api/v1/shorturl")
public class ShortUrlRestController {

    private static final String ENDPOINT_PATH = "/api/v1/shorturl/";
    private final ShortUrlService shortUrlService;

    @Autowired
    public ShortUrlRestController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }
    /**
     * Handles a "get or create" type operation on {@link ShortUrl} in a single call
     * @param  url The input full URL used in finding an existing or creating a new ShortUrl.
     * @return Returns a {@link ShortUrl} as JSON. If the item was created, return an HTTP 201.
     * If a item already exists, return the item and a HTTP 200. If the URL passed is invalid, returns a HTTP 400.
     */

    @PostMapping(value = "")
    public ResponseEntity<ShortUrl> createOrFind(@Valid @RequestBody ShortUrl url) {
        ShortUrl newShortUrl;
        HttpHeaders headers = new HttpHeaders();

        try {
            newShortUrl = shortUrlService.create(url.getUrl());
        }
        catch(org.springframework.dao.DataIntegrityViolationException e) {
            // Couldn't create the ShortUrl because it already exists. Get the existing one and return it instead.
            ShortUrl shortUrl = shortUrlService.findByUrl(url.getUrl());

            if (shortUrl == null) {
                throw new InternalServerErrorException("Creation and retrieval of the ShortUrl failed");
            }

            return new ResponseEntity<>(shortUrl, HttpStatus.OK);
        }

        // Check if the save operation successfully returned an item. If it did, add a location header
        // with th URL of the newly created resource
        if (newShortUrl == null) {
            throw new InternalServerErrorException("Creation of the ShortUrl failed");
        }
        else {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path(ENDPOINT_PATH)
                    .buildAndExpand(newShortUrl.getSlug())
                    .toUri();
            headers.setLocation(location);
        }

        return new ResponseEntity<>(newShortUrl, headers, HttpStatus.CREATED);
    }

    /**
     * Gets a {@link ShortUrl}, using its slug as the lookup value
     * @param slug The slug used in finding the {@link ShortUrl}
     * @return Returns a {@link ShortUrl} as JSON. Return a HTTP 404 if no ShortUrl is found.
     */
    @GetMapping(value = "/{slug:^[A-Za-z0-9]{1,10}$}")
    public ShortUrl get(@PathVariable String slug) {
        ShortUrl shortUrl = shortUrlService.findBySlug(slug);
        if (shortUrl == null) {
            throw new NotFoundException("ShortUrL not found");
        }
        return shortUrl;
    }
}
