package vn.id.vuductrieu.tlcn_be.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class ProductVariationDto {

    private Integer id;

    private String colorType;

    private Double price;

    private Double priceSale;

    private Integer quantity;

    private Integer quantityAvailable;

    private Integer quantitySold;
}
