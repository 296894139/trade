package jinjiang.dao.admin;

import jinjiang.entity.admin.Complain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplainDao extends JpaRepository<Complain,String> {
    Page<Complain> findByShopId(String shopId, Pageable pageable);
}
