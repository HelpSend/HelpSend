package com.heu.cs.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;



/**
 * Created by memgq on 2017/6/12.
 */
public class ImageCompress {


    private File file = null; // 文件对象
    private String inputDir; // 输入图路径
    private String outputDir; // 输出图路径
    private String inputFileName; // 输入图文件名
    private String outputFileName; // 输出图文件名
    private int outputWidth = 100; // 默认输出图片宽
    private int outputHeight = 100; // 默认输出图片高
    private boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)

    /**
     * 如果目录不是以/或者\结尾，结尾处加入/
     *
     * @param dir
     * @return
     */
    private String getDir(String dir) {
        if (!(dir.endsWith("/") || dir.endsWith("\\"))) {
            dir = dir + "/";
        }
        return dir;
    }

    public ImageCompress() { // 初始化变量
        inputDir = "";
        outputDir = "";
        inputFileName = "";
        outputFileName = "";
        outputWidth = 100;
        outputHeight = 100;
    }

    public void setInputDir(String inputDir) {

        this.inputDir = getDir(inputDir);
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = getDir(outputDir);
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void setOutputWidth(int outputWidth) {
        this.outputWidth = outputWidth;
    }

    public void setOutputHeight(int outputHeight) {
        this.outputHeight = outputHeight;
    }

    public void setWidthAndHeight(int width, int height) {
        this.outputWidth = width;
        this.outputHeight = height;
    }

    /*
     * 获得图片大小 传入参数 String path ：图片路径
     */
    public long getPicSize(String path) {
        file = new File(path);
        return file.length();
    }

    // 图片处理
    public String compressPic() {
        try {
            // 获得源文件
            file = new File(inputDir + inputFileName);
            if (!file.exists()) {
                return "file is not exist";
            }
            String fileName = file.getName();
            if (fileName.endsWith("jpg") || fileName.endsWith("jpeg")
                    || fileName.endsWith("png") || fileName.endsWith("gif")) {
                Image img = ImageIO.read(file);
                // 判断图片格式是否正确
                if (img.getWidth(null) == -1) {
                    return "no";
                } else {
                    int newWidth;
                    int newHeight;
                    // 判断是否是等比缩放
                    if (this.proportion == true) {
                        // 为等比缩放计算输出的图片宽度及高度
                        double rate1 = ((double) img.getWidth(null))
                                / (double) outputWidth + 0.1;
                        double rate2 = ((double) img.getHeight(null))
                                / (double) outputHeight + 0.1;
                        // 根据缩放比率大的进行缩放控制
                        double rate = rate1 > rate2 ? rate1 : rate2;
                        newWidth = (int) (((double) img.getWidth(null)) / rate);
                        newHeight = (int) (((double) img.getHeight(null)) / rate);
                    } else {
                        newWidth = outputWidth; // 输出的图片宽度
                        newHeight = outputHeight; // 输出的图片高度
                    }
                    BufferedImage tag = new BufferedImage((int) newWidth,
                            (int) newHeight, BufferedImage.TYPE_INT_RGB);

					/*
                     * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好
					 * 但速度慢
					 */
                    tag.getGraphics().drawImage(
                            img.getScaledInstance(newWidth, newHeight,
                                    Image.SCALE_SMOOTH), 0, 0, null);
                    String dstName=outputDir+ outputFileName;
                    FileOutputStream out = new FileOutputStream(dstName);
                    // JPEGImageEncoder可适用于其他图片类型的转换

                    String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);
                    ImageIO.write(tag, /*"GIF"*/ formatName /* format desired */ , new File(dstName) /* target */ );
                    out.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "ok";
    }

    public String compressPic(String inputDir, String outputDir,
                              String inputFileName, String outputFileName) {
        this.setInputDir(inputDir);
        this.setOutputDir(outputDir);
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        return compressPic();
    }

    public String compressPic(String inputDir, String outputDir,
                              String inputFileName, String outputFileName, int width, int height,
                              boolean gp) {
        this.setInputDir(inputDir);
        this.setOutputDir(outputDir);
        // 输入图文件名
        this.inputFileName = inputFileName;
        // 输出图文件名
        this.outputFileName = outputFileName;
        // 设置图片长宽
        setWidthAndHeight(width, height);
        // 是否是等比缩放 标记
        this.proportion = gp;
        return compressPic();
    }

    /**
     * @param arg arg[0] 源图片所在目录 arg[1] 压缩目录 arg[2] 输出图片宽度 arg[3] 输出图片高度
//     */
//    public static void main(String[] arg) {
//        ImageCompress mypic = new ImageCompress();
//        String inputDir = arg[0];
//        String outputDir = arg[1];
//        Integer width = Integer.parseInt(arg[2]);
//        Integer height = Integer.parseInt(arg[3]);
//        File file = new File(inputDir);
//        File[] fileList = file.listFiles();
//        for (int i = 0; i < fileList.length; ++i) {
//            mypic.compressPic(inputDir, outputDir, fileList[i].getName(),
//                    fileList[i].getName(), width, height, true);
//        }
//    }


}
