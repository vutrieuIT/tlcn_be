package vn.id.vuductrieu.tlcn_be.dto.mongodb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminOrderDto {

    private String orderId;

    private String fromName;
    private String fromPhone;
    private String fromAddress;
    private String toName;
    private String toPhone;
    private String toAddress;

    private Integer weight;

    private Integer length;

    private Integer width;

    private Integer height;

    private Integer serviceTypeId;

    private Integer paymentTypeId;

    private String note;

    private String requiredNote;

    private Long codAmount; // thu hộ


}
