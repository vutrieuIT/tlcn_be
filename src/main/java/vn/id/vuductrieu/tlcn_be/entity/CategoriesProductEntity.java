package vn.id.vuductrieu.tlcn_be.entity;

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
public class CategoriesProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "position")
    private Integer position;

    @Column(name = "show_hide")
    private Boolean showHide;

    @ManyToOne
    @JoinColumn(name = "parent_category_id", referencedColumnName = "id")
    private CategoriesProductEntity parentCategory;

    @OneToMany(mappedBy = "category", targetEntity = ProductEntity.class)
    private List<ProductEntity> products;
}
