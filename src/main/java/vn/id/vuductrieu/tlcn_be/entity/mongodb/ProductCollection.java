package vn.id.vuductrieu.tlcn_be.entity.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.document.SpecificationDocument;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.document.VariantDocument;

import java.util.List;

@Data
@Document(collection = "product")
public class ProductCollection {

    @Id
    private String id;

    private String name;

    private String brandName;

    private String description;

    private Boolean isShow;

    private String status;

    private Integer ram;

    private String operatingSystem;

    private Integer mainCamera;

    private Integer selfieCamera;

    private Integer batterySize;

    private Double screenSize;

    private Integer weight;

    private Integer height;

    private Integer width;

    private Integer length;

    private List<SpecificationDocument> specifications;

    private List<VariantDocument> variants;
}
