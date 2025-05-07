package com.devteria.file.service;

import com.devteria.file.dto.response.FileData;
import com.devteria.file.dto.response.FileResponse;
import com.devteria.file.entity.File;
import com.devteria.file.exception.AppException;
import com.devteria.file.exception.ErrorCode;
import com.devteria.file.repository.FileRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class FileService {

  private final FileRepository fileRepository;

  @Value("${app.file.storage-directory}")
  private String storageDirectory;

  @Value("${app.file.prefix-download-url}")
  private String prefixDownloadUrl;

  public FileResponse uploadFile(MultipartFile file) throws IOException {

    // Write file to disk
    Path folder = Paths.get(storageDirectory);

    String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

    String fileName = Objects.isNull(fileExtension)
        ? UUID.randomUUID().toString()
        : UUID.randomUUID() + "." + fileExtension;

    Path filePath = folder.resolve(fileName).normalize().toAbsolutePath();

    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    // Save file url to db
    String userId = SecurityContextHolder.getContext().getAuthentication().getName();

    File savedFile = File.builder()
        .id(fileName)
        .contentType(file.getContentType())
        .ownerId(userId)
        .size(file.getSize())
        .md5Checksum(DigestUtils.md5DigestAsHex(file.getInputStream()))
        .path(String.valueOf(filePath))
        .build();

    fileRepository.save(savedFile);

    return FileResponse.builder()
        .originalFileName(file.getOriginalFilename())
        .downloadUrl(prefixDownloadUrl + fileName)
        .build();
  }

  public FileData downloadFile(String fileName) throws IOException {
    File file = fileRepository.findById(fileName)
        .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));

    // Read file from disk
    byte[] imageBytes = Files.readAllBytes(Paths.get(file.getPath()));
    return new FileData(file.getContentType(), new ByteArrayResource(imageBytes));
  }
}
