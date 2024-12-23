package vn.id.vuductrieu.tlcn_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.AdminOrderDto;
import vn.id.vuductrieu.tlcn_be.dto.GHNItemDto;
import vn.id.vuductrieu.tlcn_be.dto.GHNOrderDto;
import vn.id.vuductrieu.tlcn_be.entity.OrderCollection;
import vn.id.vuductrieu.tlcn_be.entity.ProductCollection;
import vn.id.vuductrieu.tlcn_be.entity.UserCollection;
import vn.id.vuductrieu.tlcn_be.entity.document.ItemDocument;
import vn.id.vuductrieu.tlcn_be.repository.OrderRepo;
import vn.id.vuductrieu.tlcn_be.repository.ProductRepo;
import vn.id.vuductrieu.tlcn_be.repository.UserRepo;
import vn.id.vuductrieu.tlcn_be.utils.GhnUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GhnMongoService {

    private final OrderRepo orderRepo;

    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    private final GhnUtil ghnUtil;

    @Value("${ghn.shop-id}")
    private Integer shopId;

    public void createShipOrder(AdminOrderDto adminOrderDto) {
        OrderCollection order = orderRepo.findById(adminOrderDto.getOrderId()).orElseThrow(
            () -> new IllegalArgumentException("Không tìm thấy đơn hàng")
        );

        String userId = order.getUserId();
        UserCollection user = userRepo.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("Không tìm thấy người dùng")
        );

        String toAddress = order.getToAddress() == null || order.getToAddress().equals("") ? user.getAddress() : order.getToAddress();

        String[] address = Arrays.stream(toAddress.split(",")).map(String::trim).toArray(String[]::new);

        GHNOrderDto ghnOrderDto = new GHNOrderDto();
        ghnOrderDto.setFromName(adminOrderDto.getFromName());
        ghnOrderDto.setFromPhone(adminOrderDto.getFromPhone());
        ghnOrderDto.setFromAddress("01 Võ Văn Ngân, Linh Chiểu, Thủ Đức, Thành phố Hồ Chí Minh, Vietnam");
        ghnOrderDto.setFromWardName("Linh Chiểu");
        ghnOrderDto.setFromDistrictName("Thủ Đức");
        ghnOrderDto.setFromProvinceName("TP. Hồ Chí Minh");

        ghnOrderDto.setToName(user.getName());
        ghnOrderDto.setToPhone(order.getPhoneNumber());
        ghnOrderDto.setToAddress(order.getToAddress());
        ghnOrderDto.setToWardName(address[1]);
        ghnOrderDto.setToDistrictName(address[2]);
        ghnOrderDto.setToProvinceName(address[3]);

        ghnOrderDto.setWeight(adminOrderDto.getWeight());
        ghnOrderDto.setLength(adminOrderDto.getLength());
        ghnOrderDto.setWidth(adminOrderDto.getWidth());
        ghnOrderDto.setHeight(adminOrderDto.getHeight());

        ghnOrderDto.setServiceTypeId(adminOrderDto.getServiceTypeId());
        ghnOrderDto.setPaymentTypeId(1);

        ghnOrderDto.setRequiredNote(adminOrderDto.getRequiredNote());

        List<GHNItemDto> items = new ArrayList<>();
        for (ItemDocument item : order.getItems()) {
            GHNItemDto ghnItemDto = new GHNItemDto();
            ProductCollection product = productRepo.findById(item.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("Không tìm thấy sản phẩm")
            );
            ghnItemDto.setName(product.getName());
            ghnItemDto.setQuantity(item.getQuantity());

            items.add(ghnItemDto);
        }

        ghnOrderDto.setItems(items);

        try {
            System.out.println(new ObjectMapper().writeValueAsString(ghnOrderDto));
            ResponseEntity ResponseEntity = ghnUtil.post(GhnUtil.URI.ORDER, ghnOrderDto);
            JsonNode body = (JsonNode) ResponseEntity.getBody();
            if (ResponseEntity.getStatusCode().is2xxSuccessful()) {
                String orderCode = body.get("data").get("order_code").asText();
                order.setStatus("Đang giao");
                order.setShippingOrderStatus("Đã tạo đơn hàng");
                order.setShippingOrderId(orderCode);
                orderRepo.save(order);
            } else {
                throw new IllegalArgumentException(body.get("data").get("message").asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Tạo đơn hàng thất bại");
        }
    }

    public void cancelShipOrder(String orderId) {
        OrderCollection order = orderRepo.findById(orderId).orElseThrow(
            () -> new IllegalArgumentException("Không tìm thấy đơn hàng")
        );

        try {
            ResponseEntity ResponseEntity = ghnUtil.post(GhnUtil.URI.CANCEL_ORDER, Map.of(
                "order_codes", List.of(order.getShippingOrderId())
            ));
            JsonNode bodyResponse = (JsonNode) ResponseEntity.getBody();
            if (ResponseEntity.getStatusCode().is2xxSuccessful()) {
                order.setShippingOrderStatus("Đã hủy");
                order.setStatus("Xác nhận");
                orderRepo.save(order);
            } else {
                throw new IllegalArgumentException(bodyResponse.get("data").get("message").asText());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Hủy đơn hàng thất bại");
        }
    }

    public Object getAvailableServices(String orderId) {
        OrderCollection order = orderRepo.findById(orderId).orElseThrow(
            () -> new IllegalArgumentException("Không tìm thấy đơn hàng")
        );

        try {
            Map<String, Object> body = Map.of(
                "shop_id", shopId,
                "from_district", 3695, // Thành phố Thủ Đức
                "to_district", order.getAddressCode()[1]
            );
            ResponseEntity ResponseEntity = ghnUtil.post(GhnUtil.URI.GET_AVAILABLE_SERVICES, body);
            JsonNode bodyResponse = (JsonNode) ResponseEntity.getBody();
            if (ResponseEntity.getStatusCode().is2xxSuccessful()) {
                return bodyResponse.get("data");
            } else {
                throw new IllegalArgumentException(bodyResponse.get("data").get("message").asText());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    public Integer getShippingFee(String orderId, String serviceTypeId) {
        OrderCollection order = orderRepo.findById(orderId).orElseThrow(
            () -> new IllegalArgumentException("Không tìm thấy đơn hàng")
        );

        try {
            List<GHNItemDto> items = new ArrayList<>();
            if (Objects.equals(serviceTypeId, "5")) // hàng nặng
            {
                for (ItemDocument item : order.getItems()) {
                    GHNItemDto ghnItemDto = new GHNItemDto();
                    ProductCollection product = productRepo.findById(item.getProductId()).orElseThrow(
                        () -> new IllegalArgumentException("Không tìm thấy sản phẩm")
                    );
                    ghnItemDto.setName(product.getName());
                    ghnItemDto.setQuantity(item.getQuantity());
                    ghnItemDto.setWeight(product.getWeight());
                    ghnItemDto.setHeight(product.getHeight());
                    ghnItemDto.setLength(product.getLength());
                    ghnItemDto.setWidth(product.getWidth());
                    items.add(ghnItemDto);
                }
            }
            Map<String, Object> body = Map.of(
                "to_ward_code", order.getAddressCode()[0].toString(),
                "to_district_id", order.getAddressCode()[1],
                "service_type_id", Integer.parseInt(serviceTypeId),
                "weight", order.getTotalWeight(),
                "height", 10,
                "length", 10,
                "width", 10,
                "items", items
            );
            ResponseEntity ResponseEntity = ghnUtil.post(GhnUtil.URI.GET_SHIPPING_FEE, body);
            JsonNode bodyResponse = (JsonNode) ResponseEntity.getBody();
            if (ResponseEntity.getStatusCode().is2xxSuccessful()) {
                return bodyResponse.get("data").get("total").asInt();
            } else {
                throw new IllegalArgumentException(bodyResponse.get("data").get("message").asText());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
