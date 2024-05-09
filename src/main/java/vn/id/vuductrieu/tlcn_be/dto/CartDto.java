package vn.id.vuductrieu.tlcn_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import vn.id.vuductrieu.tlcn_be.entity.CartEntity;

@NoArgsConstructor
@Data
public class CartDto {
    private Integer id;
    private int quantity;
    private Integer price;
    private Integer user_id;
    private Integer product_id;
    private Integer variant_id;
    private String product_name;
    private String variation_color;

    public CartDto(Integer id, int quantity, Integer price, Integer user_id, Integer product_id, Integer variant_id, String product_name, String variation_color) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.user_id = user_id;
        this.product_id = product_id;
        this.variant_id = variant_id;
        this.product_name = product_name;
        this.variation_color = variation_color;
    }
}
