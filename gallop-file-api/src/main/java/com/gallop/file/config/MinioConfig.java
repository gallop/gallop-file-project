package com.gallop.file.config;

import com.gallop.file.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio文件上传配置文件
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class MinioConfig {

    @Autowired
    private StorageProperties storageProperties;

    @Bean
    public void initMinio(){
        String minioUrl = storageProperties.getMinio().getMinioUrl();
        System.err.println("------minioUrl="+minioUrl);
        if(!minioUrl.startsWith("http")){
            minioUrl = "http://" + minioUrl;
        }
        if(!minioUrl.endsWith("/")){
            minioUrl = minioUrl.concat("/");
        }
        MinioUtil.setMinioUrl(minioUrl);
        MinioUtil.setMinioName(storageProperties.getMinio().getMinioName());
        MinioUtil.setMinioPass(storageProperties.getMinio().getMinioPass());
        MinioUtil.setBucketName(storageProperties.getMinio().getBucketName());
    }

}
