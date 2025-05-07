package com.devteria.file.controller;

import com.devteria.file.dto.ApiResponse;
import com.devteria.file.dto.response.FileData;
import com.devteria.file.dto.response.FileResponse;
import com.devteria.file.service.FileService;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

  FileService fileService;

  @PostMapping("/media/upload")
  public ApiResponse<FileResponse> uploadFile(@RequestParam("file") MultipartFile file)
      throws IOException {
    return ApiResponse.<FileResponse>builder()
        .result(fileService.uploadFile(file))
        .build();
  }

  @GetMapping("/media/download/{fileName}")
  public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName)
      throws IOException {
    FileData fileData = fileService.downloadFile(fileName);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, fileData.contentType())
        .header(HttpHeaders.CONTENT_DISPOSITION, "hihi")
        .body(fileData.resource());
  }
}
