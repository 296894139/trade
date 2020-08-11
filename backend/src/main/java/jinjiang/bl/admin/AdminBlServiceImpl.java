package jinjiang.bl.admin;

import jinjiang.blservice.admin.AdminService;
import jinjiang.dao.admin.AdminDao;
import jinjiang.entity.account.User;
import jinjiang.entity.admin.Admin;
import jinjiang.entity.shop.Shop;
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
public class AdminBlServiceImpl  implements AdminService {
    private final AdminDao admindao;

    @Autowired
    public AdminBlServiceImpl(AdminDao admindaoone){
        this.admindao=admindaoone;
    }

    @Override
    public void addAdmin(Admin admin) {
        admindao.save(admin);
    }

    @Override
    public void deleteAdmin(String id) throws NotExistException {
       Optional<Admin> admin=admindao.findById(id);
       if (admin.isPresent()){
           admindao.deleteById(id);
       }else {
           throw new NotExistException("address ID", id);
       }
    }

    @Override
    public void updateAdmin(Admin admin) throws NotExistException {
        Optional<Admin> adminone=admindao.findById(admin.getId());
        if (adminone.isPresent()){
           Admin newAdmin=adminone.get();
            newAdmin.setDate(admin.getDate());
            newAdmin.setFace(admin.getFace());
            newAdmin.setLimits(admin.getLimits());
            newAdmin.setPassword(admin.getPassword());
            newAdmin.setUsername(admin.getUsername());
            admindao.save(newAdmin);
        }else {
            throw new NotExistException("address ID", admin.getId());
        }
    }

    @Override
    public Admin login(String username, String password) {
        Optional<Admin> optionalAdmin=admindao.findByUsername(username);
        if(optionalAdmin.isPresent()){
            Admin admin=optionalAdmin.get();
            if(admin.getPassword().equals(password)){
                return admin;
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }

    }

    @Override
    public Admin findById(String id) throws NotExistException {
        Optional<Admin> admin=admindao.findById(id);
        if (admin.isPresent()){
            return admin.get();
        }else {
            throw new NotExistException("address ID", id);
        }
    }

    @Override
    public Page<Admin> findAll(Pageable pageable) {
        return admindao.findAll(pageable);
    }


    @Override
    public Page<Admin> find(String query, Pageable pageable) {
        List<Admin> admins=admindao.findAll();
        List<Admin> list=new ArrayList<>();
        for(Admin admin:admins){
            if(admin.getDate().indexOf(query)!=(-1)||admin.getPassword().indexOf(query)!=(-1)||admin.getUsername().indexOf(query)!=(-1)||admin.getId().indexOf(query)!=(-1)){
                list.add(admin);
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
