package vn.id.vuductrieu.tlcn_be.constants;

import lombok.RequiredArgsConstructor;

public class Constants {

    @RequiredArgsConstructor
    public enum Role {
        ADMIN(1),
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
}
