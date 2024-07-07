package vn.id.vuductrieu.tlcn_be.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.config.VnPayConfig;
import vn.id.vuductrieu.tlcn_be.dto.CheckoutDto;
import vn.id.vuductrieu.tlcn_be.entity.CartEntity;
import vn.id.vuductrieu.tlcn_be.entity.OrderEntity;
import vn.id.vuductrieu.tlcn_be.entity.OrderItemEntity;
import vn.id.vuductrieu.tlcn_be.repository.CartRepository;
import vn.id.vuductrieu.tlcn_be.repository.CheckoutRepository;
import vn.id.vuductrieu.tlcn_be.repository.OrderItemRepository;
import vn.id.vuductrieu.tlcn_be.repository.OrderRepository;
import vn.id.vuductrieu.tlcn_be.repository.UserRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckoutService {

    private final CheckoutRepository checkoutRepository;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final CartRepository cartRepository;

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    public Map checkout(CheckoutDto checkoutDto) throws Exception {
        Integer userId = checkoutDto.getUser_id();
        List<CartEntity> carts = checkoutRepository.selectByUserId(userId);

        if (carts.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Long totalPrice = 0L;
        OrderEntity order = new OrderEntity();
        order.setUser_id(userId);
        order.setOrder_number(String.valueOf(System.currentTimeMillis()));
        order.setFull_name(checkoutDto.getFull_name());
        order.setPhone_number(checkoutDto.getPhone_number());
        order.setAddress(checkoutDto.getAddress());
        order.setDate_create(LocalDate.now());
        order.setTime_create(LocalTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("pending");

        orderRepository.save(order);

        for (CartEntity cart : carts) {
            totalPrice += (long) cart.getPrice() * cart.getQuantity();
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setPrice(cart.getPrice());
            orderItem.setOrder_id(order.getId());
            orderItem.setProduct_id(cart.getProduct_id());
            orderItem.setVariant_id(cart.getVariant_id());
            orderItem.setCreatedAt(LocalDate.now());
            orderItem.setUpdatedAt(LocalDate.now());
            orderItemRepository.save(orderItem);
        }

        List<CartEntity> cartEntities = cartRepository.selectByUserId(userId);
        cartRepository.deleteAll(cartEntities);

        String paymentUrl = createPaymentUrl(totalPrice, order.getId().toString());

        Map returnData = new HashMap();
        returnData.put("code", "00");
        returnData.put("message", "success");
        returnData.put("data", paymentUrl);
        return Map.of("redirect_url", paymentUrl);
    }

    public String createPaymentUrl(Long amoutPay, String billCode) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long vn2usd = 23200;
        long amount = amoutPay * 100 * vn2usd;

        String vnp_TxnRef = billCode;

        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        String userIp = request.getRemoteAddr();

        vnp_Params.put("vnp_IpAddr", "http://" + userIp + "/lazi-store/checkpayment");

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }

    public void checkPayment(Map<String, String> request) {
        OrderEntity orderEntity = orderRepository.findById(Integer.valueOf(request.get("orderId"))).orElseThrow(
                () -> new IllegalArgumentException("Order not found")
        );
        orderEntity.setStatus(request.get("status"));
        orderEntity.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(orderEntity);
    }
}
