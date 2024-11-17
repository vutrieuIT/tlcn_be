package vn.id.vuductrieu.tlcn_be.service.MongoService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.mongodb.AdminOrderDto;
import vn.id.vuductrieu.tlcn_be.dto.mongodb.GHNItemDto;
import vn.id.vuductrieu.tlcn_be.dto.mongodb.GHNOrderDto;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.OrderCollection;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.ProductCollection;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.UserCollection;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.document.ItemDocument;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.OrderRepo;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.ProductRepo;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.UserRepo;
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
            () -> new IllegalArgumentException("Order not found")
        );

        String userId = order.getUserId();
        UserCollection user = userRepo.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("User not found")
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
        ghnOrderDto.setToPhone(user.getPhone());
        ghnOrderDto.setToAddress(user.getAddress());
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
                () -> new IllegalArgumentException("Product not found")
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
            throw new IllegalArgumentException("Create order failed");
        }
    }

    public void cancelShipOrder(String orderId) {
        OrderCollection order = orderRepo.findById(orderId).orElseThrow(
            () -> new IllegalArgumentException("Order not found")
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
            throw new IllegalArgumentException("Cancel order failed");
        }
    }

    public Object getAvailableServices(String orderId) {
        OrderCollection order = orderRepo.findById(orderId).orElseThrow(
            () -> new IllegalArgumentException("Order not found")
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
            () -> new IllegalArgumentException("Order not found")
        );

        try {
            List<GHNItemDto> items = new ArrayList<>();
            if (Objects.equals(serviceTypeId, "5")) // hàng nặng
            {
                for (ItemDocument item : order.getItems()) {
                    GHNItemDto ghnItemDto = new GHNItemDto();
                    ProductCollection product = productRepo.findById(item.getProductId()).orElseThrow(
                        () -> new IllegalArgumentException("Product not found")
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
