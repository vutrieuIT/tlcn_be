package vn.id.vuductrieu.tlcn_be.dto;

import lombok.RequiredArgsConstructor;
import vn.id.vuductrieu.tlcn_be.entity.CartEntity;

@RequiredArgsConstructor
public class CartDto extends CartEntity {
    private String product_name;
    private String variation_color;
}
