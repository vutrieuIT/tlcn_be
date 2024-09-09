package vn.id.vuductrieu.tlcn_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "specification")
@Data
public class SpecificationEntity {
    // 'cellphone_id',"brand", "model", "operating_system"
    // "internal_memory","RAM","performance","main_camera","selfie_camera","battery_size","screen_size","weight","price"

    @Id
    @Column(name = "cellphone_id")
    private Integer cellphoneId;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "operating_system")
    private String operatingSystem;

    @Column(name = "internal_memory")
    private Integer internalMemory;

    @Column(name = "RAM")
    private Integer RAM;

    @Column(name = "performance")
    private Integer performance;

    @Column(name = "main_camera")
    private Integer mainCamera;

    @Column(name = "selfie_camera")
    private Integer selfieCamera;

    @Column(name = "battery_size")
    private Integer batterySize;

    @Column(name = "screen_size")
    private Integer screenSize;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "price")
    private Integer price;
}
