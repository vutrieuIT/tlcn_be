package vn.id.vuductrieu.tlcn_be.service.MongoService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.config.VnPayConfig;
import vn.id.vuductrieu.tlcn_be.entity.OrderEntity;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.OrderCollection;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.UserCollection;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.document.ItemDocument;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.OrderRepo;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.UserRepo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class CheckoutMongoService {

    private final UserRepo userRepo;

    private final OrderRepo orderRepo;

    private final HttpServletRequest request;

    public Map<String, String> checkout() {
        String userId = "672e19dc3207000062004d32";
        UserCollection userCollection = userRepo.findById(userId).orElse(null);
        if (userCollection == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<ItemDocument> carts = userCollection.getCart();

        OrderCollection orderCollection = new OrderCollection();
        orderCollection.setUserId(userId);
        orderCollection.setStatus("pending");
        Integer totalPrice = carts.stream().mapToInt(i -> i.getPrice() * i.getQuantity()).sum();
        orderCollection.setTotalBill(totalPrice);
        orderCollection.setQuantity(carts.size());
        orderCollection.setItems(carts);

        String paymentUrl = createPaymentUrl(Long.valueOf(totalPrice), orderCollection.getId());

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

        vnp_Params.put("vnp_IpAddr", userIp);

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
        return VnPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    public void checkPayment(Map<String, String[]> mapParams, HttpServletRequest request) {
        // checksum
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VnPayConfig.hashAllFields(fields);

        if (!signValue.equals(vnp_SecureHash)) {
            throw new IllegalArgumentException("Invalid signature");
        }

        OrderCollection orderCollection = orderRepo.findById(mapParams.get("vnp_TxnRef")[0]).orElse(null);
        String vnp_ResponseCode = mapParams.get("vnp_ResponseCode")[0];
        if (vnp_ResponseCode.equals("24")) {
            orderCollection.setStatus("unpaid");
//            orderCollection.setUpdatedAt(LocalDateTime.now());
            orderRepo.save(orderCollection);
            throw new IllegalArgumentException("Payment failed");
        }
        if (vnp_ResponseCode.equals("00")) {
            orderCollection.setStatus("paid");
//            orderCollection.setUpdatedAt(LocalDateTime.now());
            orderRepo.save(orderCollection);
            return;
        }
        orderCollection.setStatus("pending");
//        orderCollection.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(orderCollection);
        throw new IllegalArgumentException("Payment failed");
    }
}
