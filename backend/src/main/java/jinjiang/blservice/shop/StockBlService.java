package jinjiang.blservice.shop;
import jinjiang.entity.shop.Stock;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockBlService {
    Stock addStock(Stock shopBalance);

    void deleteStock(String id) throws NotExistException;

    void updateStock(Stock shopBalance) throws NotExistException;

    Stock findById(String id) throws NotExistException;

    void send(String id) throws NotExistException;

    void take(String id) throws NotExistException;

    void refund(String id) throws NotExistException;

    void back(String id) throws NotExistException;

    void grounding(String id) throws NotExistException;

    List<Stock> findByTypeAndShopId(String type, String shopId) throws NotExistException;

    Page<Stock> findByShopId(String shopId, Pageable pageable);

    Page<Stock> findAll(Pageable pageable);

}
