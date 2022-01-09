package com.gallop.file.storage;

import com.gallop.file.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * author gallop
 * date 2021-08-03 9:39
 * Description:
 * Modified By:
 */
@Slf4j
public class MinioStorage implements Storage{

    private String objectName;

    @Override
    public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
        try {
            MinioUtil.upload(inputStream,keyName);
            this.objectName = keyName;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Minio 上传文件出错："+e.getMessage(),e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String keyName) {
        return null;
    }

    @Override
    public Resource loadAsResource(String keyName) {
        InputStream in=null;
        try {
            in = MinioUtil.getMinioFile(MinioUtil.getBucketName(),keyName);
            Resource resource = new InputStreamResource(in);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void delete(String keyName) {
        MinioUtil.removeObject(MinioUtil.getBucketName(),keyName);
    }

    @Override
    public String generateUrl() {
        return null;
    }

    @Override
    public String generateKey() {
        return objectName;
    }
}
