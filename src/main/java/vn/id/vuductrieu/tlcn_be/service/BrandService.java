package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.entity.BrandEntity;
import vn.id.vuductrieu.tlcn_be.repository.BrandRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public List<BrandEntity> getAllBrand() {
        return brandRepository.findAll();
    }
}
