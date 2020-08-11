package jinjiang.dao.account;

import jinjiang.entity.account.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelDao extends JpaRepository<Level, String> {
    Page<Level> findByShopId(String shopId, Pageable pageable);
}
