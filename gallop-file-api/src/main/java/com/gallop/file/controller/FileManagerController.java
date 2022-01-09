package com.gallop.file.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.gallop.core.annotation.Permissions;
import com.gallop.core.annotation.RequiresPermissionsDesc;
import com.gallop.core.base.BaseResultUtil;
import com.gallop.core.context.login.LoginContextHolder;
import com.gallop.file.dao.FileObjectDao;
import com.gallop.file.dao.FileTreeNodeDao;
import com.gallop.file.enums.CommonStatusEnum;
import com.gallop.file.enums.FileActionEnum;
import com.gallop.file.pojo.FileObject;
import com.gallop.file.pojo.FileTreeNode;
import com.gallop.file.pojo.RootFolderPojo;
import com.gallop.file.service.RootFolderService;
import com.gallop.file.util.CommonUtils;
import com.gallop.file.util.MinioUtil;
import com.gallop.file.util.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * author gallop
 * date 2021-07-21 15:23
 * Description:
 * Modified By:
 */
@RestController
@RequestMapping("/admin/api")
@Slf4j
public class FileManagerController {
    @Autowired
    private FileObjectDao fileObjectDao;
    @Resource
    private FileTreeNodeDao treeNodeDao;
    @Resource
    private RootFolderService rootFolderService;

    public static final List<String> ids = new ArrayList<String>(Arrays.asList("60ff5f45b10523562a5adc78"));

