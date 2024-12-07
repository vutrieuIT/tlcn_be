package vn.id.vuductrieu.tlcn_be.entity.document;

import lombok.Data;

import java.util.List;

@Data
public class VariantDocument {
    private String color;

    private List<String> images;
}
