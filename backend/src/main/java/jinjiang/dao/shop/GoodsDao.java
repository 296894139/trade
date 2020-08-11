package jinjiang.dao.shop;

import jinjiang.entity.shop.Goods;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GoodsDao extends JpaRepository<Goods, String> {

    Page<Goods> findAllByShopId(String ShopId, Pageable pageable) throws NotExistException;
}
