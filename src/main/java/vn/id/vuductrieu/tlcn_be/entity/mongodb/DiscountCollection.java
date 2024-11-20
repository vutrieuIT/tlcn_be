package vn.id.vuductrieu.tlcn_be.entity.mongodb;

import com.google.api.client.util.DateTime;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "discount")
public class DiscountCollection {
    @Id
    private String id;

    private String code;

    private Integer discount;

    private String discountType;

    private String status;

    private LocalDate startDate;

    private LocalDate endDate;

    private String employeeId;

}
