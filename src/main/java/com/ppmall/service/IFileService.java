package com.ppmall.service;

import com.ppmall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {
    ServerResponse uploadFile(MultipartFile file ,String path) throws IOException;
}
