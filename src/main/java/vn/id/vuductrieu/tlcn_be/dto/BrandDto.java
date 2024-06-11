package vn.id.vuductrieu.tlcn_be.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BrandDto {
    private Integer id;
    private String name;
    private String slug;
    private Integer position;
    private String country;
    private String showHide;
}
