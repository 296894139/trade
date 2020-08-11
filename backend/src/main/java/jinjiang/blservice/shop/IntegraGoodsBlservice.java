package jinjiang.blservice.shop;


import jinjiang.entity.shop.IntegralGoods;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IntegraGoodsBlservice  {
    void addIntegralGoods(IntegralGoods goods);

    void deleteIntegralGoods(String id) throws NotExistException;

    void updateIntegralGoods(IntegralGoods goods) throws NotExistException;

    Page<IntegralGoods> find(String query, Pageable pageable);

    IntegralGoods findById(String id) throws NotExistException;

    Page<IntegralGoods> findAll(Pageable pageable);
}
