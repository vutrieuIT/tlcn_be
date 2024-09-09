package vn.id.vuductrieu.tlcn_be.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationDto {

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
