package com.devteria.file.service;

import com.devteria.file.dto.ApiResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

  public ApiResponse<Object> uploadFile(MultipartFile file) throws IOException {

    Path folder = Paths.get("D:\\Working\\Training\\BE\\bookteria\\file-upload");

    String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

    String fileName = Objects.isNull(fileExtension)
        ? UUID.randomUUID().toString()
        : UUID.randomUUID() + "." + fileExtension;

    Path filePath = folder.resolve(fileName).normalize().toAbsolutePath();

    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    return null;
  }
}
