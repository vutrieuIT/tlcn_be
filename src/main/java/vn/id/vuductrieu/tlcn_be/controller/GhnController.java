package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.utils.GhnUtil;

@RestController
@RequestMapping("/api/ghn")
@RequiredArgsConstructor
public class GhnController {

    private final GhnUtil ghnUtil;

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
}
