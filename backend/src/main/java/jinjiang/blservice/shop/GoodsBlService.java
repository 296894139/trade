package jinjiang.blservice.shop;


import jinjiang.entity.shop.Goods;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GoodsBlService  {
    void addGoods(Goods goods);

    void deleteGoods(String id) throws NotExistException;

    void updateGoods(Goods goods) throws NotExistException;

    Goods findById(String id) throws NotExistException;

    Page<Goods> findAll(Pageable pageable);

    Page<Goods> find(String query,Pageable pageable);

    Page<Goods> findGoodsByShopId(String id, Pageable pageable) throws NotExistException;
}
