package jinjiang.bl.admin;

import jinjiang.blservice.recommend.RecommendService;
import jinjiang.dao.account.UserDao;
import jinjiang.dao.recommend.RecommendDao;
import jinjiang.entity.account.User;
import jinjiang.entity.recommend.Recommend;
import jinjiang.exception.NotExistException;
import jinjiang.response.RecommendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendBlServiceImpl implements RecommendService {
    private final RecommendDao recommendDao;
    private final UserDao userDao;

    @Autowired
    public RecommendBlServiceImpl(RecommendDao recommendDao, UserDao userDao){

        this.recommendDao = recommendDao;
        this.userDao = userDao;
    }

    @Override
    public void addRecommend(Recommend recommend) {
        Optional<User> tempr=userDao.findByOpenid(recommend.getReferrer());
        recommend.setReferrer(tempr.get().getId());
        Optional<User> optionalUser=userDao.findById(recommend.getUser());
        Optional<User> referrer=userDao.findById(recommend.getReferrer());
        if(optionalUser.isPresent()){
            User user=optionalUser.get();
            if(user.getIdentity().equals("member")){
                Optional<Recommend> optionalRecommend=recommendDao.findByUser(recommend.getUser());
                if(!optionalRecommend.isPresent()){
                    if(user.getShareholderId().equals("")){
                        if(user.getRemark().equals("")){
                            if(referrer.isPresent()){
                                User r=referrer.get();
                                user.setShopId(r.getShopId());
                                if(r.getIdentity().equals("shareholder")){
                                    user.setShareholderId(r.getId());
                                    userDao.save(user);
                                }
                                else if(r.getIdentity().equals("staff")){
                                    user.setRemark(r.getRemark());
                                    userDao.save(user);
                                }
                                else if(r.getIdentity().equals("member")){
                                    recommendDao.save(recommend);
                                    if(r.getShareholderId().equals("")){

                                    }
                                    else{
                                        user.setShareholderId(r.getShareholderId());
                                        userDao.save(user);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void deleteRecommend(String id) throws NotExistException {
       Optional<Recommend> optionalRecommend=recommendDao.findById(id);
       if (optionalRecommend.isPresent()){
           recommendDao.deleteById(id);
       }else {
           throw new NotExistException("address ID", id);
       }
    }

    @Override
    public void updateRecommend(Recommend recommend) throws NotExistException {
        Optional<Recommend> optionalRecommend=recommendDao.findById(recommend.getId());
        if (optionalRecommend.isPresent()){
           Recommend newRecommend=optionalRecommend.get();
           newRecommend.setStatus(recommend.isStatus());
           newRecommend.setReferrer(recommend.getReferrer());
           newRecommend.setUser(recommend.getUser());
           recommendDao.save(newRecommend);
        }else {
            throw new NotExistException("address ID", recommend.getId());
        }
    }

    @Override
    public Recommend findById(String id) throws NotExistException {
        Optional<Recommend> optionalRecommend=recommendDao.findById(id);
        if (optionalRecommend.isPresent()){
            return optionalRecommend.get();
        }else {
            throw new NotExistException("address ID", id);
        }
    }



    @Override
    public Page<Recommend> findAll(Pageable pageable) {
        return recommendDao.findAll(pageable);
    }


    @Override
    public List<RecommendResponse> findByReferrer(String referrer) {
        List<Recommend> recommends=recommendDao.findByReferrer(referrer);
        List<RecommendResponse> recommendResponses=new ArrayList<>();
        for(Recommend recommend:recommends){
            User user=userDao.findById(recommend.getReferrer()).get();
            String status="";
            if(recommend.isStatus()){
                status="成功";
            }
            else{
                status="未成功";
            }
            RecommendResponse recommendResponse=new RecommendResponse(user.getUsername(),status);
            recommendResponses.add(recommendResponse);
        }
        return recommendResponses;
    }


    @Override
    public Page<Recommend> find(String query, Pageable pageable) {
        List<Recommend> recommends=recommendDao.findAll();
        List<Recommend> list=new ArrayList<>();
        for(Recommend recommend:recommends){
            if(recommend.getReferrer().indexOf(query)!=(-1)||recommend.getUser().indexOf(query)!=(-1)||recommend.getId().indexOf(query)!=(-1)){
                list.add(recommend);
            }
        }
        return listConvertToPage(list,pageable);
    }

    @Override
    public Page<Recommend> findByShopId(String shopId, Pageable pageable) {
        List<Recommend> recommends=recommendDao.findAll();
        List<Recommend> list=new ArrayList<>();
        for(Recommend recommend:recommends){
            String userId=recommend.getReferrer();
            Optional<User> user=userDao.findById(userId);
            if(user.isPresent()){
                String id=user.get().getShopId();
                if(id.equals(shopId)){
                    list.add(recommend);
                }
            }
        }
        return listConvertToPage(list,pageable);
    }

    public <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

}
