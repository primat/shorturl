package ca.primat.shorturl.model.dao;

import ca.primat.shorturl.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    List<ShortUrl> findByUrl(String url);
}
