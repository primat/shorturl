package ca.primat.shorturl.controller;


import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The redirection controller handles the redirections of the short URLs.
 */
@Controller
public class RedirectionController {

    private ShortUrlService shortUrlService;

    @Autowired
    public RedirectionController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    /**
     * Grabs the first segment of the request URL (i.e. a slug) and uses it to find a {@link ShortUrl}.
     * If the {@link ShortUrl} exists, redirect to its mapped absolute URL. Otherwise, return a HTTP 404 not found.
     * @param slug The slug used in the lookup
     * @param request The request object
     * @param response The response object
     * @return Returns returns a {@link ResponseEntity} with HTTP status 404 if the slug doesn't exist or an
     * HTTP 303 with location header set to the mapped absolute URL of the found {@link ShortUrl}
     */
    @RequestMapping(value = "/{slug:^[A-Za-z0-9]{1,10}$}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity redirect(@PathVariable String slug, HttpServletRequest request, HttpServletResponse response) {

        // Get the short URL by its slug
        ShortUrl shortUrl = shortUrlService.getBySlug(slug);

        // Return a 404 if no slug/ID/short URL exists.
        if (shortUrl == null) {
            return new ResponseEntity<>("404 Not Found", HttpStatus.NOT_FOUND);
        }

        // Otherwise, redirect to the mapped URL.
        response.setHeader("Location", shortUrl.getUrl());
        return new ResponseEntity<>(HttpStatus.SEE_OTHER);
    }
}
