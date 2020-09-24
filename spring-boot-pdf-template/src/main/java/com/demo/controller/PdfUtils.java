package com.demo.controller;

import cn.hutool.core.date.DateUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.util.*;

/**
 * 生成pdf的工具类
 */
public class PdfUtils
{
    public static void main(String[] args) throws IOException
    {
        System.out.println(DateUtil.now());
        //Map中Key对应PDF表单中的fieldNames，Value则是你想填充的值
        HashMap map = new HashMap<String, String>();
        map.put("number","2020");
        map.put("sex","On");	//Value为On 则勾选这个复选框
        map.put("test","On");	//Value为On 则勾选这个复选框
        map.put("fill_1","被检查单位123232");
        map.put("fill_18","有一小鸟，它的家搭在最高的树枝上，它的羽毛还未丰满，不能要飞，每日只在家里叽叽地叫着，和两只老鸟说着话儿，他们都是觉得非常的快乐。这一天早晨，它醒了，那两个老鸟都找食物去了。一看见火红的太阳，它们又害怕了，因为太阳太大了，它们又看见一棵树上的一片好大的树叶，树叶上又有站着一只小鸟，正在吃害虫，害虫吃了很多树叶，让大树不能长大，大树是我们的好朋友，每一棵树都产生氧气，让我们每一个人呼吸。这时老鸟马上飞过去，与小鸟一起吃害虫，吃得饱饱的，并为民除害。");
        String sourceFile = "D:\\temp\\01\\template.pdf"; //原文件路径
        String targetFile = "D:\\temp\\01\\output.pdf"; 	//目标文件路径
        PdfUtils.genPdf(map,sourceFile,targetFile);
        System.out.println(DateUtil.now());
    }

    /**
     * @param map 需要填充的字段
     * @param sourceFile  原文件路径
     * @param targetFile  目标文件路径
     * @throws IOException
     */
    public static void genPdf(HashMap map, String sourceFile, String targetFile) throws IOException {
        File templateFile = new File(sourceFile);
        fillParam(map, FileUtils.readFileToByteArray(templateFile), targetFile);
    }

    /**
     * Description: 使用map中的参数填充pdf，map中的key和pdf表单中的field对应 <br>
     * @author mk
     * @Date 2018-11-2 15:21 <br>
     * @Param
     * @return
     */
    public static void fillParam(Map<String, String> fieldValueMap, byte[] file, String contractFileName) {
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(contractFileName);
            PdfReader reader = null;
            PdfStamper stamper = null;
            BaseFont base = null;
            try {
                reader = new PdfReader(file);
                stamper = new PdfStamper(reader, fos);
                stamper.setFormFlattening(true);
                base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED); //简体中文字体
                //base = BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);  //繁体中文字体
                AcroFields acroFields = stamper.getAcroFields();
                for (String key : acroFields.getFields().keySet()) {
                    acroFields.setFieldProperty(key, "textfont", base, null);
                    acroFields.setFieldProperty(key, "textsize", new Float(9), null);   //字体大小
                }
                if (fieldValueMap != null) {
                    for (String fieldName : fieldValueMap.keySet())
                    {
                        if (StringUtils.isNotBlank(fieldValueMap.get(fieldName)))
                        {
                            //获取map中key对应的Value是否为On，若是则勾选复选框
                            if (fieldValueMap.get(fieldName).equals("On") || fieldValueMap.get(fieldName) == "On")
                            {
                                acroFields.setField(fieldName, fieldValueMap.get(fieldName),true);
                            }else
                            {
                                acroFields.setField(fieldName, fieldValueMap.get(fieldName));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stamper != null) {
                    try {
                        stamper.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (reader != null) {
                    reader.close();
                }
            }

        } catch (Exception e) {
            System.out.println("填充参数异常");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }


    /**
     * Description: 获取pdf表单中的fieldNames<br>
     * @author mk
     * @Date 2018-11-2 15:21 <br>
     * @Param
     * @return
     */
    public static Set<String> getTemplateFileFieldNames(String pdfFileName) {
        Set<String> fieldNames = new TreeSet<String>();
        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfFileName);
            Set<String> keys = reader.getAcroFields().getFields().keySet();
            for (String key : keys) {
                int lastIndexOf = key.lastIndexOf(".");
                int lastIndexOf2 = key.lastIndexOf("[");
                fieldNames.add(key.substring(lastIndexOf != -1 ? lastIndexOf + 1 : 0, lastIndexOf2 != -1 ? lastIndexOf2 : key.length()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return fieldNames;
    }


    /**
     * Description: 读取文件数组<br>
     * @author mk
     * @Date 2018-11-2 15:21 <br>
     * @Param
     * @return
     */
    public static byte[] fileBuff(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            //System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] file_buff = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < file_buff.length && (numRead = fi.read(file_buff, offset, file_buff.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != file_buff.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return file_buff;
    }

    /**
     * Description: 合并pdf <br>
     * @author mk
     * @Date 2018-11-2 15:21 <br>
     * @Param
     * @return
     */
    public static void mergePdfFiles(String[] files, String savepath) {
        Document document = null;
        try {
            document = new Document(); //默认A4大小
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(savepath));
            document.open();
            for (int i = 0; i < files.length; i++) {
                PdfReader reader = null;
                try {
                    reader = new PdfReader(files[i]);
                    int n = reader.getNumberOfPages();
                    for (int j = 1; j <= n; j++) {
                        document.newPage();
                        PdfImportedPage page = copy.getImportedPage(reader, j);
                        copy.addPage(page);
                    }
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭PDF文档流，OutputStream文件输出流也将在PDF文档流关闭方法内部关闭
            if (document != null) {
                document.close();
            }

        }
    }
}

