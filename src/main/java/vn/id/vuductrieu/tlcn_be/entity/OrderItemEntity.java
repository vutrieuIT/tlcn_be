package vn.id.vuductrieu.tlcn_be.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "order_items")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;

    private Integer price;

    private Integer order_id;

    private Integer product_id;

    private Integer variant_id;

    @Column(name = "created_at")
    @JsonProperty(value = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    @JsonProperty(value = "updated_at")
    private LocalDate updatedAt;
}
