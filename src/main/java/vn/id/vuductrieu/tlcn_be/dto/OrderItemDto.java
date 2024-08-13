package vn.id.vuductrieu.tlcn_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.id.vuductrieu.tlcn_be.entity.OrderItemEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto extends OrderItemEntity {

    private String image_url;

    private String product_name;
}
