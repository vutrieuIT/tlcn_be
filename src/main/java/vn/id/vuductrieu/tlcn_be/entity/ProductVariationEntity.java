package vn.id.vuductrieu.tlcn_be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "product_variation")
@Data
public class ProductVariationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "color_type")
    private String colorType;

    @Column(name = "price")
    private Double price;

    @Column(name = "price_sale")
    private Double priceSale;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "quantity_available")
    private Integer quantityAvailable;

    @Column(name = "quantity_sold")
    private Integer quantitySold;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductEntity product;

}
