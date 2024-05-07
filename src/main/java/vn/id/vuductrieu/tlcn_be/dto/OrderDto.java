package vn.id.vuductrieu.tlcn_be.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.id.vuductrieu.tlcn_be.entity.OrderEntity;
import vn.id.vuductrieu.tlcn_be.entity.OrderItemEntity;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto extends OrderEntity {

    List<OrderItemEntity> items;
}
