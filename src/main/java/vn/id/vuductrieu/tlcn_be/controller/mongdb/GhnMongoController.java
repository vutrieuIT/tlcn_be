package vn.id.vuductrieu.tlcn_be.controller.mongdb;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.service.MongoService.GhnMongoService;
import vn.id.vuductrieu.tlcn_be.utils.GhnUtil;

@RestController
@RequestMapping("/api/mongo/ghn")
@RequiredArgsConstructor
public class GhnMongoController {

    private final GhnUtil ghnUtil;

    private final GhnMongoService ghnMongoService;

    @GetMapping("/province")
    public ResponseEntity getProvince() {
        return ghnUtil.getProvince();
    }

    @GetMapping("/district/{provinceId}")
    public ResponseEntity getDistrict(@PathVariable Integer provinceId) {
        return ghnUtil.getDistrict(provinceId);
    }

    @GetMapping("/ward/{districtId}")
    public ResponseEntity getWard(@PathVariable Integer districtId) {
        return ghnUtil.getWard(districtId);
    }

    @PostMapping("/create-ship-order")
    public ResponseEntity createShipOrder(@RequestBody String body) {
        try {
            ghnMongoService.createShipOrder(body);
            return ResponseEntity.ok("Create order successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
