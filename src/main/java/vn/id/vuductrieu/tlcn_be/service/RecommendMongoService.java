package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import vn.id.vuductrieu.tlcn_be.entity.ProductCollection;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecommendMongoService {

    @Value("${myapp.url.recommend}")
    private String RECOMMEND_URL;

    @Value("${myapp.url.path-recommend-user}")
    private String RECOMMEND_USER_URL;

    @Value("${myapp.url.path-recommend-product}")
    private String RECOMMEND_PRODUCT_URL;

    private final ProductMongoService productMongoService;

    public List<ProductCollection> getRecommend(String userId, String productId) {
        WebClient webClient = WebClient.builder()
                .baseUrl(RECOMMEND_URL)
                .build();

        String uri = userId == null ? RECOMMEND_USER_URL + userId : RECOMMEND_PRODUCT_URL + productId;
        List<String> recommend = null;
        try {
             recommend = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                    })
                    .block();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        if (recommend != null && !recommend.isEmpty()) {
            return productMongoService.getProductsByIds(recommend);
        }

        return productMongoService.popularProduct(10);
    }
}
