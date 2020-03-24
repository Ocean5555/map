/**
 * 西安中科天塔科技股份有限公司
 * Copyright (c) 2018-2028, tianta.INC All Rights Reserved.
 */
package com.ocean.map.controller;

import com.ocean.map.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.rmi.runtime.Log;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <b>Description:</b>  <br/>
 * <b>@Author:</b> Ocean <br/>
 * <b>@DateTime:</b> 2019/11/11 14:01
 */
@Controller
@CrossOrigin
@Slf4j
public class Map {

    private static String pathPrefix;

    @Value("${windows.prefix.path}")
    private String windowsPath;
    @Value("${linux.prefix.path}")
    private String linuxPath;

    @PostConstruct
    private void init(){
        if(File.separator.equals("\\")){
            pathPrefix = windowsPath;
            log.info("current system:windows");
        }else{
            pathPrefix = linuxPath;
            log.info("current system:linux");
        }
    }

    @RequestMapping(value = "/map",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@RequestParam Integer z,@RequestParam Integer x,@RequestParam Integer y) throws IOException {
        String path1 = pathPrefix + File.separator + "map_picture" + File.separator + "gaode1" + File.separator +
                z + File.separator + x + File.separator + y + ".png";
        String path2 = pathPrefix + File.separator + "map_picture" + File.separator + "gaode2" + File.separator +
                z + File.separator + x + File.separator + y + ".png";
        File file1 = new File(path1);
        File file2 = new File(path2);
        if(!file1.exists()){
            log.warn("访问资源不存在："+path1);
            return null;
        }
        if(!file2.exists() || file2.length()==176){
            FileInputStream inputStream = new FileInputStream(file1);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;
        }
        return ImageUtils.cover(file1, file2);
    }

}
