package jinjiang.bl.account;


import jinjiang.dao.account.AddressDao;
import jinjiang.dao.recommend.RecommendDao;
import jinjiang.dao.shop.ShopDao;
import jinjiang.entity.account.Address;
import jinjiang.entity.shop.Shop;
import jinjiang.response.MemberResponse;
import jinjiang.response.account.OpenIdAndSessionKeyResponse;
import jinjiang.response.account.QrCodeResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import jinjiang.blservice.account.UserBlService;
import jinjiang.dao.account.UserDao;
import jinjiang.entity.account.User;
import jinjiang.exception.NotExistException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class UserBlServiceImpl implements UserBlService {
	private final UserDao userDao;
	private final ShopDao shopDao;
	private final AddressDao addressDao;
	private final RecommendDao recommendDao;

	@Autowired
	public UserBlServiceImpl(UserDao userDao, ShopDao shopDao, AddressDao addressDao, RecommendDao recommendDao) {
		this.userDao = userDao;
		this.shopDao = shopDao;
		this.addressDao = addressDao;
		this.recommendDao = recommendDao;
	}

	@Override
	public void addUser(User user) {
		userDao.save(user);
	}

	@Override
	public void deleteUser(String id) throws NotExistException {
		Optional<User> optionalUser = userDao.findById(id);
		if (optionalUser.isPresent()) {
			userDao.deleteById(id);
		} else {
			throw new NotExistException("User ID", id);
		}
	}

	@Override
	public void updateUser(User user) throws NotExistException {
		System.out.println(user.getId());
		Optional<User> optionalUser = userDao.findById(user.getId());
		if(optionalUser.isPresent()) {
			User newUser = optionalUser.get();
			newUser.setBalance(user.getBalance());
			newUser.setBirthday(user.getBirthday());
			newUser.setDefaultAddress(user.getDefaultAddress());
			newUser.setEmail(user.getEmail());
			newUser.setFaceUrl(user.getFaceUrl());
			newUser.setIdentity(user.getIdentity());
			newUser.setIntegral(user.getIntegral());
			newUser.setLevel(user.getLevel());
			newUser.setMobilePhone(user.getMobilePhone());
			newUser.setName(user.getName());
			newUser.setOpenid(user.getOpenid());
			newUser.setRegtime(user.getRegtime());
			newUser.setRemark(user.getRemark());
			newUser.setTakeBalance(user.getTakeBalance());
			newUser.setUsername(user.getUsername());
			newUser.setShopId(user.getShopId());
			newUser.setShareholderId(user.getShareholderId());
			newUser.setInvest(user.getInvest());
			userDao.save(newUser);
		} else {
			throw new NotExistException("User ID", user.getId());
		}
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		return userDao.findAll(pageable);
	}

	@Override
	public Page<User> findByIdentity(String identity,Pageable pageable) {
		return userDao.findByIdentity(identity,pageable);
	}

	@Override
	public Page<User> find(String identity, String query, Pageable pageable) {
		List<User> userList=userDao.findByIdentity(identity);
		List<User> list=new ArrayList<>();
		for(User user:userList){
			String shopName="***";
			String shareholderName="***";
			Optional<Shop> shop=shopDao.findById(user.getShopId());
			Optional<User> shareholder=userDao.findById(user.getShareholderId());
			if(shop.isPresent()){
				shopName=shop.get().getName();
			}
			if(shareholder.isPresent()){
				shareholderName=shareholder.get().getName();
			}
			if(user.getLevel().indexOf(query)!=(-1)||user.getMobilePhone().indexOf(query)!=(-1)||user.getEmail().indexOf(query)!=(-1)||user.getOpenid().indexOf(query)!=(-1)||user.getName().indexOf(query)!=(-1)||shareholderName.indexOf(query)!=(-1)||shopName.indexOf(query)!=(-1)||user.getUsername().indexOf(query)!=(-1)||user.getId().indexOf(query)!=(-1)){
				list.add(user);
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
	public Page<User> findByShopId(String shopId,Pageable pageable) {
		return userDao.findByShopId(shopId,pageable);
	}

	@Override
	public List<User> findByShareholderId(String shareholderId) {
		return userDao.findByShareholderId(shareholderId);
	}


	@Override
	public Page<User> findIdentityAndShop(String identity,String shopId,Pageable pageable) {
		return userDao.findByIdentityAndShopId(identity,shopId,pageable);
	}

	@Override
	public Page<User> findIdentityAndShopAndLevel(String identity,String shopId,String levelId,Pageable pageable) {
		return userDao.findByIdentityAndShopIdAndLevel(identity,shopId,levelId,pageable);
	}


	@Override
	public List<MemberResponse> findByShareholderIdwx(String shareholderId) {
		List<User> users=userDao.findByShareholderId(shareholderId);
		List<MemberResponse> memberResponses=new ArrayList<>();
		for(User user:users){
			int recommend=0;
			recommend=recommendDao.findByReferrerAndStatus(user.getId(),true).size();
			MemberResponse memberResponse=new MemberResponse(user.getFaceUrl(),user.getUsername(),user.getMobilePhone(),user.getLevel(),recommend,user.getRegtime());
			memberResponses.add(memberResponse);
		}
		return memberResponses;
	}


	@Override
	public User findById(String id) throws NotExistException {
		Optional<User> optionalUser = userDao.findById(id);
		if(optionalUser.isPresent()) {
			return optionalUser.get();
		}else {
			throw new NotExistException("User ID", id);
		}
	}


	@Override
	public User findByOpenid(String openid) throws NotExistException {
		Optional<User> optionalUser = userDao.findByOpenid(openid);
		if(optionalUser.isPresent()) {
			return optionalUser.get();
		}else {
			throw new NotExistException("User ID", openid);
		}
	}

	@Override
	public void setDefaultAddress(String userId, String addressId) throws NotExistException {
		Optional<User> optionalUser=userDao.findById(userId);
		if(optionalUser.isPresent()){
            User lastUser=optionalUser.get();
            lastUser.setDefaultAddress(addressId);
            userDao.save(lastUser);
		}
		else {
			throw new NotExistException("User ID", userId);
		}
		Optional<Address> optionalAddress=addressDao.findById(addressId);
		if(optionalAddress.isPresent()){
			Address address=optionalAddress.get();
			address.setDefault(true);
			addressDao.save(address);
		}
		List<Address> addresses=addressDao.findByUserId(userId);
		for(Address address:addresses){
			if(!address.getId().equals(addressId)){
				address.setDefault(false);
				addressDao.save(address);
			}
		}
	}

	@Value(value = "${wechat.url}")
	private String wechatUrl;

	@Value(value = "${wechat.id}")
	private String appId;

	@Value(value = "${wechat.secret}")
	private String appSecret;

	@Override
	public OpenIdAndSessionKeyResponse getOpenIdAndSessionKey(String jsCode) {
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<>("", headers);
		ResponseEntity<String> response = client.exchange(wechatUrl + appId + "&secret=" + appSecret + "&js_code=" + jsCode + "&grant_type=authorization_code", HttpMethod.GET, entity, String.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			String openid = (String) JSONObject.fromObject(response.getBody()).get("openid");
//            User user=null;
//			try {
//				user = userDataService.getUserByOpenid(openid);
//			} catch (NotExistException e) {
//				e.printStackTrace();
//			}

			//JwtUser jwtUser = (JwtUser) jwtUserDetailsService.loadUserByUsername(openid);
			String token = "";
			//token = jwtService.generateToken(jwtUser, EXPIRATION);

			return new OpenIdAndSessionKeyResponse(openid, (String) JSONObject.fromObject(response.getBody()).get("session_key"), token);
		} else {
			return new OpenIdAndSessionKeyResponse("", (String) JSONObject.fromObject(response.getBody()).get("session_key"), "");
		}
	}

    @Override
    public User loginMyUser(String openid, String username, String faceWxUrl)  {
	    Optional<User> optionalUser=userDao.findByOpenid(openid);
	    if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        else{
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(currentTime);
	        User user=new User(username,openid,"","","",faceWxUrl,"member","","","","非会员",0,0,0,dateString,"","",0);
	        userDao.save(user);
	        return user;
        }
    }

	@Override
	public QrCodeResponse getWxQrCode(String scene, String page, int width, boolean autoColor, String lineColorR, String lineColorG, String lineColorB, boolean isHyaline) {
		RestTemplate client = new RestTemplate();

		//获取accessToken
		String accessToken = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<>("", headers);
		ResponseEntity<String> response = client.exchange(
				"https://api.weixin.qq.com/cgi-bin/token?" + "&grant_type=client_credential&appid=" + appId + "&secret=" + appSecret, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			accessToken = (String) JSONObject.fromObject(response.getBody()).get("access_token");
		} else {
			System.err.println(response);
			return new QrCodeResponse(false, "access_token获取失败(" + response + ")", "");
		}

		//根据accessToken获取二维码图片
		String wxQrCodeUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
		Map<String, Object> wxQrCodeParams = new HashMap<>();
		wxQrCodeParams.put("scene", scene);
		wxQrCodeParams.put("page", page);
		wxQrCodeParams.put("width", width);
		wxQrCodeParams.put("auto_color", autoColor);
		Map<String, Object> lineColor = new HashMap<>();
		lineColor.put("r", lineColorR);
		lineColor.put("g", lineColorG);
		lineColor.put("b", lineColorB);
		wxQrCodeParams.put("line_color", lineColor);
		wxQrCodeParams.put("is_hyaline", isHyaline);
		MultiValueMap<String, String> wxQrCodeHeaders = new LinkedMultiValueMap<>();
		HttpEntity wxQrCodeRequest = new HttpEntity(wxQrCodeParams, wxQrCodeHeaders);
		ResponseEntity<byte[]> wxQrCodeResponse = client.exchange(wxQrCodeUrl, HttpMethod.POST, wxQrCodeRequest, byte[].class);
		if (wxQrCodeResponse.getStatusCode() == HttpStatus.OK) {
			byte[] image = wxQrCodeResponse.getBody();
			final String dirPath = "record/user/";
			File dirFile = new File(dirPath);
			if (!dirFile.exists() && !dirFile.mkdirs()) {
				return new QrCodeResponse(false, "二维码存储目录创建失败", "");
			}
			String imagePath = null;
			try {
				imagePath = dirPath + UUID.randomUUID();
				File imageFile = new File(imagePath);
				while (!imageFile.createNewFile()) { //若文件已存在，则换个名字
					imagePath = dirPath + UUID.randomUUID();
					imageFile = new File(imagePath);
				}
				InputStream inputStream = new ByteArrayInputStream(image);
				OutputStream outputStream = new FileOutputStream(imageFile);
				int len = 0;
				byte[] buf = new byte[1024];
				while ((len = inputStream.read(buf, 0, 1024)) != -1) {
					outputStream.write(buf, 0, len);
				}
				outputStream.flush();

				//1分钟后删除此图片
				final File finalImageFile = new File(imagePath);
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						if (!finalImageFile.delete()) {
							System.err.println(finalImageFile.getName() + "文件删除失败");
						}
					}
				}, 60 * 1000);

				return new QrCodeResponse(true, "ok", imagePath);
			} catch (IOException e) {
				System.err.println("二维码图片保存时出现错误！");
				e.printStackTrace();
				return new QrCodeResponse(false, "二维码保存失败", "");
			}
		} else {
			System.err.println(wxQrCodeResponse);
			return new QrCodeResponse(false, "二维码获取失败", "");
		}
	}

}
