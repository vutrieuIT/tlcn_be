package vn.id.vuductrieu.tlcn_be.entity.mongodb.document;

import lombok.Data;

@Data
public class ItemDocument {

    private String productId;

    private String productName;

    private String color;

    private Integer quantity;

    private Integer internalMemory;

    private Integer price;
}
