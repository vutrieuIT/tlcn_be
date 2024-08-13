package vn.id.vuductrieu.tlcn_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.id.vuductrieu.tlcn_be.entity.OrderEntity;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto extends OrderEntity {
    private List<OrderItemDto> items;
}
