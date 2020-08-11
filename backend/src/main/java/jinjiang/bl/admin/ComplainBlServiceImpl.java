package jinjiang.bl.admin;

import jinjiang.blservice.admin.ComplainService;
import jinjiang.blservice.admin.CultureService;
import jinjiang.dao.admin.ComplainDao;
import jinjiang.dao.admin.CultureDao;
import jinjiang.entity.admin.Complain;
import jinjiang.entity.admin.Culture;
import jinjiang.exception.NotExistException;
import jinjiang.util.FormatDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ComplainBlServiceImpl implements ComplainService {
    private final ComplainDao complainDao;

    @Autowired
    public ComplainBlServiceImpl(ComplainDao complainDao){
        this.complainDao = complainDao;
    }

    @Override
    public void addComplain(Complain culture) {
        Date date=new Date();
        String time= FormatDateTime.toLongDateString(date);
        culture.setTime(time);
        complainDao.save(culture);
    }

    @Override
    public void deleteComplain(String id) throws NotExistException {
       Optional<Complain> optionalCulture=complainDao.findById(id);
       if (optionalCulture.isPresent()){
           complainDao.deleteById(id);
       }else {
           throw new NotExistException("address ID", id);
       }
    }

    @Override
    public void updateComplain(Complain complain) throws NotExistException {
        Optional<Complain> optionalComplain=complainDao.findById(complain.getId());
        if (optionalComplain.isPresent()){
            Complain newcomplain=optionalComplain.get();
            newcomplain.setContent(complain.getContent());
            newcomplain.setTime(complain.getTime());
            newcomplain.setUsername(complain.getUsername());
            newcomplain.setShopId(complain.getShopId());
           complainDao.save(newcomplain);
        }else {
            throw new NotExistException("address ID", complain.getId());
        }
    }

    @Override
    public Complain findById(String id) throws NotExistException {
        Optional<Complain> optionalComplain=complainDao.findById(id);
        if (optionalComplain.isPresent()){
            return optionalComplain.get();
        }else {
            throw new NotExistException("address ID", id);
        }
    }

    @Override
    public Page<Complain> findAll(Pageable pageable) {
        return complainDao.findAll(pageable);
    }


    @Override
    public Page<Complain> findByShopId(String shopId,Pageable pageable) {
        return complainDao.findByShopId(shopId,pageable);
    }


    @Override
    public Page<Complain> find(String query, Pageable pageable) {
        List<Complain> complains=complainDao.findAll();
        List<Complain> list=new ArrayList<>();
        for(Complain complain:complains){
            if(complain.getContent().indexOf(query)!=(-1)||complain.getTime().indexOf(query)!=(-1)||complain.getUsername().indexOf(query)!=(-1)||complain.getId().indexOf(query)!=(-1)){
                list.add(complain);
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
