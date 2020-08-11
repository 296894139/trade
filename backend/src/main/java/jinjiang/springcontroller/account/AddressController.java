package jinjiang.springcontroller.account;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jinjiang.blservice.account.AddressBlService;
import jinjiang.entity.account.Address;
import jinjiang.exception.NotExistException;
import jinjiang.response.Result;
import jinjiang.response.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {
    private final AddressBlService addressBlService;

        @Autowired
        public AddressController(AddressBlService addressBlService) {
            this.addressBlService = addressBlService;
        }

    //ok
        @ApiOperation(value = "新增地址", notes = "新增地址")
        @RequestMapping(value = "/add", method = RequestMethod.POST)
        @ResponseBody
        public ResponseEntity<Result> addUser(@Valid @RequestBody Address address) throws NotExistException {
            Map<String, Object> result = new HashMap<>();
            result.put("items",addressBlService.addAddress(address));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }
        //ok
        @ApiOperation(value = "删除地址", notes = "删除地址")
        @RequestMapping(value = "/delete", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> deleteUserById(@RequestParam("id") String id) throws NotExistException {
            addressBlService.deleteAddress(id);
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(), HttpStatus.OK);
        }

        //接通
        @ApiOperation(value = "修改地址", notes = "修改地址")
        @RequestMapping(value = "/update", method = RequestMethod.PUT)
        @ResponseBody
        public ResponseEntity<Result> updateUserById(@Valid @RequestBody Address ddress) throws NotExistException {
            addressBlService.updateAddress(ddress);
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
        }

        //接通
        @ApiOperation(value = "根据地址id查找地址详情", notes = "")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
        })
        @RequestMapping(value = "/find/id", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> getUserById(@RequestParam("id") String id) throws NotExistException {
            Map<String, Object> result = new HashMap<>();
            result.put("items",addressBlService.findById(id));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }

    @ApiOperation(value = "根据地址id查找地址详情", notes = "")

    @RequestMapping(value = "/find/userId", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> getUserByUserId(@RequestParam("userId") String userId) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",addressBlService.findByUserId(userId));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

        //接通
        @ApiOperation(value = "所有地址", notes = "")
        @RequestMapping(value = "/find/all", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> getAllUser(Pageable pageable) {
            Map<String, Object> result = new HashMap<>();
            result.put("items",addressBlService.findAll(pageable));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }


       /* @ApiOperation(value = "修改默认地址", notes = "")
        @RequestMapping(value = "/setDefaultAddress", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> setDefaultAddress(@RequestParam("userId")String userId,@RequestParam("addressId")String addressId) throws NotExistException {
            addressBlService.setDefaultAddress(userId,addressId);
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
        }*/



}
