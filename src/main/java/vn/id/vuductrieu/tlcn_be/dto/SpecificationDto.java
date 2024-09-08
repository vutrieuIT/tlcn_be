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
