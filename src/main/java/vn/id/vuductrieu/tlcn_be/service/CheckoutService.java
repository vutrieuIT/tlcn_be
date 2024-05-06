package vn.id.vuductrieu.tlcn_be.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.VNPayConfig;
import vn.id.vuductrieu.tlcn_be.dto.CheckoutDto;
import vn.id.vuductrieu.tlcn_be.dto.RegisterDto;
import vn.id.vuductrieu.tlcn_be.entity.CartEntity;
import vn.id.vuductrieu.tlcn_be.entity.OrderEntity;
import vn.id.vuductrieu.tlcn_be.entity.OrderItemEntity;
import vn.id.vuductrieu.tlcn_be.entity.UserEntity;
import vn.id.vuductrieu.tlcn_be.repository.CartRepository;
import vn.id.vuductrieu.tlcn_be.repository.CheckoutRepository;
import vn.id.vuductrieu.tlcn_be.repository.OrderItemRepository;
import vn.id.vuductrieu.tlcn_be.repository.OrderRepository;
import vn.id.vuductrieu.tlcn_be.repository.UserRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckoutService {

    private final UserRepository userRepository;

    private final CheckoutRepository checkoutRepository;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final CartRepository cartRepository;

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    private final VNPayConfig vnpayConfig;

    public Map checkout(CheckoutDto checkoutDto) throws Exception {
        Integer userId = checkoutDto.getUser_id();
        List<CartEntity> carts = checkoutRepository.findByUser_id(userId);

        if (carts.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Long totalPrice = 0L;
        OrderEntity order = new OrderEntity();
        order.setUser_id(userId);
        order.setOrder_number("ORDER-" + System.currentTimeMillis());
        order.setFull_name(checkoutDto.getFull_name());
        order.setPhone_number(checkoutDto.getPhone_number());
        order.setAddress(checkoutDto.getAddress());
        order.setDate_create(LocalDate.now());
        order.setTime_create(LocalTime.now());

        orderRepository.save(order);

        for (CartEntity cart : carts) {
            totalPrice += (long) cart.getPrice() * cart.getQuantity();
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setPrice(cart.getPrice());
            orderItem.setOrder_id(order.getId());
            orderItem.setProduct_id(cart.getProduct_id());
            orderItem.setVariant_id(cart.getVariant_id());
            orderItemRepository.save(orderItem);
        }

        cartRepository.deleteByUser_id(userId);

        String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String vnp_Returnurl = "http://localhost:8080/api/checkpayment";
        String vnp_TmnCode = "MNV5UXO7";
        String vnp_HashSecret = "LBKBRQXZXEYAIAINDKMVJLBYOJEZITIL";

        String vnp_TxnRef = order.getId().toString();
        String vnp_OrderInfo = "Thanh toán đơn hàng";
        String vnp_OrderType = "bank";
        String vnp_Amount = totalPrice.toString();
        String vnp_Locale = "vn";
        String vnp_BankCode = "NCB";
        String vnp_IpAddr = "http://localhost:8080/api/checkpayment";

        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("vnp_Version", "2.1.0");
        inputParam.put("vnp_TmnCode", vnp_TmnCode);
        inputParam.put("vnp_Amount", vnp_Amount);
        inputParam.put("vnp_Command", "pay");

        inputParam.put("vnp_CreateDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        inputParam.put("vnp_CurrCode", "VND");
        inputParam.put("vnp_IpAddr", vnp_IpAddr);
        inputParam.put("vnp_Locale", vnp_Locale);
        inputParam.put("vnp_OrderInfo", vnp_OrderInfo);
        inputParam.put("vnp_OrderType", vnp_OrderType);
        inputParam.put("vnp_ReturnUrl", vnp_Returnurl);
        inputParam.put("vnp_TxnRef", vnp_TxnRef);

        if (vnp_BankCode != null && !vnp_BankCode.isEmpty()) {
            inputParam.put("vnp_BankCode", vnp_BankCode);
        }

        List fieldNames = new ArrayList(inputParam.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();

        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = inputParam.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                // Build query
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
        String salt = VNPayConfig.vnp_HashSecret;
        String vnp_SecureHash = VNPayConfig.hmacSHA512(salt, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        Map returnData = new HashMap();
        returnData.put("code", "00");
        returnData.put("message", "success");
        returnData.put("data", paymentUrl);
        return Map.of("redirect_url", paymentUrl);
    }

    public int checkPayment(HttpServletRequest request) {
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = null;
            String fieldValue = null;
            fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII);
            fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPayConfig.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }
}
