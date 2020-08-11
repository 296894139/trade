package jinjiang.bl.shop;

import jinjiang.blservice.shop.ShopBlService;
import jinjiang.dao.account.DiscountDao;
import jinjiang.dao.account.LevelDao;
import jinjiang.dao.admin.DeductDao;
import jinjiang.dao.shop.ShopDao;
import jinjiang.entity.account.Discount;
import jinjiang.entity.account.Level;
import jinjiang.entity.admin.Deduct;
import jinjiang.entity.shop.Shop;
import jinjiang.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShopBlServiceImpl implements ShopBlService {
    private final ShopDao shopdao;
    private final DeductDao deductDao;
    private final DiscountDao discountDao;
    private final LevelDao levelDao;

    private static final double EARTH_RADIUS = 6371393;

    @Autowired
    public ShopBlServiceImpl(ShopDao shopdao, DeductDao deductDao, DiscountDao discountDao, LevelDao levelDao){
        this.shopdao=shopdao;
        this.deductDao = deductDao;
        this.discountDao = discountDao;
        this.levelDao = levelDao;
    }

    @Override
    public void addShop(Shop shop) {
        String id=shopdao.save(shop).getId();
        Deduct deduct=new Deduct(0.3,0.3,0.3,0.1,id);
        Discount discount=new Discount("直推奖","直推奖","直推奖",0,888,1,"1","",1000,0,100,"","",id);
        String discountid=discountDao.save(discount).getId();
        Level level=new Level("非会员",0,"","直推奖",id);
        levelDao.save(level);
        level=new Level("普通会员",10000,"","直推奖",id);
        levelDao.save(level);
        level=new Level("高级会员",50000,"","直推奖",id);
        levelDao.save(level);
        deductDao.save(deduct);
    }

    @Override
    public void deleteShop(String id) throws NotExistException {
        Optional<Shop> shop=shopdao.findById(id);
        if (shop.isPresent()){
           shopdao.deleteById(id);
        }else {
            throw new NotExistException("shop ID", id);
        }
    }

    @Override
    public void updateShop(Shop shop) throws NotExistException {
        Optional<Shop> shopone=shopdao.findById(shop.getId());
        if (shopone.isPresent()){
           Shop shopinfo=shopone.get();
            shopinfo.setBalance(shop.getBalance());
            shopinfo.setCity(shop.getCity());
            shopinfo.setDetail(shop.getDetail());
            shopinfo.setDistrict(shop.getDistrict());
            shopinfo.setFaceUrl(shop.getFaceUrl());
            shopinfo.setMobilePhone(shop.getMobilePhone());
            shopinfo.setName(shop.getName());
            shopinfo.setProvince(shop.getProvince());
            shopinfo.setShowUrl(shop.getShowUrl());
            shopinfo.setWorkTime(shop.getWorkTime());
            shopdao.save(shopinfo);
        }else {
            throw new NotExistException("shop ID", shop.getId());
        }


    }

    @Override
    public Shop findById(String id) throws NotExistException {
       Optional<Shop> shop=shopdao.findById(id);
       if (shop.isPresent()){
           return shop.get();
       }else {
           throw new NotExistException("shop ID", id);
       }

    }

    @Override
    public Page<Shop> findAll(Pageable pageable) {
        Page<Shop> shops=shopdao.findAll(pageable);
        System.out.println("shops===="+shops.getSize());
        return shops ;
    }

    @Override
    public List<Shop> findAllwx(double longitude, double latitude) throws IOException {
        List<Shop> shops=shopdao.findAll();
        for(int i=0;i<shops.size();i++){
            Shop shop=shops.get(i);
            String address=shop.getProvince()+shop.getCity()+shop.getDistrict()+shop.getDetail();
            double[] o=getCoordinate(address);
            DecimalFormat df   = new DecimalFormat("######0.00");
            double distance=Double.valueOf(df.format(getDistance(latitude,longitude,o[1],o[0])/1000));
            shop.setBalance(distance);
        }
        return shops;
    }


    @Override
    public String findIndex(double longitude, double latitude) throws IOException {
        List<Shop> shops=shopdao.findAll();
        String id="";
        for(int i=0;i<shops.size();i++){
            Shop shop=shops.get(i);
            String address=shop.getProvince()+shop.getCity()+shop.getDistrict()+shop.getDetail();
            double[] o=getCoordinate(address);
            DecimalFormat df   = new DecimalFormat("######0.00");
            double distance=Double.valueOf(df.format(getDistance(latitude,longitude,o[1],o[0])/1000));
            shop.setBalance(distance);
        }
        double min=10000000;
        for(int i=0;i<shops.size();i++){
            if(shops.get(i).getBalance()<min){
                min=shops.get(i).getBalance();
            }
        }
        for(int i=0;i<shops.size();i++){
            if(shops.get(i).getBalance()==min){
                id=shops.get(i).getId();
            }
        }
        return id;
    }

    @Override
    public Page<Shop> find(String query, Pageable pageable) {
        List<Shop> shops=shopdao.findAll();
        List<Shop> list=new ArrayList<>();
        for(Shop shop:shops){
            if(shop.getCity().indexOf(query)!=(-1)||shop.getDetail().indexOf(query)!=(-1)||shop.getDetail().indexOf(query)!=(-1)||shop.getName().indexOf(query)!=(-1)||shop.getMobilePhone().indexOf(query)!=(-1)||shop.getId().indexOf(query)!=(-1)){
                list.add(shop);
            }
        }
        return listConvertToPage(list,pageable);
    }

    @Override
    public double cal(String shopId, double longitude, double latitude) throws IOException {
        Optional<Shop> optionalShop=shopdao.findById(shopId);
        if(optionalShop.isPresent()){
            Shop shop=optionalShop.get();
            String address=shop.getProvince()+shop.getCity()+shop.getDistrict()+shop.getDetail();
            double[] o=getCoordinate(address);
            DecimalFormat df   = new DecimalFormat("######0.00");
            double distance=Double.valueOf(df.format(getDistance(latitude,longitude,o[1],o[0])/1000));
            return distance;
        }
        return 0;
    }

    public <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public static double getDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        double radiansAX = Math.toRadians(lng1); // A经弧度
        double radiansAY = Math.toRadians(lat1); // A纬弧度
        double radiansBX = Math.toRadians(lng2); // B经弧度
        double radiansBY = Math.toRadians(lat2); // B纬弧度

        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
                + Math.sin(radiansAY) * Math.sin(radiansBY);
//        System.out.println("cos = " + cos); // 值域[-1,1]
        double acos = Math.acos(cos); // 反余弦值
//        System.out.println("acos = " + acos); // 值域[0,π]
//        System.out.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]
        return EARTH_RADIUS * acos; // 最终结果
    }



    public static double[] getCoordinate(String addr) throws IOException {
        String lng = null;//经度
        String lat = null;//纬度
        String address =addr;
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        String key = "qDaws0IfG7VID98VWsDCYZvr8j6fRkGv";
        String url = String .format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s", address, key);
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
            if (httpsConn != null)
            {
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                int count = 1;
                while((data= br.readLine())!=null){
                    if(count==5){
                        lng = (String)data.subSequence(data.indexOf(":")+1, data.indexOf(","));//经度
                        count++;
                    }else if(count==6){
                        lat = data.substring(data.indexOf(":")+1);//
                        count++;
                    }
                    else{
                        count++;
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(insr!=null){
                insr.close();
            }
            if(br!=null){
                br.close();
            }
        }
        return new double[]{Double.valueOf(lng),Double.valueOf(lat)};
    }

}
