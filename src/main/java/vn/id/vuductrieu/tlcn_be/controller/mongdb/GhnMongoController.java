package vn.id.vuductrieu.tlcn_be.controller.mongdb;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.constants.Constants;
import vn.id.vuductrieu.tlcn_be.dto.mongodb.AdminOrderDto;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.OrderCollection;
import vn.id.vuductrieu.tlcn_be.service.MongoService.GhnMongoService;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;
import vn.id.vuductrieu.tlcn_be.utils.GhnUtil;

@RestController
@RequestMapping("/api/mongo/ghn")
@RequiredArgsConstructor
public class GhnMongoController {

    private final GhnUtil ghnUtil;

    private final GhnMongoService ghnMongoService;

    private final PermissionService permissionService;

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
    public ResponseEntity createShipOrder(@RequestBody AdminOrderDto body) {
        try {
            if (!permissionService.checkRole(Constants.Role.EMPLOYEE.getValue(), Constants.Role.ADMIN.getValue())) {
                return ResponseEntity.badRequest().body("Permission denied");
            }
            ghnMongoService.createShipOrder(body);
            return ResponseEntity.ok("Create order successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancel-ship-order/{orderId}")
    public ResponseEntity cancelShipOrder(@PathVariable String orderId) {
        try {
            if (!permissionService.checkRole(Constants.Role.EMPLOYEE.getValue(), Constants.Role.ADMIN.getValue())) {
                return ResponseEntity.badRequest().body("Permission denied");
            }
            ghnMongoService.cancelShipOrder(orderId);
            return ResponseEntity.ok("Cancel order successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/available-services/{orderId}")
    public ResponseEntity getAvailableServices(@PathVariable String orderId){
        try {
            return ResponseEntity.ok(ghnMongoService.getAvailableServices(orderId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/shipping-fee/{orderId}/{serviceTypeId}")
    public ResponseEntity getShippingFee(@PathVariable String orderId, @PathVariable String serviceTypeId){
        try {
            return ResponseEntity.ok(ghnMongoService.getShippingFee(orderId, serviceTypeId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
