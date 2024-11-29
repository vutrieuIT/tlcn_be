package vn.id.vuductrieu.tlcn_be.constants;

import lombok.RequiredArgsConstructor;

public class MyConstants {

    @RequiredArgsConstructor
    public enum Role {
        ADMIN(2),
        EMPLOYEE(1),
        USER(0);
        private final int value;

        public int getValue() {
            return value;
        }
    }

    @RequiredArgsConstructor
    public enum Status {

        ACTIVE("active"),
        INACTIVE("inactive");
        private final String value;

        public String getValue() {
            return value;
        }
    }

    @RequiredArgsConstructor
    public enum DiscountType {
        PERCENTAGE("percentage"),
        AMOUNT("amount");
        private final String value;
        public String getValue() {
            return value;
        }
    }

    @RequiredArgsConstructor
    public enum DiscountStatus {
        ACTIVE("active"),
        INACTIVE("inactive"),
        USED("used");
        private final String value;
        public String getValue() {
            return value;
        }
    }

    @RequiredArgsConstructor
    public enum OrderStatus {
        PENDING("Chờ xác nhận"),
        CONFIRMED("Đã xác nhận"),
        SHIPPING("Đang giao hàng"),
        COMPLETED("Đã giao hàng"),
        CANCELED("Đã hủy");
        private final String value;
        public String getValue() {
            return value;
        }
    }

    @RequiredArgsConstructor
    public enum PaymentStatus {
        PAID("Đã thanh toán"),
        UNPAID("Chưa thanh toán");
        private final String value;
        public String getValue() {
            return value;
        }
    }

    @RequiredArgsConstructor
    public enum ProductStatus {
//        "0": "hết hàng",
//            "1": "sắp hêt hàng",
//            "2": "còn hàng",
//            "3": "ngừng kinh doanh"
        OUT_OF_STOCK("0"),
        RUNNING_OUT("1"),
        IN_STOCK("2"),
        STOP_SELLING("3");
        private final String value;
        public String getValue() {
            return value;
        }
    }
}
