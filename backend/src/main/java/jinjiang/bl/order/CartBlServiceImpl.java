package jinjiang.bl.order;

import jinjiang.blservice.order.CartBlService;
import jinjiang.dao.order.CartDao;
import jinjiang.entity.order.Cart;
import jinjiang.entity.order.Order;
import jinjiang.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartBlServiceImpl implements CartBlService {
    private final CartDao cartDao;

    @Autowired
    public CartBlServiceImpl(CartDao cartDao){
        this.cartDao = cartDao;
    }

    @Override
    public void addCart(Cart cart) {
       cartDao.save(cart);
    }

    @Override
    public void deleteCart(String id) throws NotExistException {
     Optional<Cart> optionalCart=cartDao.findById(id);
     if (optionalCart.isPresent()){
         cartDao.deleteById(id);
     }else {
         throw new NotExistException("order ID", id);
     }
    }

    @Override
    public void updateCart(Cart cart) throws NotExistException {
        Optional<Cart> optionalCart=cartDao.findById(cart.getId());
        if (optionalCart.isPresent()){
            Cart newCart= optionalCart.get();
            newCart.setGoodsId(cart.getGoodsId());
            newCart.setGoodsName(cart.getGoodsName());
            newCart.setNumber(cart.getNumber());
            newCart.setPicUrl(cart.getPicUrl());
            newCart.setPrice(cart.getPrice());
            newCart.setUserOpenid(cart.getUserOpenid());
            newCart.setChecked(cart.isChecked());
            cartDao.save(newCart);
        } else {
            throw new NotExistException("cart ID", cart.getId());
        }
    }

    @Override
    public Cart findById(String id) throws NotExistException {
        Optional<Cart> optionalCart=cartDao.findById(id);
        if (optionalCart.isPresent()){
            return optionalCart.get();
        }else {
            throw new NotExistException("order ID", id);
        }
    }

    @Override
    public Page<Cart> findAll(Pageable pageable) {

        return cartDao.findAll(pageable);
    }

    @Override
    public List<Cart> findByOpenid(String openid) throws NotExistException {
        return cartDao.findByUserOpenid(openid);
    }


}
