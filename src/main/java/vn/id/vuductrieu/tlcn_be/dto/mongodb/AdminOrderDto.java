package vn.id.vuductrieu.tlcn_be.dto.mongodb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class AdminOrderDto {

    private String orderId;

    private String fromName;
    private String fromPhone;
    private String fromAddress;

    private String toName;
    private String toPhone;
    private String toAddress;

    @JsonProperty("weight")
    private Integer weight;

    @JsonProperty("length")
    private Integer length;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("service_type_id")
    private Integer serviceTypeId;

    @JsonProperty("payment_type_id")
    private Integer paymentTypeId;

    @JsonProperty("note")
    private String note;

    @JsonProperty("required_note")
    private String requiredNote;

    private Long codAmount; // thu hộ


}
