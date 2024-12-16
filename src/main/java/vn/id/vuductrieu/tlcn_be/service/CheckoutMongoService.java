package vn.id.vuductrieu.tlcn_be.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.config.VnPayConfig;
import vn.id.vuductrieu.tlcn_be.constants.MyConstants;
import vn.id.vuductrieu.tlcn_be.dto.CheckoutMongoDto;
import vn.id.vuductrieu.tlcn_be.entity.DiscountCollection;
import vn.id.vuductrieu.tlcn_be.entity.OrderCollection;
import vn.id.vuductrieu.tlcn_be.entity.ProductCollection;
import vn.id.vuductrieu.tlcn_be.entity.UserCollection;
import vn.id.vuductrieu.tlcn_be.entity.document.ItemDocument;
import vn.id.vuductrieu.tlcn_be.entity.document.SpecificationDocument;
import vn.id.vuductrieu.tlcn_be.entity.document.VariantDocument;
import vn.id.vuductrieu.tlcn_be.repository.DiscountRepo;
import vn.id.vuductrieu.tlcn_be.repository.OrderRepo;
import vn.id.vuductrieu.tlcn_be.repository.ProductRepo;
import vn.id.vuductrieu.tlcn_be.repository.UserRepo;

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

    private final DiscountRepo discountRepo;

    private final ProductRepo productRepo;

    private final HttpServletRequest request;

    private final PermissionService permissionService;

    private final ProductMongoService productMongoService;

    public Map<String, String> checkout(CheckoutMongoDto checkoutMongoDto) {
        String userId = permissionService.getUserId();
        UserCollection userCollection = userRepo.findById(userId).orElse(null);
        if (userCollection == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng");
        }

        List<ItemDocument> carts = userCollection.getCart();

        OrderCollection orderCollection = new OrderCollection();
        orderCollection.setUserId(userId);
        orderCollection.setStatus(MyConstants.OrderStatus.PENDING.getValue());
        int totalPrice = carts.stream().mapToInt(i -> i.getPrice() * i.getQuantity()).sum();
        orderCollection.setTotalBill(totalPrice);
        orderCollection.setQuantity(carts.size());
        orderCollection.setItems(carts);
        orderCollection.setToAddress(checkoutMongoDto.getAddress());
        orderCollection.setAddressCode(checkoutMongoDto.getAddressCode());
        orderCollection.setPhoneNumber(checkoutMongoDto.getPhone_number());
        int weight = 0;
        for (ItemDocument item : carts) {
            ProductCollection productCollection = productRepo.findById(item.getProductId()).orElse(null);
            if (productCollection == null) {
                throw new IllegalArgumentException("Sản phẩm không tồn tại");
            }
            // minus quantity
            SpecificationDocument specificationDocument = productCollection.getSpecifications().stream()
                .filter(s -> s.getInternalMemory().equals(item.getInternalMemory())).findFirst().orElse(null);
            if (specificationDocument == null) {
                throw new IllegalArgumentException("Phiên bản " + item.getInternalMemory() + " không tồn tại");
            }

            SpecificationDocument.ColorVariant colorVariant = specificationDocument.getColorVariant().stream()
                .filter(v -> v.getColor().equals(item.getColor())).findFirst().orElse(null);

            if (colorVariant == null) {
                throw new IllegalArgumentException("Màu " + item.getColor() + " không tồn tại");
            }

            // check quantity
            if (colorVariant.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Số lượng sản phẩm " + productCollection.getName() + " màu " + item.getColor() + " phiên bản " + item.getInternalMemory() + " không đủ");
            }
            colorVariant.setQuantity(colorVariant.getQuantity() - item.getQuantity());
            productMongoService.setStatusProductByQuantity(productCollection);

            productRepo.save(productCollection);
            weight += productCollection.getWeight() * item.getQuantity();
        }
        orderCollection.setTotalWeight(weight);
        orderCollection.setPaymentStatus(MyConstants.PaymentStatus.UNPAID.getValue());
        orderCollection.setCreatedAt(LocalDateTime.now());
        orderRepo.save(orderCollection);

        if (checkoutMongoDto.getDiscountCode() != null) {
            DiscountCollection discountCollection = discountRepo.findByCode(checkoutMongoDto.getDiscountCode());
            if (discountCollection == null) {
                throw new IllegalArgumentException("Mã giảm giá không tồn tại");
            }
            if (discountCollection.getStatus().equals(MyConstants.DiscountStatus.USED.getValue())) {
                throw new IllegalArgumentException("Mã giảm giá đã được sử dụng");
            }
            if (discountCollection.getDiscount() > totalPrice) {
                throw new IllegalArgumentException("Mã giảm giá không hợp lệ");
            }
            if (discountCollection.getDiscountType().equals(MyConstants.DiscountType.PERCENTAGE.getValue())) {
                totalPrice = totalPrice - (totalPrice * discountCollection.getDiscount() / 100);
            } else {
                totalPrice = totalPrice - discountCollection.getDiscount();
            }
            orderCollection.setDiscountId(discountCollection.getId());
        }

        userCollection.setCart(new ArrayList<>());
        userRepo.save(userCollection);

        String paymentUrl = createPaymentUrl((long) totalPrice, orderCollection.getId());

        return Map.of("redirect_url", paymentUrl);
    }

    public String createPaymentUrl(Long amoutPay, String billCode) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = amoutPay * 100;

        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", billCode);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + billCode);
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
            throw new IllegalArgumentException("checksum không đúng");
        }

        OrderCollection orderCollection = orderRepo.findById(mapParams.get("vnp_TxnRef")[0]).orElse(null);
        String vnp_ResponseCode = mapParams.get("vnp_ResponseCode")[0];
        if (vnp_ResponseCode.equals("24")) {
            orderCollection.setPaymentStatus(MyConstants.PaymentStatus.UNPAID.getValue());
            orderCollection.setUpdatedAt(LocalDateTime.now());
            orderRepo.save(orderCollection);
            throw new IllegalArgumentException("Thanh toán thất bại");
        }
        if (vnp_ResponseCode.equals("00")) {
            orderCollection.setPaymentStatus(MyConstants.PaymentStatus.PAID.getValue());
            orderCollection.setUpdatedAt(LocalDateTime.now());
            orderRepo.save(orderCollection);
            return;
        }
        orderCollection.setPaymentStatus(MyConstants.PaymentStatus.UNPAID.getValue());
        orderCollection.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(orderCollection);
        throw new IllegalArgumentException("Thanh toán thất bại");
    }

    public Map<String, String> payment(String orderId) {
        String userId = permissionService.getUserId();
        UserCollection userCollection = userRepo.findById(userId).orElse(null);
        if (userCollection == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng");
        }

        OrderCollection orderCollection = orderRepo.findById(orderId).orElseThrow(
            () -> new IllegalArgumentException("Không tìm thấy đơn hàng")
        );

        String paymentUrl = createPaymentUrl(Long.parseLong(orderCollection.getTotalBill().toString()), orderCollection.getId());
        return Map.of("redirect_url", paymentUrl);
    }
}
