package jinjiang.bl.shop;

import jinjiang.blservice.shop.GoodsBlService;
import jinjiang.dao.shop.Goods2Dao;
import jinjiang.dao.shop.GoodsDao;
import jinjiang.dao.shop.ShopDao;
import jinjiang.entity.shop.Goods;
import jinjiang.entity.shop.Shop;
import jinjiang.exception.NotExistException;
import jinjiang.util.FormatDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GoodsBlServiceImpl implements GoodsBlService{
    private final GoodsDao goodsDao;
    private final Goods2Dao goods2Dao;
    private final ShopDao shopdao;


    @Autowired
    public GoodsBlServiceImpl(GoodsDao goods, Goods2Dao goods2Dao, ShopDao shopdao){
        this.goodsDao=goods;
        this.goods2Dao = goods2Dao;
        this.shopdao=shopdao;
    }

    @Override
    public void addGoods(Goods goods) {
        goods.setDiscountId(FormatDateTime.toShortDateString());
        goodsDao.save(goods);
    }

    @Override
    public void deleteGoods(String id) throws NotExistException {
       Optional<Goods> goods=goodsDao.findById(id);
       if (goods.isPresent()){
           goodsDao.deleteById(id);
       }
    }

    @Override
    public void updateGoods(Goods goods) throws NotExistException {
        Optional<Goods> good=goodsDao.findById(goods.getId());
        if (good.isPresent()){
           Goods goodsone=good.get();
            goodsone.setDiscountId(goods.getDiscountId());
            goodsone.setDetail(goods.getDetail());
            goodsone.setFreight(goods.getFreight());
            goodsone.setImageUrl(goods.getImageUrl());
            goodsone.setMemberPrice(goods.getMemberPrice());
            goodsone.setName(goods.getName());
            goodsone.setNumber(goods.getNumber());
            goodsone.setPrice(goods.getPrice());
            goodsone.setSales(goods.getSales());
            goodsone.setShopId(goods.getShopId());
            goodsone.setStockPrice(goods.getStockPrice());
            goodsone.setSwiperImgs(goods.getSwiperImgs());
            goodsone.setBrief(goods.getBrief());
            goodsone.setStandard(goods.getStandard());
            goodsDao.save(goodsone);
        }else {
            throw new NotExistException("Goods ID", goods.getId());
        }
    }


    @Override
    public Goods findById(String id) throws NotExistException {
        Optional<Goods> goods=goodsDao.findById(id);
        if (goods.isPresent()){
            return goods.get();
        }else {
            throw new NotExistException("Goods ID", id);
        }

    }

    @Override
    public Page<Goods> findAll(Pageable pageable) {
        return goodsDao.findAll(pageable);
    }

    @Override
    public Page<Goods> find(String query, Pageable pageable) {
        List<Goods> goodsList=goodsDao.findAll();
        List<Goods> list=new ArrayList<>();
        for(Goods goods:goodsList){
            String shopName="***";
            Optional<Shop> shop=shopdao.findById(goods.getShopId());

            if(shop.isPresent()){
                shopName=shop.get().getName();
            }
            if(goods.getName().indexOf(query)!=(-1)||goods.getId().indexOf(query)!=(-1)||shopName.indexOf(query)!=(-1)){
                list.add(goods);
            }
        }
        return listConvertToPage(list,pageable);

    }

    public <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    @Override
    public Page<Goods> findGoodsByShopId(String id,Pageable pageable)throws NotExistException {
        //验证shop是否存在，存在再验证下面是否有商品是在属于商家；
        Page<Goods> goods=goodsDao.findAllByShopId(id,pageable);
        return goods;
    }


}
