package com.imooc.mall.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import java.io.IOException;

//图片工具类
public class ImageUtil {

    public static void imageUtil() throws IOException {
        String path="D:/image/";
        //切割
        Thumbnails.of(path+"imge.jpg").sourceRegion(Positions.BOTTOM_RIGHT,200,
                200).size(200,200).toFile(path+"crop.jpg");

        //缩放
        Thumbnails.of(path+"imge.jpg").scale(0.7).toFile(path+"scale.jpg");
        Thumbnails.of(path+"imge.jpg").scale(1.5).toFile(path+"scale.jpg");
        //keepAspectRatio的true和false表示是否开启比例的缩放
        Thumbnails.of(path+"imge.jpg").size(500,500).keepAspectRatio(true)
                        .toFile(path+"scad.jpg");
        Thumbnails.of(path+"imge.jpg").size(500,500).keepAspectRatio(false)
                .toFile(path+"scad.jpg");

        //旋转
        Thumbnails.of(path+"imge.jpg").rotate(90).toFile(path+"rotate.jpg");

        //旋转加缩放
        Thumbnails.of(path+"imge.jpg").rotate(90).size(500,500).keepAspectRatio(true)
                .toFile(path+"sca.jpg");
    }
}
