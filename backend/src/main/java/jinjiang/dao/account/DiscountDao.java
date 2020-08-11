package jinjiang.dao.account;

import jinjiang.entity.account.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountDao extends JpaRepository<Discount,String> {
    Optional<Discount> findByGoodsType(String goodsType);
    Optional<Discount> findByGoodsTypeAndShopId(String goodsType,String shopId);
    Page<Discount> findByShopId(String shopId, Pageable pageable);
}
