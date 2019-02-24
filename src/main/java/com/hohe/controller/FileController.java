package com.hohe.controller;

import com.hohe.oss.AliyunOssClient;
import com.hohe.oss.AliyunOssFile;
import com.hohe.oss.OssClient;
import com.hohe.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

@Controller
@RequestMapping(value = "/file")
public class FileController {

    public final Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * 上传附件到OSS
     * @param files
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public void uploadFileToOss(@RequestParam(value = "file") MultipartFile[] files){

        if(files != null && files.length > 0 ){
            logger.info("正在上传附件......");
            OssClient aliyunOssClient = (OssClient) new AliyunOssClient();
            Date now = new Date();
            String dir = DateUtil.convertDateToString(now, DateUtil.FORMAT_yyyy) + "/"+ DateUtil.convertDateToString(now, DateUtil.FORMAT_MM) + "/"+ DateUtil.convertDateToString(now, DateUtil.FORMAT_dd) + "/";//附件存在的目录位置

            for (MultipartFile file : files) {
                try {
                    AliyunOssFile ossFile = (AliyunOssFile)  aliyunOssClient.uploadFile(dir, file.getOriginalFilename(), file.getInputStream(), null);
                    logger.info("上传成功! ossFilePath:"+ ossFile.getEndPoint());

                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info("正在上传失败......", e);
                }
            }
        }
    }


    @RequestMapping(value = "/downLoad", method = RequestMethod.POST)
    @ResponseBody
    public void downLoad(HttpServletRequest request, HttpServletResponse response, String filePath, String fileName){

        if(StringUtils.isNotBlank(filePath)){
            OssClient client = (OssClient) new AliyunOssClient();
            InputStream inputstream  =   client.downloadFile(filePath);
            //设置相关响应信息返回给前端
            try {
                response.reset();
                response.setContentType("application/x-download");
                response.setCharacterEncoding("utf-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
                // response.addHeader("Content-Length", "" + file.length());
                OutputStream out = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                byte[] buffer = new byte[1024 * 1024 ];
                int i = -1;
                while ((i = inputstream.read(buffer)) != -1) {
                    out.write(buffer, 0, i);
                }
                out.flush();
                out.close();
                inputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("文件下载异常: ", e);
            }
        }


    }




}