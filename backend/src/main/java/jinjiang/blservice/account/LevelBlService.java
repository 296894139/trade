package jinjiang.blservice.account;

import jinjiang.entity.account.Level;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LevelBlService {

    void addLevel(Level level);

    void deleteLevel(String id) throws NotExistException;

    void updateLevel(Level level) throws NotExistException;

    Level findById(String id) throws NotExistException;

    Page<Level> findAll(Pageable pageable);

    Page<Level> findByShopId(String shopId, Pageable pageable);
}
