package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.SpecificationDto;
import vn.id.vuductrieu.tlcn_be.entity.SpecificationEntity;
import vn.id.vuductrieu.tlcn_be.repository.SpecificationRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpecificationService {


    private final SpecificationRepository specificationRepository;

    public SpecificationEntity getSpecificationByProductId(Integer id) {
        return specificationRepository.findById(id).orElse(null);
    }

    public void deleteByProductId(Integer id) {
        specificationRepository.deleteByCellphoneId(id);
    }

    public void createSpecification(SpecificationDto specificationDto) {
        SpecificationEntity specificationEntity = new SpecificationEntity();
        BeanUtils.copyProperties(specificationDto, specificationEntity);
        specificationRepository.save(specificationEntity);
    }

    public void updateSpecification(SpecificationDto specificationDto) {
        Optional<SpecificationEntity> specificationEntity = specificationRepository.findById(specificationDto.getCellphoneId());
        specificationEntity.ifPresentOrElse(specification -> {
            BeanUtils.copyProperties(specificationDto, specification);
            specificationRepository.save(specification);
        }, () -> {
            throw new IllegalArgumentException("Specification not found");
        });

    }
}
