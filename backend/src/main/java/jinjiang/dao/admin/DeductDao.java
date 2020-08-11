package jinjiang.dao.admin;

import jinjiang.entity.admin.Deduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeductDao extends JpaRepository<Deduct,String> {
    Optional<Deduct> findByShopId(String shopId);

}
