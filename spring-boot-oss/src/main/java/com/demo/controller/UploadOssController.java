package com.demo.controller;

import com.demo.service.impl.UploadOssServiceImpl;
import com.demo.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@ResponseBody
public class UploadOssController {
    @Autowired
    private UploadOssServiceImpl commonService;

    /**
     * 上传文件至阿里云 oss
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "upload/oss", method = {RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public R uploadOSS(@RequestParam(value = "file") MultipartFile file) throws Exception {
        return commonService.uploadOSS(file);
    }


    /**
     * @return
     * @desc 根据文件名下载oss上的文件
     * @Param objectName
     */
    @RequestMapping("download/oss")
    @ResponseBody
    public void download(@RequestParam("fileName") String objectName, HttpServletResponse response) throws IOException {
        //通知浏览器以附件形式下载
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(objectName.getBytes(), "ISO-8859-1"));
        commonService.exportOssFile(response.getOutputStream(), objectName);
    }
}
