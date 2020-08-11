package jinjiang.blservice.shop;
import jinjiang.entity.shop.OffLine;
import jinjiang.entity.shop.Stock;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OffLineBlService {
    OffLine addOffLine(OffLine offLine);

    void deleteOffLine(String id) throws NotExistException;

    void updateOffLine(OffLine offLine) throws NotExistException;

    OffLine findById(String id) throws NotExistException;

    void pass(String id) throws NotExistException;

    List<OffLine> findByShopId(String shopId) throws NotExistException;

    Page<OffLine> findAll(Pageable pageable);

}
