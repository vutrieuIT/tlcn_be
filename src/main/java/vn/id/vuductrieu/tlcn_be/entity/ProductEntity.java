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
@Table(name = "products")
@Data
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "seo_keywords")
    private String seo_keywords;

    @Column(name = "product_type")
    private String product_type;

    @Column(name = "brand_id")
    private Integer brand_id;

    @Column(name = "description")
    private String description;

    @Column(name = "show_hide")
    private Integer show_hide;

    @Column(name= "status")
    private String status;

    @OneToMany(mappedBy = "product", targetEntity = ProductVariationEntity.class, fetch = FetchType.LAZY)
    private List<ProductVariationEntity> variations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categories_product_id", referencedColumnName = "id")
    private CategoriesProductEntity category;

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", seo_keywords='" + seo_keywords + '\'' +
                ", product_type='" + product_type + '\'' +
                ", brand_id=" + brand_id +
                ", description='" + description + '\'' +
                ", show_hide=" + show_hide +
                ", status='" + status + '\'' +
                '}';
    }
}
