package jinjiang.springcontroller.shop;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jinjiang.blservice.shop.ShopBlService;
import jinjiang.entity.shop.Shop;
import jinjiang.exception.NotExistException;
import jinjiang.response.Result;
import jinjiang.response.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shop")
public class ShopController {

    private final ShopBlService shopBlService;

    @Autowired
    public ShopController(ShopBlService shopBlService) {
        this.shopBlService = shopBlService;
    }

    @ApiOperation(value = "新增门店", notes = "新增门店")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Result> addUser(@Valid @RequestBody Shop shop) throws NotExistException {
        shopBlService.addShop(shop);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(), HttpStatus.OK);
    }

    @ApiOperation(value = "删除门店", notes = "删除门店")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> deleteUserById(@RequestParam("id") String id) throws NotExistException {
        shopBlService.deleteShop(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "修改门店", notes = "修改门店")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Result> updateUserById(@Valid @RequestBody Shop shop) throws NotExistException {
        shopBlService.updateShop(shop);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "根据id查找门店", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/find/id", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> getUserById(@RequestParam("id") String id) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",shopBlService.findById(id));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "根据id查找门店", notes = "")
    @RequestMapping(value = "/cal", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> cal(@RequestParam("shopId") String shopId,@RequestParam("longitude") double longitude,@RequestParam("latitude") double latitude) throws NotExistException, IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",shopBlService.cal(shopId,longitude,latitude));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "根据id查找门店", notes = "")
    @RequestMapping(value = "/find/all/wx", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> findAll(@RequestParam("longitude") double longitude,@RequestParam("latitude") double latitude) throws NotExistException, IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",shopBlService.findAllwx(longitude,latitude));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "根据id查找门店", notes = "")
    @RequestMapping(value = "/find/index", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> findIndex(@RequestParam("longitude") double longitude,@RequestParam("latitude") double latitude) throws NotExistException, IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",shopBlService.findIndex(longitude,latitude));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "所有门店", notes = "")
    @RequestMapping(value = "/find/all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> getAllUser(Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",shopBlService.findAll(pageable));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "所有门店", notes = "")
    @RequestMapping(value = "/find/query", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> find(@RequestParam("query") String query,Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",shopBlService.find(query,pageable));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }



}
