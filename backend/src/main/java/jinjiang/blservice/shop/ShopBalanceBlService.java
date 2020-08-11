package jinjiang.blservice.shop;

import jinjiang.entity.shop.ShopBalance;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShopBalanceBlService {
    ShopBalance addShopBalance(ShopBalance shopBalance);

    void deleteShopBalance(String id) throws NotExistException;

    void updateShopBalance(ShopBalance shopBalance) throws NotExistException;

    ShopBalance findById(String id) throws NotExistException;

    void pass(String id) throws NotExistException;

    List<ShopBalance> findByTypeAndShopId(String type, String shopId) throws NotExistException;

    List<ShopBalance> findByType(String type);

    Page<ShopBalance> findAll(Pageable pageable);

}
