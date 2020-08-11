package jinjiang.bl.account;

import jinjiang.blservice.account.LevelBlService;
import jinjiang.dao.account.LevelDao;
import jinjiang.entity.account.Level;
import jinjiang.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LevelBlserviceImpl implements LevelBlService {
    private final LevelDao levelDao;

    @Autowired
    public LevelBlserviceImpl(LevelDao levelDao) {
        this.levelDao = levelDao;
    }


    @Override
    public void addLevel(Level level) {
        levelDao.save(level);
    }

    @Override
    public void deleteLevel(String id) throws NotExistException {
      Optional<Level> levelInfo=levelDao.findById(id);
      if (levelInfo.isPresent()){
          levelDao.deleteById(id);
      }else {
          throw new NotExistException("level ID", id);
      }
    }

    @Override
    public void updateLevel(Level level) throws NotExistException {
        Optional<Level> levelInfo=levelDao.findById(level.getId());
        if (levelInfo.isPresent()){
            levelInfo.get().setDiscount(level.getDiscount());
            levelInfo.get().setDiscountId(level.getDiscountId());
            levelInfo.get().setName(level.getName());
            levelInfo.get().setUrl(level.getUrl());
            levelInfo.get().setShopId(level.getShopId());
            levelDao.save(levelInfo.get());
        }else {
            throw new NotExistException("level ID", level.getId());
        }
    }

    @Override
    public Level findById(String id) throws NotExistException {
        Optional<Level> levelInfo=levelDao.findById(id);
        if (levelInfo.isPresent()){
            return levelInfo.get();
        }else {
            throw new NotExistException("level ID",id);
        }
    }

    @Override
    public Page<Level> findAll(Pageable pageable) {
        return levelDao.findAll(pageable);
    }

    @Override
    public Page<Level> findByShopId(String shopId,Pageable pageable) {
        return levelDao.findByShopId(shopId,pageable);
    }
}
