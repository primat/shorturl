package ca.primat.shorturl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * ShortUrl represents a mapping between an absolute URL and a (generated) shorter URL.
 * The path of the minimized URL is referred to as the slug and is generated at runtime
 * based on the primary key "id". This is so that records can easily be sorted without
 * any overhead.
 */
@Entity
public class ShortUrl {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(unique=true)
    @JsonIgnore
    private long id;
    @Transient
    private String slug; // Holds the the id encoded as a base62 string
    private final String url; // Holds the absolute URL that the short version maps to.

    public ShortUrl(String url) {
        this.url = url;
        this.slug = "";
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
