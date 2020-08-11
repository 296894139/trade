package jinjiang.dao.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import jinjiang.entity.account.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, String> {
    Page<User> findByIdentity(String identity, Pageable pageable);
    List<User> findByIdentity(String identity);
    Page<User> findByShopId(String ShopId, Pageable pageable);
    Page<User> findByIdentityAndShopId(String identity, String ShopId, Pageable pageable);
    Page<User> findByIdentityAndShopIdAndLevel(String identity, String ShopId,String levelId, Pageable pageable);
    List<User> findByIdentityAndShopId(String identity, String ShopId);
    Optional<User> findByOpenid(String openid);
    List<User> findByShareholderId(String shareholderId);
    Optional<User> findByMobilePhone(String mobilePhone);
}
