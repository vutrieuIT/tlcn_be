package vn.id.vuductrieu.tlcn_be.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class GhnUtil {

    private WebClient webClient = WebClient.create();

    @Value("${ghn.token}")
    private String token;

    @Value("${ghn.url}")
    private String baseUrl;

    @Value("${ghn.shop-id}")
    private String shopId;

    public ResponseEntity getProvince() {
        return webClient.get()
                .uri(baseUrl + URI.PROVINCE)
                .header("Token", token)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    public ResponseEntity getDistrict(Integer provinceId) {
        Map<String, Object> body = Map.of("province_id", provinceId);
        return webClient.post()
                .uri(baseUrl + URI.DISTRICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(body), Map.class)
                .header("Token", token)
                .retrieve()
                .toEntity(String.class)
                .block();
        }

    public ResponseEntity getWard(Integer districtId) {
        Map<String, Object> body = Map.of("district_id", districtId);
        return webClient.post()
                .uri(baseUrl + URI.WARD)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(body), Map.class)
                .header("Token", token)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    private class URI {
        public static final String PROVINCE = "/master-data/province";
        public static final String DISTRICT = "/master-data/district";
        public static final String WARD = "/master-data/ward";
        public static final String SHOP = "/shop/get";
        public static final String ORDER = "/order";
    }
}
