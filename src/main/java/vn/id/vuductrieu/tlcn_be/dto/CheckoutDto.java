package vn.id.vuductrieu.tlcn_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutDto {

    private Integer user_id;

    private String email;
    private String full_name;

    private String phone_number;
    private String address;
}
