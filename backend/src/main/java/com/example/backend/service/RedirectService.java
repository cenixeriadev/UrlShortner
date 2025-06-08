package  com.example.backend.service;
import com.example.backend.repository.ShortUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling URL
 * redirection operations.
 * <p>
 * This class extends {@link BaseUrlService}
 * and provides functionality to resolve
 * original URLs from shortcodes without
 * caching the results. It's primarily used
 * for redirecting users to the original URLs.
 *
 * @see BaseUrlService
 * @see RedisService
 * @see ShortUrlRepository
 */
@Slf4j
@Service
public class RedirectService extends BaseUrlService {

    /**
     * Constructs a new RedirectService
     * with the required dependencies.
     * @param shortUrlRepository repository for accessing short URL data
     * @param redisService service for cache operations
     */
    public RedirectService(ShortUrlRepository shortUrlRepository,
                           RedisService redisService) {
        super(shortUrlRepository, redisService);
    }

    /**
     * Retrieves the original URL
     * associated with the given shortcode.
     * <p>
     * This method resolves the original
     * URL without caching the result,
     * as it's used for immediate
     * redirection purposes.
     *
     * @param shortCode the shortcode to resolve
     * @return the original URL associated with the shortcode
     * @throws RuntimeException if the URL is not found
     */
    public String getOriginalUrl(String shortCode) {
        return resolveUrl(shortCode, false);
    }
}