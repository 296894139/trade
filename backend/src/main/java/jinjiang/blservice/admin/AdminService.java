package jinjiang.blservice.admin;

import jinjiang.entity.account.User;
import jinjiang.entity.admin.Admin;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    void addAdmin(Admin admin);

    void deleteAdmin(String id) throws NotExistException;

    void updateAdmin(Admin admin) throws NotExistException;

    Admin login(String username,String password);

    Admin findById(String id) throws NotExistException;

    Page<Admin> findAll(Pageable pageable);

    Page<Admin> find(String query, Pageable pageable);
}
