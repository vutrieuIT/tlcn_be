package vn.id.vuductrieu.tlcn_be.entity.mongodb;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "discount")
public class DiscountCollection {
    @Id
    private String id;

    private String code;

    private Integer discount;

    private String discountType;

    private String status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

}
