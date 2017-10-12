package ca.primat.shorturl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * A controller for displaying HTML pages
 */
@Controller
public class HtmlController {

    /**
     * Renders the index page, where clients can generate short URLs
     * @return The index template name
     */
    @GetMapping(value = "/")
    public String home() {
        return "index.html";
    }
}
