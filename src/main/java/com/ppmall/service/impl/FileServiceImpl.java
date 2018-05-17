package com.ppmall.service.impl;

import com.ppmall.common.ServerResponse;
import com.ppmall.service.IFileService;
import com.ppmall.util.FtpUtil;
import com.ppmall.util.PropertiesUtil;
import com.ppmall.util.UUIDUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("iFileService")
public class FileServiceImpl implements IFileService {
    @Override
    public ServerResponse uploadFile(MultipartFile file, String path) throws IOException {

        String fileName = file.getOriginalFilename();
        fileName = UUIDUtil.getUUID() + fileName.substring(fileName.lastIndexOf("."));
        String fileUrl = PropertiesUtil.getProperty("ftp.server.http.prefix") + fileName;

        List<File> files = new ArrayList<>();

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path, fileName);
        file.transferTo(targetFile);
        files.add(targetFile);
        FtpUtil.uploadFile(files);
        targetFile.delete();

        Map returnMap = new HashMap();
        returnMap.put("uri",fileName);
        //returnMap.put("url",fileUrl);
        returnMap.put("file_path",fileUrl);

        return ServerResponse.createSuccess("上传成功",returnMap);
    }
}
