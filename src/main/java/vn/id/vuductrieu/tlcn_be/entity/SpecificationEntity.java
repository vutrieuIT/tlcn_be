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
    private String internalMemory;

    @Column(name = "RAM")
    private String RAM;

    @Column(name = "performance")
    private String performance;

    @Column(name = "main_camera")
    private String mainCamera;

    @Column(name = "selfie_camera")
    private String selfieCamera;

    @Column(name = "battery_size")
    private String batterySize;

    @Column(name = "screen_size")
    private String screenSize;

    @Column(name = "weight")
    private String weight;

    @Column(name = "price")
    private String price;
}
