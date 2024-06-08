package vn.id.vuductrieu.tlcn_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "brands")
@Data
public class BrandEntity extends SystemField{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "position")
    private Integer position;

    @Column(name = "country")
    private String country;

    @Column(name = "show_hide")
    private String showHide;
}
