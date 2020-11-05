package com.offcn.util;

//文件上传 工具类

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class OssTemplate {
    private String endpoint = "http://oss-cn-chengdu.aliyuncs.com";
    private String bucketDomain = "offcn0706.oss-cn-chengdu.aliyuncs.com";
    private String accessKeyId = "LTAI4G5imNwvKD16fKHQ4CWR";  //keyid
    private String accessKeySecret ="4Rpp5SNetppm8fiJAJKx5UJMCBXePt"; //keySecret
    private String bucketName = "offcn0706";     //桶的名字

    //文件上传 路径
    public String upload(InputStream inputStream, String fileName){
        //处理文件名  uuid+ 日期
        fileName = UUID.randomUUID().toString().replace("-", "")+fileName;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String foldName = dateFormat.format(new Date());
        //创建oss 对象
        OSS oss = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        //上传文件流  桶名 和文件名
        oss.putObject(bucketName,"pic/"+foldName+"/"+fileName,inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //关闭oss
        oss.shutdown();
        String url= "https://"+bucketDomain+"/pic/"+foldName+"/"+fileName;
        System.out.println("上传文件访问路径:"+url);
        return url;
    }
}
