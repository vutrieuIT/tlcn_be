package vn.id.vuductrieu.tlcn_be.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import vn.id.vuductrieu.tlcn_be.dto.CheckoutDto;
import vn.id.vuductrieu.tlcn_be.dto.UserDto;
import vn.id.vuductrieu.tlcn_be.entity.OrderEntity;
import vn.id.vuductrieu.tlcn_be.service.CheckoutService;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutDto checkoutDto) {
        try {
            Map response = checkoutService.checkout(checkoutDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/checkpayment")
    public ResponseEntity<?> checkPayment(@RequestBody Map<String, String> request) {
        try {
            System.out.println(request);
            //            checkoutService.checkPayment(request);
            return ResponseEntity.ok("Thanh toán thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/lazi-store/checkpayment")
    public ResponseEntity<?> checkPayment(HttpServletRequest request) {
        String redirectUrl = "http://localhost:5173/lazi-store/checkpayment?vnp_ResponseCode=00";
        try {
            checkoutService.checkPayment(request.getParameterMap());

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectUrl)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectUrl)
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectUrl)
                    .build();
        }
    }
}
