package jinjiang.bl.shop;

import jinjiang.blservice.shop.StockBlService;
import jinjiang.dao.shop.*;
import jinjiang.entity.shop.Goods;
import jinjiang.entity.shop.Goods2;
import jinjiang.entity.shop.ShopBalance;
import jinjiang.entity.shop.Stock;
import jinjiang.exception.NotExistException;
import jinjiang.util.FormatDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StockBlServiceImpl implements StockBlService {

    private final StockDao stockDao;
    private final GoodsDao goodsDao;
    private final Goods2Dao goods2Dao;
    private final ShopBalanceDao shopBalanceDao;
    @Autowired
    public StockBlServiceImpl(StockDao stockDao, GoodsDao goodsDao, Goods2Dao goods2Dao, ShopBalanceDao shopBalanceDao) {

        this.stockDao = stockDao;
        this.goodsDao = goodsDao;
        this.goods2Dao = goods2Dao;
        this.shopBalanceDao = shopBalanceDao;
    }

    @Override
    public Stock addStock(Stock stock) {
        stock.setTime(FormatDateTime.toLongDateString(new Date()));
        return stockDao.save(stock);
    }

    @Override
    public void deleteStock(String id) throws NotExistException {
        Optional<Stock> optionalBalance = stockDao.findById(id);
        if (optionalBalance.isPresent()) {
            stockDao.deleteById(id);
        } else {
            throw new NotExistException("Address ID", id);
        }
    }

    @Override
    public void updateStock(Stock stock) throws NotExistException {
        Optional<Stock> optionalStock = stockDao.findById(stock.getId());
        if (optionalStock.isPresent()){
            Stock newStock=optionalStock.get();
            newStock.setGoodsId(stock.getGoodsId());
            newStock.setGoodsName(stock.getGoodsName());
            newStock.setImageUrl(stock.getImageUrl());
            newStock.setName(stock.getName());
            newStock.setNumber(stock.getNumber());
            newStock.setPrice(stock.getPrice());
            newStock.setStatus(stock.getStatus());
            newStock.setShopId(stock.getShopId());
            newStock.setTime(stock.getTime());
            stockDao.save(newStock);
        }else {
            throw new NotExistException("address ID", stock.getId());
        }

    }

    @Override
    public Stock findById(String id) throws NotExistException {
        return stockDao.findById(id).get();
    }

    @Override
    public void send(String id) throws NotExistException {
        Optional<Stock> optionalStock = stockDao.findById(id);
        if (optionalStock.isPresent()){
            Stock newStock=optionalStock.get();
            newStock.setStatus("待收货");
            stockDao.save(newStock);
        }else {
            throw new NotExistException("address ID", id);
        }
    }

    @Override
    public void take(String id) throws NotExistException {
        Optional<Stock> optionalStock = stockDao.findById(id);
        if (optionalStock.isPresent()){
            Stock newStock=optionalStock.get();
            newStock.setStatus("待上架");
            stockDao.save(newStock);
            List<String> goodsList=new ArrayList<>();
            goodsList.add(newStock.getGoodsId());
            ShopBalance shopBalance=new ShopBalance(newStock.getShopId(),newStock.getName(),"支出","",newStock.getPrice(),"进货："+newStock.getGoodsName(),FormatDateTime.toLongDateString(new Date()),goodsList);
            shopBalanceDao.save(shopBalance);
        }else {
            throw new NotExistException("address ID", id);
        }
    }

    @Override
    public void refund(String id) throws NotExistException {
        Optional<Stock> optionalStock = stockDao.findById(id);
        if (optionalStock.isPresent()){
            Stock newStock=optionalStock.get();
            newStock.setStatus("退款中");
            stockDao.save(newStock);
        }else {
            throw new NotExistException("address ID", id);
        }
    }

    @Override
    public void back(String id) throws NotExistException {
        Optional<Stock> optionalStock = stockDao.findById(id);
        if (optionalStock.isPresent()){
            Stock newStock=optionalStock.get();
            newStock.setStatus("已退款");
            stockDao.save(newStock);
            List<String> goodsList=new ArrayList<>();
            goodsList.add(newStock.getGoodsId());
            ShopBalance shopBalance=new ShopBalance(newStock.getShopId(),newStock.getName(),"收入","",newStock.getPrice(),"进货退款："+newStock.getGoodsName(),FormatDateTime.toLongDateString(new Date()),goodsList);
            shopBalanceDao.save(shopBalance);
        }else {
            throw new NotExistException("address ID", id);
        }
    }

    @Override
    public void grounding(String id) throws NotExistException {
        Optional<Stock> optionalStock = stockDao.findById(id);
        if (optionalStock.isPresent()){
            Stock newStock=optionalStock.get();
            newStock.setStatus("已上架");
            stockDao.save(newStock);
            Optional<Goods> optionalGoods=goodsDao.findById(newStock.getGoodsId());
            if(optionalGoods.isPresent()){
                Goods goods=optionalGoods.get();
                goods.setNumber(goods.getNumber()+newStock.getNumber());
                goodsDao.save(goods);
            }
            else{
                Optional<Goods2> optionalGoods2=goods2Dao.findById(newStock.getGoodsId());
                if(optionalGoods2.isPresent()){
                    Goods2 goods2=optionalGoods2.get();
                    goods2.setNumber(goods2.getNumber()+newStock.getNumber());
                    goods2Dao.save(goods2);
                }
            }
        }else {
            throw new NotExistException("address ID", id);
        }
    }


    @Override
    public List<Stock> findByTypeAndShopId(String status, String shopId){
        return stockDao.findByStatusAndShopId(status,shopId);
    }

    @Override
    public Page<Stock> findByShopId(String shopId,Pageable pageable){
        return stockDao.findByShopId(shopId,pageable);
    }

    @Override
    public Page<Stock> findAll(Pageable pageable) {
        return stockDao.findAll(pageable);
    }
}
