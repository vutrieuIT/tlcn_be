package vn.id.vuductrieu.tlcn_be.service.MongoService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class GhnMongoService {

    private final OrderRepo orderRepo;

    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    private final GhnUtil ghnUtil;

    public void createShipOrder(AdminOrderDto adminOrderDto) {
        OrderCollection order = orderRepo.findById(adminOrderDto.getOrderId()).orElseThrow(
                () -> new IllegalArgumentException("Order not found")
        );

        String userId = order.getUserId();
        UserCollection user = userRepo.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        String[] address = Arrays.stream(user.getAddress().split(", ")).map(String::trim).toArray(String[]::new);

        GHNOrderDto ghnOrderDto = new GHNOrderDto();
        ghnOrderDto.setFromName("Vũ Đức Triệu");
        ghnOrderDto.setFromPhone("0987654321");
        ghnOrderDto.setFromAddress("01 Võ Văn Ngân, Linh Chiểu, Thủ Đức, Thành phố Hồ Chí Minh, Vietnam");
        ghnOrderDto.setFromWardName("Linh Chiểu");
        ghnOrderDto.setFromDistrictName("Thủ Đức");
        ghnOrderDto.setFromProvinceName("TP. Hồ Chí Minh");

        ghnOrderDto.setToName(user.getName());
        ghnOrderDto.setToPhone(user.getPhone());
        ghnOrderDto.setToAddress(user.getAddress());
        ghnOrderDto.setToWardName(address[1]); // lấy từ address
        ghnOrderDto.setToDistrictName(address[2]); // lấy từ address
        ghnOrderDto.setToProvinceName(address[3]); // lấy từ address

        ghnOrderDto.setWeight(adminOrderDto.getWeight()); // gam lấy từ UI
        ghnOrderDto.setLength(adminOrderDto.getLength()); // cm lấy từ UI
        ghnOrderDto.setWidth(adminOrderDto.getWidth()); // cm lấy từ UI
        ghnOrderDto.setHeight(adminOrderDto.getHeight()); // cm lấy từ UI

        ghnOrderDto.setServiceTypeId(adminOrderDto.getServiceTypeId()); // lấy từ UI
        ghnOrderDto.setPaymentTypeId(1); // người gửi trả tiền

        ghnOrderDto.setRequiredNote(adminOrderDto.getRequiredNote()); // lấy từ UI

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
            if (ResponseEntity.getStatusCode().is2xxSuccessful()) {
                JsonNode body = (JsonNode) ResponseEntity.getBody();
                String orderCode = body.get("data").get("order_code").asText();
                order.setStatus("Đang gửi hàng");
                order.setShippingOrder(orderCode);
                orderRepo.save(order);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Create order failed");
        }
    }
}
