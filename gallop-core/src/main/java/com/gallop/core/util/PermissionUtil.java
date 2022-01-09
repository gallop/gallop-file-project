package com.gallop.core.util;

import com.gallop.core.annotation.Permissions;
import com.gallop.core.annotation.RequiresPermissionsDesc;
import com.gallop.core.security.bean.PermVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author gallop
 * date 2020-04-19 16:50
 * Description:
 * Modified By:
 */
public class PermissionUtil {
    public static List<PermVo> listPermVo(List<PermissionWrap> permissionWraps) {
        List<PermVo> root = new ArrayList<>();
        for (PermissionWrap permissionWrap : permissionWraps) {
            Permissions permissions = permissionWrap.getPermissions();
            RequiresPermissionsDesc requiresPermissionsDesc = permissionWrap.getRequiresPermissionsDesc();
            String api = permissionWrap.getApi();

            String[] menus = requiresPermissionsDesc.menu();
            if (menus.length != 2) {
                throw new RuntimeException("目前只支持两级菜单");
            }
            String menu1 = menus[0];
            PermVo perm1 = null;
            for (PermVo permVo : root) {
                if (permVo.getTitle().equals(menu1)) {
                    perm1 = permVo;
                    break;
                }
            }
            if (perm1 == null) {
                perm1 = new PermVo();
                perm1.setId(menu1);
                perm1.setTitle(menu1);
                perm1.setChildren(new ArrayList<>());
                root.add(perm1);
            }
            String menu2 = menus[1];
            PermVo perm2 = null;
            for (PermVo permVo : perm1.getChildren()) {
                if (permVo.getTitle().equals(menu2)) {
                    perm2 = permVo;
                    break;
                }
            }
            if (perm2 == null) {
                perm2 = new PermVo();
                perm2.setId(menu2);
                perm2.setTitle(menu2);
                perm2.setChildren(new ArrayList<>());
                perm1.getChildren().add(perm2);
            }

            String button = requiresPermissionsDesc.button();
            PermVo leftPerm = null;
            for (PermVo permVo : perm2.getChildren()) {
                if (permVo.getTitle().equals(button)) {
                    leftPerm = permVo;
                    break;
                }
            }
            if (leftPerm == null) {
                leftPerm = new PermVo();
                leftPerm.setId(permissions.value());
                leftPerm.setTitle(requiresPermissionsDesc.button());
                leftPerm.setApi(api);
                perm2.getChildren().add(leftPerm);
            }
            else{
                // TODO
                // 目前限制Controller里面每个方法的RequiresPermissionsDesc注解是唯一的
                // 如果允许相同，可能会造成内部权限不一致。
                throw new RuntimeException("权限已经存在，不能添加新权限");
            }

        }
        return root;
    }

    public static List<PermissionWrap> listPermissionWrap(ApplicationContext context, String basicPackage) {
        Map<String, Object> map = context.getBeansWithAnnotation(Controller.class);
        List<PermissionWrap> permissionWrapLists = new ArrayList<>();
        System.err.println("basicPackage="+basicPackage);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object bean = entry.getValue();

            if (!StringUtils.contains(ClassUtils.getPackageName(bean.getClass()), basicPackage)) {
                continue;
            }
            //System.err.println(">>>bean-name="+bean.getClass().getName());
            //System.err.println("---key:"+ClassUtils.getPackageName(bean.getClass()));


            Class<?> clz = bean.getClass();
            Class controllerClz = clz.getClass();
            RequestMapping clazzRequestMapping = AnnotationUtils.findAnnotation(clz, RequestMapping.class);
            /*if (clazzRequestMapping != null) {
                System.err.println("api="+clazzRequestMapping.value()[0]);
            }*/
            /*Method[] methodss = clz.getMethods();
            Method[] arr$ = methodss;
            int len$ = methodss.length;
            for(int i$ = 0; i$ < len$; ++i$) {
                Method method = arr$[i$];
                System.err.println("method-name="+method.getName());
                if (method.getAnnotation(Permissions.class) != null) {
                    System.err.println("Permissions-Annotation is not null");
                }
            }*/
            List<Method> methods = MethodUtils.getMethodsListWithAnnotation(clz, RequiresPermissionsDesc.class,true,false);
            for (Method method : methods) {
                Permissions permissions = AnnotationUtils.getAnnotation(method, Permissions.class);
                RequiresPermissionsDesc requiresPermissionsDesc = AnnotationUtils.getAnnotation(method, RequiresPermissionsDesc.class);

                if(bean.getClass().getName().contains("FileManagerController")){

                }
                if (permissions == null || requiresPermissionsDesc == null) {
                    continue;
                }

                String api = "";
                if (clazzRequestMapping != null) {
                    api = clazzRequestMapping.value()[0];
                }

                PostMapping postMapping = AnnotationUtils.getAnnotation(method, PostMapping.class);
                if (postMapping != null) {
                    api = "POST " + api + postMapping.value()[0];
                    //System.err.println("post-api=="+api);
                    PermissionWrap permissionWrap = new PermissionWrap();
                    permissionWrap.setPermissions(permissions);
                    permissionWrap.setRequiresPermissionsDesc(requiresPermissionsDesc);
                    permissionWrap.setApi(api);
                    permissionWrapLists.add(permissionWrap);
                    continue;
                }
                GetMapping getMapping = AnnotationUtils.getAnnotation(method, GetMapping.class);
                if (getMapping != null) {
                    api = "GET " + api + getMapping.value()[0];
                    //System.err.println("get-api=="+api);
                    PermissionWrap permissionWrap = new PermissionWrap();
                    permissionWrap.setPermissions(permissions);
                    permissionWrap.setRequiresPermissionsDesc(requiresPermissionsDesc);
                    permissionWrap.setApi(api);
                    permissionWrapLists.add(permissionWrap);
                    continue;
                }
                // TODO
                // 这里只支持GetMapping注解或者PostMapping注解，应该进一步提供灵活性
                throw new RuntimeException("目前权限管理应该在method的前面使用GetMapping注解或者PostMapping注解");
            }
        }
        return permissionWrapLists;
    }

    public static Set<String> listPermissionString(List<PermissionWrap> permissionWrapLists) {
        Set<String> permissionsString = new HashSet<>();
        for(PermissionWrap permissionWrap : permissionWrapLists){
            permissionsString.add(permissionWrap.getPermissions().value());
        }
        return permissionsString;
    }
    public static String getPermissionStrByRegex(String s){
        String result = null;
        // (?<=exp) 表示匹配exp后面的位置
        Pattern pattern = Pattern.compile("(?<=hasPermission\\(')[^')]+");
        Matcher matcher = pattern.matcher(s);
        if(matcher.find()){
            result = matcher.group(0);
        }

        return result;
    }

    public static void main(String[] args) {
        /*String s = "@aps.hasPermission('admin:user:read')abd('999')";
        //(?<=\\()[^)]+")   \b\w+\b(?="[.])
        // (?<=exp) 表示匹配exp后面的位置
        Pattern pattern = Pattern.compile("(?<=hasPermission\\(')[^')]+");
        Matcher matcher = pattern.matcher(s);

        while(matcher.find()){
            System.out.println("result:"+matcher.group(0));
        }*/


    }
}
