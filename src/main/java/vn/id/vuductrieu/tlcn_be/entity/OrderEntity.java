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

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String order_number;
    private String full_name;
    private String phone_number;
    private String address;
    private Integer user_id;
    private LocalDate date_create;
    private LocalTime time_create;

    @Column(name = "created_at")
    @JsonProperty(value = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty(value = "updated_at")
    private LocalDateTime updatedAt;

    private String status;
}
