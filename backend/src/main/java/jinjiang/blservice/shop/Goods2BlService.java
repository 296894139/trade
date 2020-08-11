package jinjiang.blservice.shop;



import jinjiang.entity.shop.Goods2;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Goods2BlService {
    void addGoods(Goods2 goods);

    void deleteGoods(String id) throws NotExistException;

    void updateGoods(Goods2 goods) throws NotExistException;

    Goods2 findById(String id) throws NotExistException;

    Page<Goods2> findAll(Pageable pageable);

    Page<Goods2> find(String query, Pageable pageable);

    Page<Goods2> findGoodsByShopId(String id, Pageable pageable) throws NotExistException;



}
