package vn.id.vuductrieu.tlcn_be.entity.mongodb.document;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class SpecificationDocument {
    private Integer internalMemory;
    private Integer price;
    private List<ColorVariant> colorVariant;


    @Data
    private static class ColorVariant {
        private String color;
        private Integer quantity;
    }

    public Integer sumQuantity() {
        return colorVariant.stream().mapToInt(ColorVariant::getQuantity).sum();
    }
}
