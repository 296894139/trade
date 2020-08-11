package jinjiang.blservice.recommend;

import jinjiang.entity.recommend.Recommend;
import jinjiang.exception.NotExistException;
import jinjiang.response.RecommendResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecommendService {

    void addRecommend(Recommend recommend);

    void deleteRecommend(String id) throws NotExistException;

    void updateRecommend(Recommend recommend) throws NotExistException;

    Recommend findById(String id) throws NotExistException;

    Page<Recommend> findAll(Pageable pageable);

    List<RecommendResponse> findByReferrer(String referrer);

    Page<Recommend> find(String query, Pageable pageable);

    Page<Recommend> findByShopId(String shopId, Pageable pageable);
}
