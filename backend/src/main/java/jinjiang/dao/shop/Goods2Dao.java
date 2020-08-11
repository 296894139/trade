package jinjiang.dao.shop;

import jinjiang.entity.shop.Goods2;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface Goods2Dao extends JpaRepository<Goods2, String> {

    Page<Goods2> findAllByShopId(String ShopId, Pageable pageable) throws NotExistException;
}
