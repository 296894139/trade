package jinjiang.dao.recommend;

import jinjiang.entity.recommend.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendDao extends JpaRepository<Recommend,String> {
    Optional<Recommend> findByUser(String user);
    List<Recommend> findByReferrer(String referrer);
    List<Recommend> findByReferrerAndStatus(String referrer,boolean status);
}
