package ca.primat.shorturl.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * A basic controller for displaying HTML pages
 */
@Controller
public class HtmlController {

    /**
     * Controller for the home page where clients can generate their short URLs
     * @return The index template
     */
    @GetMapping(value = "/")
    public String home() {
        return "index.html";
    }
}
