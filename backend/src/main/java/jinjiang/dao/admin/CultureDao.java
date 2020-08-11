package jinjiang.dao.admin;

import jinjiang.entity.admin.Culture;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

public interface CultureDao extends JpaRepository<Culture,String> {
    Page<Culture> findByType(String type, Pageable pageable);
}
