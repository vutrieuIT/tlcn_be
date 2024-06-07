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
@Table(name = "categories_products")
@Data
public class CategoriesProductEntity extends SystemField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "position")
    private Integer position;

    @Column(name = "show_hide")
    private Boolean show_hide;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_category_id", referencedColumnName = "id")
    private CategoriesProductEntity parentCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "category", targetEntity = ProductEntity.class, fetch = FetchType.LAZY)
    private List<ProductEntity> products;
}
