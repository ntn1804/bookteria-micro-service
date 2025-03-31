package com.devteria.file.controller;

import com.devteria.file.dto.ApiResponse;
import com.devteria.file.dto.response.FileResponse;
import com.devteria.file.service.FileService;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
}
