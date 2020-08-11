package jinjiang.bl.admin;

import jinjiang.blservice.admin.CultureService;
import jinjiang.dao.admin.CultureDao;
import jinjiang.entity.admin.Admin;
import jinjiang.entity.admin.Culture;
import jinjiang.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CultureBlServiceImpl implements CultureService {
    private final CultureDao cultureDao;

    @Autowired
    public CultureBlServiceImpl(CultureDao cultureDao){

        this.cultureDao = cultureDao;
    }

    @Override
    public void addCulture(Culture culture) {
         cultureDao.save(culture);
    }

    @Override
    public void deleteCulture(String id) throws NotExistException {
       Optional<Culture> optionalCulture=cultureDao.findById(id);
       if (optionalCulture.isPresent()){
           cultureDao.deleteById(id);
       }else {
           throw new NotExistException("address ID", id);
       }
    }

    @Override
    public void updateCulture(Culture culture) throws NotExistException {
        Optional<Culture> optionalCulture=cultureDao.findById(culture.getId());
        if (optionalCulture.isPresent()){
           Culture newCulture=optionalCulture.get();
           newCulture.setBrief(culture.getBrief());
           newCulture.setDetail(culture.getDetail());
           newCulture.setImage(culture.getImage());
           newCulture.setTitle(culture.getTitle());
           newCulture.setTime(culture.getTime());
           newCulture.setType(culture.getType());
           cultureDao.save(newCulture);
        }else {
            throw new NotExistException("address ID", culture.getId());
        }
    }

    @Override
    public Culture findById(String id) throws NotExistException {
        Optional<Culture> optionalCulture=cultureDao.findById(id);
        if (optionalCulture.isPresent()){
            return optionalCulture.get();
        }else {
            throw new NotExistException("address ID", id);
        }
    }

    @Override
    public Page<Culture> findByType(String type,Pageable pageable){
        return cultureDao.findByType(type,pageable);
    }

    @Override
    public Page<Culture> findAll(Pageable pageable) {
        return cultureDao.findAll(pageable);
    }

    @Override
    public Page<Culture> find(String query, Pageable pageable) {
        List<Culture> cultures=cultureDao.findAll();
        List<Culture> list=new ArrayList<>();
        for(Culture culture:cultures){
            if(culture.getBrief().indexOf(query)!=(-1)||culture.getDetail().indexOf(query)!=(-1)||culture.getTime().indexOf(query)!=(-1)||culture.getId().indexOf(query)!=(-1)||culture.getTitle().indexOf(query)!=(-1)){
                list.add(culture);
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
