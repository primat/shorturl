package ca.primat.shorturl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonIgnore
    private Long id; // Initialized to an invalid ID

    @Transient
    private String slug = ""; // Holds the the id encoded as a base62 string

    @URL
    @Length(min = 1, max = 255)
    @NotEmpty
    @Column(unique=true)
    private String url = ""; // Holds the absolute URL that the short version maps to.

    public ShortUrl() {
        super();
    }

    public ShortUrl(String url) {
        super();
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format(
                "ShortUrl[id=%d, slug='%s', usrl='%s']",
                id, slug, url);
    }

    public Long getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getUrl() {
        return url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
