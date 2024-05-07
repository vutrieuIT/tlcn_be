package vn.id.vuductrieu.tlcn_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.id.vuductrieu.tlcn_be.dto.CartDto;
import vn.id.vuductrieu.tlcn_be.entity.CartEntity;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    @Query(value = "delete from CartEntity c where c.user_id = ?1")
    void deleteByUser_id(Integer userId);

    @Query(value = "delete from CartEntity c where c.user_id = ?1 and c.product_id = ?2 and c.variant_id = ?3")
    Optional<CartEntity> findByUser_idAndProduct_idAndVariant_id(Integer userId, Integer productId, Integer variantId);

    @Query(value = "select new vn.id.vuductrieu.tlcn_be.dto.CartDto(c.id, c.quantity, c.price, c.user_id, c.product_id, c.variant_id, p.name, v.color_type) " +
            "from CartEntity c " +
            "join ProductEntity p on c.product_id = p.id " +
            "join ProductVariationEntity v on c.variant_id = v.id " +
            "where c.user_id = ?1")
    List<CartDto> findByUser_id(Integer userId);

}
