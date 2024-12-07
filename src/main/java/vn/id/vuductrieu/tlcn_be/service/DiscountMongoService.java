package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.constants.MyConstants;
import vn.id.vuductrieu.tlcn_be.entity.DiscountCollection;
import vn.id.vuductrieu.tlcn_be.repository.DiscountRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountMongoService {

    private final DiscountRepo discountRepo;

    private final PermissionService permissionService;
    public List<DiscountCollection> getAll() {
        List<DiscountCollection> discountCollections = discountRepo.findAll();
        discountCollections.sort((discount1, discount2) -> sortByStatus(discount1, discount2));
        return discountCollections;
    }

    public void create(DiscountCollection discount) {
        DiscountCollection discountCollection = discountRepo.findByCodeAndStatus(discount.getCode(), MyConstants.DiscountStatus.ACTIVE.getValue());
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

        if (discountCollection.getStatus().equals(MyConstants.DiscountStatus.USED.getValue())) {
            throw new IllegalArgumentException("Mã giảm giá đã được sử dụng");
        }
        String employeeId = permissionService.getUserId();
        discount.setEmployeeId(employeeId);
        discountRepo.save(discount);
    }

    private int sortByStatus(DiscountCollection discount1, DiscountCollection discount2) {
        Integer mapStaus1 = MyConstants.DiscountStatus.ACTIVE.getValue().equals(discount1.getStatus()) ? 3 :
            MyConstants.DiscountStatus.INACTIVE.getValue().equals(discount1.getStatus()) ? 2 : 1;
        Integer mapStaus2 = MyConstants.DiscountStatus.ACTIVE.getValue().equals(discount2.getStatus()) ? 3 :
            MyConstants.DiscountStatus.INACTIVE.getValue().equals(discount2.getStatus()) ? 2 : 1;
        return mapStaus2.compareTo(mapStaus1);
    }
}
