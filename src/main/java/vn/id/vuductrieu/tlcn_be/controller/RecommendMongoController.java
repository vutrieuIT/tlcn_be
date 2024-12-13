package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.entity.ProductCollection;
import vn.id.vuductrieu.tlcn_be.service.RecommendMongoService;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecommendMongoController {

    private final RecommendMongoService recommendMongoService;

    @GetMapping()
    public ResponseEntity<List<ProductCollection>> recommend(@RequestParam(required = false) String userId,
            @RequestParam(required = false) String productId) {
        return ResponseEntity.ok().body(recommendMongoService.getRecommend(userId, productId));
    }
}
