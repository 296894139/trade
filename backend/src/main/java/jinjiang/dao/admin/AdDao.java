package jinjiang.dao.admin;

import jinjiang.entity.admin.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdDao extends JpaRepository<Ad,String> {
    Optional<Ad> findByShowPlace(String showPlace);
}
