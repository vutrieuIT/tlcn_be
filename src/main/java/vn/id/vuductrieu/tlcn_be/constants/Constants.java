package vn.id.vuductrieu.tlcn_be.constants;

import lombok.RequiredArgsConstructor;

public class Constants {

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
}
