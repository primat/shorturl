package ca.primat.shorturl.controller;

import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest interface for the Short URL entity
 */
@RestController
@RequestMapping("/api/v1/shorturl")
public class ShortUrlRestController {

    private ShortUrlService shortUrlService;

    @Autowired
    public ShortUrlRestController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

//    @GetMapping(value = "/api/v1/shorturl")
//    public ResponseEntity<List<ShortUrl>> getAll(@RequestParam(value="url") String absoluteUrl) {
//        return
//    }

//return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);

    /**
     * Handles a "get or create" type operation on ShortUrl
     * @param absoluteUrl The absolute URL to associate with the short URL
     * @return Returns a ShortUrl as JSON. If the maximum number of items has been reached, then a HTTP 403 is
     * returned. If the URL passed is invalid, returns a HTTP 400.
     */
    @PostMapping(value = "")
    public ResponseEntity<ShortUrl> getOrCreate(@RequestParam(value="url") String absoluteUrl) {

        // TODO: Reject invalid URLs and add error handling

        ShortUrl shortUrl = shortUrlService.getOrCreateShortUrl(absoluteUrl);

        // TODO: Return HTTP 200 if object not created, otherwise return 201.
        return ResponseEntity.ok(shortUrl);
        //return new ResponseEntity<ShortUrl>(shortUrl, HttpStatus.CREATED);
    }
}
