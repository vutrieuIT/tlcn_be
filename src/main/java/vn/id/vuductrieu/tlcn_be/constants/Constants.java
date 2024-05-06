package vn.id.vuductrieu.tlcn_be.constants;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class Constants {

    @RequiredArgsConstructor
    public enum Role {
        ADMIN(0),
        USER(1);
        private final int value;
    }
}
