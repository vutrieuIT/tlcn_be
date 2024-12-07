package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.service.BrandMongoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mongo")
public class BrandMongoController {


    private final BrandMongoService brandMongoService;

    @GetMapping("/thuong-hieu")
    public ResponseEntity getBrand() {
        try {
            return ResponseEntity.ok().body(brandMongoService.getBrand());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
