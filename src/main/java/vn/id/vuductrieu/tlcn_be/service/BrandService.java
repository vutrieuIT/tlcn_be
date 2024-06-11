package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.BrandDto;
import vn.id.vuductrieu.tlcn_be.entity.BrandEntity;
import vn.id.vuductrieu.tlcn_be.repository.BrandRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    private final ProductService productService;

    public List<BrandEntity> getAllBrand() {
        return brandRepository.findAll();
    }

    public void createBrand(BrandDto brandDto) {
        BrandEntity brandEntity = new BrandEntity();
        BeanUtils.copyProperties(brandDto, brandEntity);
        brandEntity.setPosition(brandRepository.getMaxPosition() + 1);
        brandEntity.setCreated_at(LocalDateTime.now());
        brandRepository.save(brandEntity);

    }

    public void updateBrand(Integer id, BrandDto brandDto) {
        BrandEntity brandEntity = brandRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Không tìm thấy thương hiệu",1));
        BeanUtils.copyProperties(brandDto, brandEntity);
        brandEntity.setUpdated_at(LocalDateTime.now());
        brandRepository.save(brandEntity);
    }

    public void deleteBrand(Integer id) {
        brandRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Không tìm thấy thương hiệu", 1));
        brandRepository.deleteById(id);
    }
}
