package jinjiang.springcontroller.upload;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jinjiang.blservice.upload.UploadService;
import jinjiang.response.Result;
import jinjiang.response.ResultGenerator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadController {
    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @ApiOperation(value = "上传文件", notes = "上传文件")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<Result> upload(@RequestParam("file") MultipartFile file) throws IOException, URISyntaxException {
        String filePath=uploadService.upload(file);
        Map<String, Object> result = new HashMap<>();
        result.put("filePath",filePath);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result), HttpStatus.OK);
    }
}
