/**
 * 西安中科天塔科技股份有限公司
 * Copyright (c) 2018-2028, tianta.INC All Rights Reserved.
 */
package com.ocean.map.utils;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * <b>Description:</b>  <br/>
 * <b>@Author:</b> Ocean <br/>
 * <b>@DateTime:</b> 2019/11/11 15:15
 */
public class ImageUtils {

    public static byte[] cover(File file, File waterFile) throws IOException {
        // 获取底图
        BufferedImage buffImg = ImageIO.read(file);
        // 获取层图
        BufferedImage waterImg = ImageIO.read(waterFile);
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = buffImg.createGraphics();
        // 在图形和图像中实现混合和透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        // 绘制
        g2d.drawImage(waterImg, 0, 0, waterImg.getWidth(), waterImg.getHeight(), null);
        g2d.dispose();// 释放图形上下文使用的系统资源
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(buffImg,"png",out);
        return out.toByteArray();
    }

}
