package vn.actvn.onthionline.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vn.actvn.onthionline.common.Constant;
import vn.actvn.onthionline.common.ValidationError;
import vn.actvn.onthionline.common.exception.ServiceException;
import vn.actvn.onthionline.common.exception.ServiceExceptionBuilder;
import vn.actvn.onthionline.common.exception.ValidationErrorResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class ImageService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    public String saveFile(String imgBase64, String fileName, String fileTail) throws IOException {
        makeDirectoryIfNotExist();
        Path filePath = Paths.get(Constant.IMAGES_DIR_DEFAULT,
                fileName.concat(".").concat(fileTail));
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(imgBase64);
            FileUtils.writeByteArrayToFile(new File(filePath.toString()), decodedBytes);
            return filePath.toString();
        } catch (IOException e) {
            LOGGER.info("Can not save img {}", filePath.toString());
            throw e;
        }
    }

    public String getFile(String filePath) throws IOException {
        try {
            File file = new File(filePath);
            String imgBase64 = "";
            if (file.canRead()) {
                byte[] fileContent = FileUtils.readFileToByteArray(file);
                imgBase64 = Base64.getEncoder().encodeToString(fileContent);
            }

            return imgBase64;
        } catch (IOException e) {
            throw e;
        }
    }

    private void makeDirectoryIfNotExist() {
        File directory = new File(Constant.IMAGES_DIR_DEFAULT);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
