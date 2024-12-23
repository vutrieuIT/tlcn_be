package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.service.SumaryMongoService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/sumary")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SumaryMongoController {

    private final SumaryMongoService sumaryMongoService;

    @GetMapping("sales")
    public ResponseEntity<Object> getSales(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, @RequestParam(required = false) String brand) {
        try {
            if (startDate == null) {
                startDate = LocalDate.now().minusDays(30);
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }
            return ResponseEntity.ok().body(sumaryMongoService.getSales(startDate, endDate, brand));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("revenue")
    public ResponseEntity<Object> getRevenue(@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate,
        @RequestParam(required = false) String brand) {
        try {
            if (startDate == null) {
                startDate = LocalDate.now().minusDays(30);
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }
            return ResponseEntity.ok().body(sumaryMongoService.getRevenue(startDate, endDate, brand));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