    @GetMapping("/getFileList")
    @Permissions("admin:fileManager:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "我的文档"}, button = "文档列表")
    public Object list(String source,String target,String action) {
        if(action == null){
            action = "list";
        }
        Long userId = LoginContextHolder.getContext().getSysLoginUser().getUser().getId();
        List<RootFolderPojo> rootFolderList = rootFolderService.findByUserId(userId);
        System.err.println("rootFolderList=="+rootFolderList.toString());
        if(CollectionUtil.isNotEmpty(rootFolderList)){
            List<String> filedIds = rootFolderList.stream().map(RootFolderPojo::getFileId).collect(Collectors.toList());
            List<FileTreeNode> treeNodeList = treeNodeDao.getTreeNodeOfAllChildren(filedIds.toArray(new String[filedIds.size()]));

            treeNodeList.stream().forEach(item->{
                if(item.getData() != null && item.getData().size()>0){
                    List<FileTreeNode> childrenNode = TreeUtil.buildTreeNode(item.getData());
                    item.getData().clear();
                    item.appendChildren(childrenNode);
                }
            });

            System.err.println("treeNodeList="+treeNodeList.toString());

            return treeNodeList;
        }else {
            //创建一个默认根目录
            List<FileTreeNode> list = new ArrayList<>();
            FileTreeNode node = createFolder("我的文件夹", null);
            list.add(node);
            return list;
        }

    }

    @Permissions("admin:fileManager:operate")
    @RequiresPermissionsDesc(menu = {"系统管理", "我的文档"}, button = "文档操作")
    @PostMapping("/operateFile")
    public Object list2(String source,String target,String action) {
        if(action == null){
            action = "list";
        }
        switch (FileActionEnum.getFileActionEnum(action)){
            case ACTION_LIST:{
                break;
            }
            case ACTION_RENAME:{
                //List<FileObject> list = fileObjectDao.findByIds(ids);
                rename(source,target);
                break;
            }
            /*case ACTION_MOVE:{
                break;
            }*/
            case ACTION_REMOVE:{
                //List<FileObject> list = fileObjectDao.findByIds(ids);
                //删除文档目录
                //remove(source,list);
                remove(source);
                break;
            }
        }

        return null;
    }

    @Permissions("admin:fileManager:create")
    @RequiresPermissionsDesc(menu = {"系统管理", "我的文档"}, button = "新建目录")
    @PostMapping("/create")
    public Object create(long id, String source,String target,String action) {
        System.err.println("id="+id);
        System.err.println("source="+source);
        System.err.println("target="+target);
        System.err.println("action="+action);
        /*FileObject newFolder = FileObject.builder()
                .isFolder(true)
                .id(""+id)
                .type("folder")
                .value(source)
                .size(0l)
                .date(new Date())
                .build();

        List<FileObject> list = fileObjectDao.findByIds(ids);
        createFolder(target,newFolder,list);*/

        FileTreeNode newFolder = createFolder(source,target);
        return newFolder;
    }

    /**
     * 上传
     * @param request
     */
    @Permissions("admin:fileManager:uploadMino")
    @RequiresPermissionsDesc(menu = {"系统管理", "我的文档"}, button = "上传文件")
    @PostMapping(value = "/uploadMinio")
    public Object uploadMinio(HttpServletRequest request) {
        String target = request.getParameter("target");
        System.out.println("orignal-target="+target);
        String bizPath = request.getParameter("biz");
        if(StringUtils.isEmpty(bizPath)){
            bizPath = "";
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("upload");// 获取上传文件对象
        String origName = file.getOriginalFilename();// 获取文件名
        origName = CommonUtils.getFileName(origName);
        int begin = origName.indexOf(".");
        String extensionName = origName.substring(begin+1);//获取后缀名
        String fileUrl =  MinioUtil.upload(file,bizPath);
        if(StringUtils.isEmpty(fileUrl)){
            return BaseResultUtil.failure(406,"上传失败,请检查配置信息是否正确!");
        }
        System.err.println("fileUrl="+fileUrl);
        System.err.println(">>>getContentType="+file.getContentType());
        System.err.println(">>>getContentType="+extensionName);
        //保存文件信息
       /* FileObject newFile = FileObject.builder()
                .isFolder(true)
                .id(UUID.randomUUID().toString().replaceAll("-",""))
                .type(file.getContentType())
                .value(orgName)
                .size(file.getSize())
                .date(new Date())
                .url(fileUrl)
                .build();

        System.err.println("target=="+target);

        List<FileObject> list = fileObjectDao.findByIds(ids);
        createFolder(target,newFile,list);*/
        Long userId = LoginContextHolder.getContext().getSysLoginUser().getUser().getId();
        FileTreeNode treeNode = FileTreeNode.builder()
                .isFolder(true)
                //.id(new ObjectId(UUID.randomUUID().toString().replaceAll("-","")))
                .type(getFileType(extensionName))
                .value(origName)
                .size(file.getSize())
                .date(new Date())
                .ownId(userId.toString())
                .url(fileUrl)
                .parentId(new ObjectId(target))
                .build();
        treeNode = treeNodeDao.saveTreeNode(treeNode);
        return treeNode;

    }

    private String getFileType(String contentType){
        String fileType = "";
        contentType = contentType.toLowerCase().trim();
        if(contentType==null || contentType.equals("")){
            fileType = "file";
        }else if(contentType.contains("txt")){
            fileType = "text";
        }else if(contentType.contains("jpg") || contentType.contains("jpeg") || contentType.contains("png")){
            fileType = "image";
        }else if(contentType.contains("pdf")){
            fileType = "pdf";
        }else if(contentType.contains("doc")){
            fileType = "doc";
        }else if(contentType.contains("ppt")){
            fileType = "pp";
        }else if(contentType.contains("xls")){
            fileType = "excel";
        }else if(contentType.contains("md")){
            fileType = "archive";
        }else {
            fileType = "file";
        }

        return fileType;
    }

    @Permissions("admin:fileManager:download")
    @RequiresPermissionsDesc(menu = {"系统管理", "我的文档"}, button = "下载")
    @PostMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response, String source) {
        InputStream in=null;
        System.err.println("source=="+source);
       /* List<FileObject> list = fileObjectDao.findByIds(ids);
        FileObject fileObject = findNode(source,list);*/
        FileTreeNode fileTreeNode = treeNodeDao.getFileTreeNode(source);
        if(fileTreeNode != null){
            try {
               /* MediaType mediaType = MediaType.parseMediaType(fileObject.getType());
                System.err.println("mediaType=="+mediaType.toString());
                response.setContentType(mediaType.toString());*/
                String url = fileTreeNode.getUrl();
                String objectName = url.substring(url.lastIndexOf("/")+1);
                System.err.println("objectName="+objectName);
                response.addHeader("Content-Disposition", "attachment;filename=" + objectName);
                response.setContentType("multipart/form-data");
                in = MinioUtil.getMinioFile(MinioUtil.getBucketName(),objectName);
                IOUtils.copy(in,response.getOutputStream());

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(in!=null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Permissions("admin:fileManager:doPreview")
    @RequiresPermissionsDesc(menu = {"系统管理", "我的文档"}, button = "预览")
    @GetMapping(value = "/doPreview")
    public Object doPreview(String id){
        System.err.println("id=="+id);
        /*List<FileObject> list = fileObjectDao.findByIds(ids);
        FileObject fileObject = findNode(id,list);
        String url = "";
        if(fileObject != null){
            String fileUrl = fileObject.getUrl();
            String objectName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);

            url = MinioUtil.getObjectURL(MinioUtil.getBucketName(),objectName,3600);
        }*/
        FileTreeNode fileTreeNode = treeNodeDao.getFileTreeNode(id);
        String url = "";
        if(!ObjectUtils.isEmpty(fileTreeNode)){
            String fileUrl = fileTreeNode.getUrl();
            String objectName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);

            url = MinioUtil.getObjectURL(MinioUtil.getBucketName(),objectName,3600);
        }
        HashMap<String,String> resultMap = new HashMap<>();
        resultMap.put("url",url);

        return resultMap;
    }
    private void createFolder(String targetId,FileObject newFolder,List<FileObject> list){
        if(list ==null || list.size()==0){
            return ;
        }
        for (int i = 0; i < list.size(); i++) {
            FileObject fileTmp = list.get(i);
            if(fileTmp.getId().equals(targetId)){
                fileTmp.append(newFolder);
                fileObjectDao.updateName(fileTmp);
                break;
            }else {
                FileObject childFileTmp = findNode(targetId,fileTmp.getData());
                if(childFileTmp != null){
                    childFileTmp.append(newFolder);
                    fileObjectDao.updateName(fileTmp);
                    break;
                }
            }
        }
    }
    @Transactional
    public FileTreeNode createFolder(String source,String target){
        Long userId = LoginContextHolder.getContext().getSysLoginUser().getUser().getId();
        FileTreeNode newFolder = FileTreeNode.builder()
                .isFolder(true)
                .type("folder")
                .ownId(userId.toString())
                .value(source)
                .size(0l)
                .date(new Date())
                .open(true)
                .build();
        //根目录 没有taget参数值
        if(!StringUtils.isBlank(target)){
            newFolder.setParentId(new ObjectId(target));
        }
        newFolder = treeNodeDao.saveTreeNode(newFolder);
        System.err.println("new-file-id="+newFolder.getId());
        //根目录和用户的对应关系 存mysql表中
        if(StringUtils.isBlank(target)){
            RootFolderPojo folderPojo = new RootFolderPojo();
            folderPojo.setFileId(newFolder.getId());
            folderPojo.setUserId(userId.longValue());
            folderPojo.setAddTime(LocalDateTime.now());
            folderPojo.setUpdateTime(LocalDateTime.now());
            folderPojo.setStatus(CommonStatusEnum.ENABLE.getCode());
            System.err.println("create new folderPojo="+folderPojo.toString());
            rootFolderService.saveRootFolder(folderPojo);
        }

        return newFolder;
    }
     /**
      * @date 2021-07-26 17:30
      * Description: 修改名称
      * Param:
      *     source: 1627033951234
      *     action: rename
      *     target: test3
      * return:
      **/
    private void rename(String source,String targetName,List<FileObject> list){
        if(list ==null || list.size()==0){
            return ;
        }

        for (int i = 0; i < list.size(); i++) {
            FileObject fileTmp = list.get(i);
            if(fileTmp.getId().equals(source)){
                fileTmp.setValue(targetName);
                fileObjectDao.updateName(fileTmp);
                break;
            }else {
                FileObject childFileTmp = findNode(source,fileTmp.getData());
                if(childFileTmp != null){
                    childFileTmp.setValue(targetName);
                    fileObjectDao.updateName(fileTmp);
                    break;
                }
            }
        }
    }

    private void rename(String source,String targetName){
        if(StringUtils.isBlank(source)){
            return;
        }
        FileTreeNode node = new FileTreeNode();
        node.setId(new ObjectId(source));
        node.setValue(targetName);

        treeNodeDao.updateFileTreeNode(node);
    }

     /**
      * @date 2021-07-26 17:38
      * Description: 删除目录或文件
      * Param:
      * return:
      **/
    private void remove(String source,List<FileObject> list){
        if(list ==null || list.size()==0){
            return ;
        }

        for (int i = 0; i < list.size(); i++) {
            FileObject fileTmp = list.get(i);
            if(fileTmp.getId().equals(source)){
                fileObjectDao.deleteById(source);
                break;
            }else {
                List<FileObject> childDataList = findNodeInclude(source,fileTmp);
                if(childDataList != null){
                    //childDataList = childDataList.stream().filter(e -> !source.equals(e.getId())).collect(Collectors.toList());
                    fileObjectDao.updateName(fileTmp);
                    break;
                }
            }
        }
    }
    @Transactional
    public void remove(String source){
        if(StringUtils.isBlank(source)){
            return;
        }
        FileTreeNode fileTreeNode = treeNodeDao.getFileTreeNode(source);
        if(!ObjectUtils.isEmpty(fileTreeNode)){
            treeNodeDao.deleteById(source);
            String url = fileTreeNode.getUrl();
            String objectName = url.substring(url.lastIndexOf("/")+1);
            MinioUtil.removeObject(MinioUtil.getBucketName(),objectName);
        }
    }

    private FileObject findNode(String id,List<FileObject> list){
        FileObject fileObject = null;
        if(list == null || list.size()==0){
            return null;
        }

        for (int i = 0; i < list.size(); i++) {
            FileObject tmp = list.get(i);
            if(tmp.getId().equals(id)){
                fileObject = tmp;
                break;
            }else if(tmp.getData() != null && tmp.getData().size()>0){
                fileObject = findNode(id,tmp.getData());
                if(fileObject != null){
                    break;
                }
            }
        }

        return fileObject;
    }

    private List<FileObject> findNodeInclude(String id,FileObject fileObject){
        List<FileObject> resultList = null;
        boolean hasFound = false;
        if(fileObject == null || fileObject.getData() == null ||
                fileObject.getData().size()==0){
            return null;
        }

        for (int i = 0; i < fileObject.getData().size(); i++) {
            FileObject tmp = fileObject.getData().get(i);
            if(ObjectUtils.isEmpty(tmp.getId())){
                break;
            }else if(tmp.getId().equals(id)){
                hasFound = true;
                break;
            }else if(tmp.getData() != null && tmp.getData().size()>0){
                resultList = findNodeInclude(id,tmp);
                if(resultList != null){
                    tmp.setData(resultList);
                    break;
                }
            }
        }
        if(hasFound){
            resultList = fileObject.getData().stream().filter(e -> !id.equals(e.getId())).collect(Collectors.toList());
            fileObject.setData(resultList);
            System.err.println("resultList-size"+resultList.size());
            resultList.forEach(e->{
                System.err.println("value="+e.getValue());
            });
        }

        return resultList;
    }
}
