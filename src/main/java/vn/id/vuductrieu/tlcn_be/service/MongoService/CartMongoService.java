package vn.id.vuductrieu.tlcn_be.service.MongoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.CartDto;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.UserCollection;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.document.ItemDocument;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.UserRepo;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

        //        String userId = permissionService.getUserId().toString();
        String userId = "672e19dc3207000062004d32";

        UserCollection userCollection = userRepo.findById(userId).orElse(null);

        if (userCollection == null) {
            throw new IllegalArgumentException("User not found");
        } else {
            List<ItemDocument> cart = userCollection.getCart();
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
            errors.add("Quantity must be greater than 0");
        }

        if (itemDocument.getPrice() <= 0) {
            errors.add("Price must be greater than 0");
        }

        if (itemDocument.getProductId() == null) {
            errors.add("Product id must not be null");
        }

        if (itemDocument.getColor() == null) {
            errors.add("Color must not be null");
        }

        if (itemDocument.getInternalMemory() == null) {
            errors.add("Internal memory must not be null");
        }

        return String.join(", ", errors);
    }

    public List<ItemDocument> getCart() {
        //        String userId = permissionService.getUserId().toString();
        String userId = "672e19dc3207000062004d32";

        UserCollection userCollection = userRepo.findById(userId).orElse(null);

        if (userCollection == null) {
            throw new IllegalArgumentException("User not found");
        } else {
            return userCollection.getCart();
        }
    }

    public void deleteCart(ItemDocument itemDocument) {
        // TODO permission service
        String userId = "672e19dc3207000062004d32";

        UserCollection userCollection = userRepo.findById(userId).orElse(null);

        if (userCollection == null) {
            throw new IllegalArgumentException("User not found");
        } else {
            if (itemDocument == null) {
                userCollection.setCart(new ArrayList<>());
            } else {
                List<ItemDocument> cart = userCollection.getCart();
                Optional<ItemDocument> existingItem = cart.stream()
                    .filter(item
                        -> Objects.equals(item.getProductId(), itemDocument.getProductId())
                        && Objects.equals(item.getColor(), itemDocument.getColor())
                        && Objects.equals(item.getInternalMemory(), itemDocument.getInternalMemory()))
                    .findFirst();
                existingItem.ifPresent(cart::remove);
                userCollection.setCart(cart);
            }
            userRepo.save(userCollection);
        }
    }
}
