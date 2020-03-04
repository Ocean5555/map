/**
 * 西安中科天塔科技股份有限公司
 * Copyright (c) 2018-2028, tianta.INC All Rights Reserved.
 */
package com.ocean.map.createMap;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * <b>Description:</b>  <br/>
 * <b>@Author:</b> Ocean <br/>
 * <b>@DateTime:</b> 2019/11/10 16:21
 */
@Slf4j
public class CreateMap {

    public static InputStream getInputStreamByGet(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                return inputStream;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveData(InputStream is, File file) {
        try (BufferedInputStream bis = new BufferedInputStream(is);
             BufferedOutputStream bos = new BufferedOutputStream(
                     new FileOutputStream(file))) {
            byte[] buffer = new byte[1024];
            int len ;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createImg(Integer z, Integer x, Integer y, String bast_path, String img_url) {

        String url = img_url.replaceAll("\\{x\\}", String.valueOf(x)).replaceAll("\\{y\\}", String.valueOf(y))
                .replaceAll("\\{z\\}", String.valueOf(z));
        InputStream inputStream = getInputStreamByGet(url);
        if(inputStream==null){
            return;
        }
        File dir = new File(bast_path + z + File.separator + x + File.separator);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(bast_path + z + File.separator + x + File.separator + y + ".png");
        for(int i=0 ; i<3;i++){
            try {
                saveData(inputStream, file);
                break;
            }catch (Exception e){
                continue;
            }
        }


    }

    public static void main(String[] args) {
        String bast_path = "D:/map_picture/";
        String img_url = "https://www.google.cn/maps/vt?lyrs=s%40716%2Ch%40245000000&gl=CN&&x={x}&y={y}&z={z}";  //谷歌地图 地图+名称线路图

        String bast_path2 = "D:/map_picture2/";
        String img_url2 = "http://webst01.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}";            //高德地图   地图

        String bast_path3= "D:/map_picture3/";
        String img_url3 = "http://webst02.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scale=1&style=8"; //高德地图  名称线路图

        Random random = new Random();
        int[] zz = { 9, 9 };
        for (int z = zz[0]; z <= zz[1]; z++) {// 层
            int a = z;
            int total =  (int)Math.pow(2,a);
            List<Integer[]> array = new LinkedList<>();
            for(int i=0 ;i<total;i++){
                for (int j=0;j<total;j++){
                    array.add(new Integer[]{i,j});
                }

            }
            System.out.println("size = "+array.size());
            for (int x = 0; x <= total; x++) {// 经度
                for (int y = 0; y <= total; y++) { // 纬度上下
                    if(array.size()==0){
                        return;
                    }
                    int index = random.nextInt(array.size());
                    Integer[] integers = array.get(index);
                    array.remove(index);
                    log.info(integers[0]+"  "+integers[1] + "----------"+array.size());
                    createImg(a, integers[0], integers[1],bast_path3,img_url3);
                }
            }

        }
    }

}
