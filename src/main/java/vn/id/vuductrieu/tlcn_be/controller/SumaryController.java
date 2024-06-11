package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.service.SumaryService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/sumary")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SumaryController {

    private final SumaryService sumaryService;

    @GetMapping("sales")
    public ResponseEntity<Object> getSales(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            if (startDate == null) {
                startDate = LocalDate.now().minusDays(30);
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }
            return ResponseEntity.ok().body(sumaryService.getSales(startDate, endDate));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("revenue")
    public ResponseEntity<Object> getRevenue(@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
        try {
            if (startDate == null) {
                startDate = LocalDate.now().minusDays(30);
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }
            return ResponseEntity.ok().body(sumaryService.getRevenue(startDate, endDate));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
