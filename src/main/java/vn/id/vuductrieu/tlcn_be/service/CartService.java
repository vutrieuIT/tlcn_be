package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.CartDto;
import vn.id.vuductrieu.tlcn_be.entity.CartEntity;
import vn.id.vuductrieu.tlcn_be.repository.CartRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CartService {

    private final CartRepository cartRepository;

    public void addToCart(CartEntity cartEntity) {
        String errors = validateCart(cartEntity);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors);
        }

        Optional<CartEntity> existingCart = cartRepository.findByUser_idAndProduct_idAndVariant_id(cartEntity.getUser_id(),
                cartEntity.getProduct_id(), cartEntity.getVariant_id());
        if (existingCart.isPresent()) {
            CartEntity cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + cartEntity.getQuantity());
            cartRepository.save(cart);
        } else {
            CartEntity newCart = new CartEntity();
            newCart.setQuantity(cartEntity.getQuantity());
            newCart.setPrice(cartEntity.getPrice());
            newCart.setUser_id(cartEntity.getUser_id());
            newCart.setProduct_id(cartEntity.getProduct_id());
            newCart.setVariant_id(cartEntity.getVariant_id());
            cartRepository.save(newCart);
        }
    }

    public List<CartDto> getCartsByUserId(Integer userId){
        return cartRepository.findByUser_id(userId);
    }

    private String validateCart(CartEntity cartEntity) {
        List<String> errors = new ArrayList<>();
        if (cartEntity.getQuantity() <= 0) {
            errors.add("Quantity must be greater than 0");
        }
        if (cartEntity.getPrice() <= 0) {
            errors.add("Price must be greater than 0");
        }
        if (cartEntity.getUser_id() == null) {
            errors.add("User id must not be null");
        }
        if (cartEntity.getProduct_id() == null) {
            errors.add("Product id must not be null");
        }
        if (cartEntity.getVariant_id() == null) {
            errors.add("Variant id must not be null");
        }
        return String.join(", ", errors);
    }

    public void deleteCart(Integer id) {
        CartEntity cart = cartRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cartRepository.delete(cart);
    }

    public void updateCart(CartEntity cartEntity) {
        CartEntity cart = cartRepository.findById(cartEntity.getId()).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.setQuantity(cartEntity.getQuantity());
        cart.setPrice(cartEntity.getPrice());
        cartRepository.save(cart);
    }
}
