package jinjiang.dao.admin;

import jinjiang.entity.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminDao extends JpaRepository<Admin,String> {
    Optional<Admin> findByUsername(String username);
}
