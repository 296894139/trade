package jinjiang.blservice.upload;

import org.springframework.web.multipart.MultipartFile;
import jinjiang.exception.SystemException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface UploadService {
    String upload(MultipartFile multipartFile) throws IOException, URISyntaxException;
}
