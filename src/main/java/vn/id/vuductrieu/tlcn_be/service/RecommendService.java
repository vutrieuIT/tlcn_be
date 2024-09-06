package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import vn.id.vuductrieu.tlcn_be.entity.ProductEntity;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecommendService {

    @Value("${myapp.url.recommend}")
    private String RECOMMEND_URL;

    private final PermissionService permissionService;

    private final ProductService productService;

    public List<ProductEntity> getRecommend(String productId) {
        WebClient webClient = WebClient.builder()
                .baseUrl(RECOMMEND_URL)
                .build();

        Integer userId = permissionService.getUserId();
        String uri = userId == null ? "/api/v2/recommend_product/" + productId : "/api/v2/recommend/" + userId;
        List<Integer> recommend = null;
        try {
             recommend = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Integer>>() {
                    })
                    .block();
        } catch (Exception ignored) {
        }

        if (!recommend.isEmpty()) {
            return productService.getProductsByIds(recommend);
        }

        return productService.popularProduct(10);
    }
}
