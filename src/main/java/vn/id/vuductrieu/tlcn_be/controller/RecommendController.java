package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.entity.ProductEntity;
import vn.id.vuductrieu.tlcn_be.service.RecommendService;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping()
    public ResponseEntity<List<ProductEntity>> recommend(@RequestParam(required = false) String userId,
            @RequestParam(required = false) String productId) {
        return ResponseEntity.ok().body(recommendService.getRecommend(userId, productId));
    }
}
