package vn.id.vuductrieu.tlcn_be.service.MongoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.UserCollection;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.document.ItemDocument;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartMongoService {

    private final UserRepo userRepo;

    private final PermissionService permissionService;

    public void addToCart(ItemDocument itemDocument) {
        String errors = validateCart(itemDocument);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors);
        }

        String userId = permissionService.getUserId().toString();

        UserCollection userCollection = userRepo.findById(userId).orElse(null);

        if (userCollection == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng");
        } else {
            List<ItemDocument> cart = userCollection.getCart();
            if (cart == null) {
                cart = new ArrayList<>();
            }
            Optional<ItemDocument> existingItem = cart.stream()
                .filter(item -> item.getProductId().equals(itemDocument.getProductId()) && item.getColor().equals(itemDocument.getColor())
                    && item.getInternalMemory().equals(itemDocument.getInternalMemory())).findFirst();
            if (existingItem.isPresent()) {
                ItemDocument item = existingItem.get();
                item.setQuantity(itemDocument.getQuantity());
            } else {
                cart.add(itemDocument);
            }
            userCollection.setCart(cart);
            userRepo.save(userCollection);
        }

    }

    private String validateCart(ItemDocument itemDocument) {
        List<String> errors = new ArrayList<>();
        if (itemDocument.getQuantity() <= 0) {
            errors.add("Số lượng phải lớn hơn 0");
        }

        if (itemDocument.getPrice() <= 0) {
            errors.add("giá phải lớn hơn 0");
        }

        if (itemDocument.getProductId() == null) {
            errors.add("Mã sản phẩm không được để trống");
        }

        if (itemDocument.getColor() == null) {
            errors.add("Màu sắc không được để trống");
        }

        if (itemDocument.getInternalMemory() == null) {
            errors.add("Bộ nhớ trong không được để trống");
        }

        return String.join(", ", errors);
    }

    public List<ItemDocument> getCart() {
        String userId = permissionService.getUserId().toString();

        UserCollection userCollection = userRepo.findById(userId).orElse(null);

        if (userCollection == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng");
        } else {
            return userCollection.getCart();
        }
    }

    public void updateCart(List<ItemDocument> itemDocument) {
        String userId = permissionService.getUserId().toString();

        UserCollection userCollection = userRepo.findById(userId).orElse(null);

        if (userCollection == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng");
        } else {
            userCollection.setCart(itemDocument);
            userRepo.save(userCollection);
        }
    }
}
