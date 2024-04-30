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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "product")
@Data
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "seo_keywords")
    private String seoKeywords;

    @Column(name = "image_url")
    private String imageUrl;


    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "description")
    private String description;

    @Column(name = "show_hide")
    private Boolean showHide;

    @OneToMany(mappedBy = "product", targetEntity = ProductVariationEntity.class, fetch = FetchType.LAZY)
    private List<ProductVariationEntity> variations;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_product_id", referencedColumnName = "id")
    private CategoriesProductEntity category;
}
