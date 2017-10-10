package ca.primat.shorturl.controller;

import ca.primat.shorturl.model.ShortUrl;
import ca.primat.shorturl.service.Base62Service;
import ca.primat.shorturl.service.UrlService;
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

@Controller
public class HtmlController {

    private Base62Service base62Service;
    private UrlService urlService;

    @Autowired
    public HtmlController(Base62Service base62Service, UrlService urlService) {
        this.base62Service = base62Service;
        this.urlService = urlService;
    }

    /**
     * Controller for the home page where clients can generate their short URLs
     * @return Renders the HTML for the root page.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "index.html";
    }

    /**
     * Checks if a slug exists and redirects to the appropriate URL. Otherwise, returns an HTTP 404 "Not Found"
     * @param slug The slug used in the lookup
     * @param request The request object
     * @param response The response object
     * @return Returns an HTTP 404 if the slug doesn't exist or a 303 and forces a redirection to the mapped URL
     */
    @RequestMapping(value = "/{slug:^[A-Za-z0-9]{1,10}$}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity redirect(@PathVariable String slug, HttpServletRequest request, HttpServletResponse response) {

        // Convert the slug (i.e. path of the short URL) to an ID and use the ID to get the URL from the DB
        long lookupId = base62Service.decode(slug);
        ShortUrl url = urlService.getById(lookupId);

        // Return a 404 if no slug/ID/short URL exists.
        if (url == null) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }

        // Otherwise, redirect to the mapped URL.
        response.setHeader("Location", url.getUrl());
        return new ResponseEntity<>(HttpStatus.SEE_OTHER);
    }
}
