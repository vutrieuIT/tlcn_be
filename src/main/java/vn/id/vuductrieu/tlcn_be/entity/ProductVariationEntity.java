package vn.id.vuductrieu.tlcn_be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "product_variations")
@Data
public class ProductVariationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "image_path")
    private String image_path;

    @Column(name = "color_type")
    private String color_type;

    @Column(name = "price")
    private String price;

    @Column(name = "price_sale")
    private String price_sale;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "quantity_available")
    private Integer quantity_available;

    @Column(name = "quantity_sold")
    private Integer quantity_sold;

    @Column(name = "position")
    private Integer position;

    @Column(name = "show_hide")
    private Integer show_hide;


    @Column(name = "product_id", insertable = false, updatable = false)
    private Integer product_id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductEntity product;

}
