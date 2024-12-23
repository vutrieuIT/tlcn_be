package vn.id.vuductrieu.tlcn_be.controller;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.dto.CheckoutMongoDto;
import vn.id.vuductrieu.tlcn_be.service.CheckoutMongoService;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CheckoutMongoController {

    private final CheckoutMongoService checkoutMongoService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutMongoDto checkoutMongoDto) {
        try {
            Map response = checkoutMongoService.checkout(checkoutMongoDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/payment/{orderId}")
    public ResponseEntity<?> payment(@PathVariable String orderId) {
        try {
            Map response = checkoutMongoService.payment(orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/checkpayment")
    public ResponseEntity<?> checkPayment(@RequestBody Map<String, String> request) {
        try {
            System.out.println(request);
            //            checkoutMongoService.checkPayment(request);
            return ResponseEntity.ok("Thanh toán thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/lazi-store/checkpayment")
    public ResponseEntity<?> checkPayment(HttpServletRequest request) {
        String redirectUrl = Dotenv.load().get("APP_HOST") + "/lazi-store/checkpayment?vnp_ResponseCode=%s";
        try {
            System.out.println("URL:" + request.getRequestURL());
            checkoutMongoService.checkPayment(request.getParameterMap(), request);

            redirectUrl = redirectUrl.formatted("00");

            return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUrl)
                .build();
        } catch (IllegalArgumentException e) {
            redirectUrl = redirectUrl.formatted("01");
            return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUrl)
                .build();
        } catch (Exception e) {
            redirectUrl = redirectUrl.formatted("99");
            return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUrl)
                .build();
        }
    }
}
