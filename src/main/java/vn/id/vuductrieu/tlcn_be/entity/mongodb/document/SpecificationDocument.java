package vn.id.vuductrieu.tlcn_be.entity.mongodb.document;

import lombok.Data;

import java.util.List;

@Data
public class SpecificationDocument {
    private Integer internalMemory;
    private Integer price;
    private List<String> color;
}