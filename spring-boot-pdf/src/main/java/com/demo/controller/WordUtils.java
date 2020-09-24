package com.demo.controller;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.Docx4J;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 单元格合并,背景颜色,边框的功能
 */
@Slf4j
public class WordUtils {
    public static void main(String[] args) throws Exception {
        WordUtils.convertDocxToPdf("D:\\temp\\01\\test.docx","D:\\temp\\01\\test.pdf");
    }

    /**
     * docx文档转换为PDF
     *
     * @param pdfPath PDF文档存储路径
     * @throws Exception 可能为Docx4JException, FileNotFoundException, IOException等
     */
    public static void convertDocxToPdf(String docxPath, String pdfPath)  {

        FileOutputStream fileOutputStream = null;
        try {
            System.out.println(DateUtil.now());
            File file = new File(docxPath);
            fileOutputStream = new FileOutputStream(new File(pdfPath));
            System.out.println(DateUtil.now());
            WordprocessingMLPackage mlPackage = WordprocessingMLPackage.load(file);
            System.out.println(DateUtil.now());
            setFontMapper(mlPackage);
            System.out.println(DateUtil.now());
            Docx4J.toPDF(mlPackage, new FileOutputStream(new File(pdfPath)));
            System.out.println(DateUtil.now());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    private static void setFontMapper(WordprocessingMLPackage mlPackage) throws Exception {
        Mapper fontMapper = new IdentityPlusMapper();
        fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
        fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
        fontMapper.put("微软雅黑", PhysicalFonts.get("Microsoft Yahei"));
        fontMapper.put("黑体", PhysicalFonts.get("SimHei"));
        fontMapper.put("楷体", PhysicalFonts.get("KaiTi"));
        fontMapper.put("新宋体", PhysicalFonts.get("NSimSun"));
        fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
        fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
        fontMapper.put("宋体扩展", PhysicalFonts.get("simsun-extB"));
        fontMapper.put("仿宋", PhysicalFonts.get("FangSong"));
        fontMapper.put("仿宋_GB2312", PhysicalFonts.get("FangSong_GB2312"));
        fontMapper.put("幼圆", PhysicalFonts.get("YouYuan"));
        fontMapper.put("华文宋体", PhysicalFonts.get("STSong"));
        fontMapper.put("华文中宋", PhysicalFonts.get("STZhongsong"));

        mlPackage.setFontMapper(fontMapper);
    }
}
