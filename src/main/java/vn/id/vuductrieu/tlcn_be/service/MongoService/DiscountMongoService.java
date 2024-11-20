package vn.id.vuductrieu.tlcn_be.service.MongoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.constants.Constants;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.DiscountCollection;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.DiscountRepo;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountMongoService {

    private final DiscountRepo discountRepo;

    private final PermissionService permissionService;
    public List<DiscountCollection> getAll() {
        return discountRepo.findAllSortByCreatedAt();
    }

    public void create(DiscountCollection discount) {
        DiscountCollection discountCollection = discountRepo.findByCodeAndStatus(discount.getCode(), Constants.DiscountStatus.ACTIVE.getValue());
        if (discountCollection != null) {
            throw new IllegalArgumentException("Mã giảm giá đã tồn tại");
        }
        String employeeId = permissionService.getUserId();
        discount.setEmployeeId(employeeId);
        discountRepo.insert(discount);
    }

    public void update(DiscountCollection discount) {
        DiscountCollection discountCollection = discountRepo.findById(discount.getId()).orElse(null);
        if (discountCollection == null) {
            throw new IllegalArgumentException("Không tìm thấy mã giảm giá");
        }

        if (discountCollection.getStatus().equals(Constants.DiscountStatus.USED.getValue())) {
            throw new IllegalArgumentException("Mã giảm giá đã được sử dụng");
        }
        String employeeId = permissionService.getUserId();
        discount.setEmployeeId(employeeId);
        discountRepo.save(discount);
    }
}
