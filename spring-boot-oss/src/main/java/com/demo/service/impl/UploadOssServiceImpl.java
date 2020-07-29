package com.demo.service.impl;


import com.aliyun.oss.model.OSSObject;
import com.demo.config.OSSConfig;
import com.demo.utils.OSSSingleUtil;
import com.demo.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 公共业务具体实现类
 * @Author:
 * @Date: 2020/01/01
 */
@Service
public class UploadOssServiceImpl {

    @Autowired
    private OSSConfig ossConfig;
    private final String filePath = "upload/demo/";
    /**
     * 上传文件至阿里云 oss
     * @param file
     * @return
     * @throws Exception
     */
    public R uploadOSS(MultipartFile file) throws Exception {
        String path = filePath  ;
        // 低依赖版本 oss 上传工具
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));;
        String fileName = OSSSingleUtil.upload(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret(), ossConfig.getBucketName(), ossConfig.getUrl(), file.getInputStream(),
                path, fileSuffix);

        Map<String, Object> resultMap = new HashMap<>(1);
        resultMap.put("fileName", fileName);
        return R.ok().setData(resultMap);
    }


    /**
     * @author lastwhisper
     * @desc 下载文件
     * 文档链接 https://help.aliyun.com/document_detail/84823.html?spm=a2c4g.11186623.2.7.37836e84ZIuZaC#concept-84823-zh
     */
    public void exportOssFile(OutputStream os, String objectName) throws IOException {
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = OSSSingleUtil.getObject(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret(),ossConfig.getBucketName(),
                filePath  + objectName);
        // 读取文件内容。
        BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
        BufferedOutputStream out = new BufferedOutputStream(os);
        byte[] buffer = new byte[1024];
        int lenght = 0;
        while ((lenght = in.read(buffer)) != -1) {
            out.write(buffer, 0, lenght);
        }
        if (out != null) {
            out.flush();
            out.close();
        }
        if (in != null) {
            in.close();
        }
    }
}

