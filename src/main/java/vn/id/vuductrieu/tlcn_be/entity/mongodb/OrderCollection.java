package vn.id.vuductrieu.tlcn_be.entity.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.document.ItemDocument;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "order")
public class OrderCollection {

    @Id
    private String id;

    private Integer totalBill;

    private Integer quantity;

    private List<ItemDocument> items;

    private String status;
    private String userId;

    private String discountId;

    private String paymentStatus;

    private String paymentType;

    private String shippingOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
