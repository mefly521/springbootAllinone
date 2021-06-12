package poi.word;


import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import poi.word.Dbutil;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;


/**
 * 将mysql数据库表结构导出成word
 */
public class WordExportFromDb {

    public static void main(String[] args) throws Exception {
        //Blank Document
        XWPFDocument document = new XWPFDocument();

        //Write the Document in file system
        FileOutputStream out = new FileOutputStream(new File("d:/数据字典.docx"));

        //添加标题
        XWPFParagraph titleParagraph = document.createParagraph();
        //设置段落居中
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleParagraphRun = titleParagraph.createRun();
        titleParagraphRun.setText("数据字典");
        titleParagraphRun.setColor("000000");
        titleParagraphRun.setFontSize(20);

        Dbutil dbutil = new Dbutil();
        List<Map> tables = dbutil.getColumnInfo();

        for (Map map : tables) {
            createTable(document, map);
        }

        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);

        //添加页眉
        CTP ctpHeader = CTP.Factory.newInstance();
        CTR ctrHeader = ctpHeader.addNewR();
        CTText ctHeader = ctrHeader.addNewT();
        String headerText = "Java POI create MS word file.";
        ctHeader.setStringValue(headerText);
        XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, document);
        //设置为右对齐
        headerParagraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFParagraph[] parsHeader = new XWPFParagraph[1];
        parsHeader[0] = headerParagraph;
        policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);


        //添加页脚
        CTP ctpFooter = CTP.Factory.newInstance();
        CTR ctrFooter = ctpFooter.addNewR();
        CTText ctFooter = ctrFooter.addNewT();
        String footerText = "http://blog.csdn.net/zhouseawater";
        ctFooter.setStringValue(footerText);
        XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, document);
        headerParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFParagraph[] parsFooter = new XWPFParagraph[1];
        parsFooter[0] = footerParagraph;
        policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);


        document.write(out);
        out.close();
        System.out.println("create_table document written success.");
    }

    private static void createTable(XWPFDocument document, Map tables) {

        String tableName = tables.get("tableName").toString();
        String tableComment = tables.get("tableComment").toString();

        List<Map> listColumn = (List<Map>) tables.get("column");
        //换行
        br(document);
        br(document);

        //段落
        XWPFParagraph firstParagraph = document.createParagraph();
        XWPFRun run = firstParagraph.createRun();
        run.setText(tableName);
        run.setFontSize(16);
        run.setBold(true);

        br(document);

        //段落
        XWPFParagraph paragraph2 = document.createParagraph();
        XWPFRun run2 = paragraph2.createRun();
        run2.setText("描述: "+tableComment);
        run2.setFontSize(12);

        //工作经历表格
        XWPFTable xTable = document.createTable(listColumn.size()+1, 6);
//        //列宽自动分割
//        CTTblWidth comTableWidth = xTable.getCTTbl().addNewTblPr().addNewTblW();
//        comTableWidth.setType(STTblWidth.DXA);
//        comTableWidth.setW(BigInteger.valueOf(6000));

        CTTbl ttbl = xTable.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
        tblWidth.setW(new BigInteger("6000"));
        tblWidth.setType(STTblWidth.DXA);

        String bgColor = "111111";
        String bgColorTitle = "333333";
        setCellTitleText(document, getCellHight(xTable, 0, 0), "序号", bgColorTitle, 1000);
        setCellTitleText(document, getCellHight(xTable, 0, 1), "字段名称", bgColor, 2000);
        setCellTitleText(document, getCellHight(xTable, 0, 2), "是否为空", bgColor, 1000);
        setCellTitleText(document, getCellHight(xTable, 0, 3), "字段类型", bgColor, 2000);
        setCellTitleText(document, getCellHight(xTable, 0, 4), "是否主键", bgColor, 1000);
        setCellTitleText(document, getCellHight(xTable, 0, 5), "注释", bgColor, 2000);
        System.out.println(tableName);
        int number = 0;
        for (Map cloumn : listColumn) {
            number++;
            setCellContentText(document, getCellHight(xTable, number, 0), number + "", bgColor, 1000);
            setCellContentText(document, getCellHight(xTable, number, 1), cloumn.get("column_name").toString(), bgColor, 2000);
            setCellContentText(document, getCellHight(xTable, number, 2), cloumn.get("is_nullable").toString(), bgColor, 1000);
            setCellContentText(document, getCellHight(xTable, number, 3), cloumn.get("column_type").toString(), bgColor, 2000);
            setCellContentText(document, getCellHight(xTable, number, 4), cloumn.get("column_key").toString(), bgColor, 1000);
            setCellContentText(document, getCellHight(xTable, number, 5), cloumn.get("column_comment").toString(), bgColor, 2000);
        }
//        //表格标题
//        XWPFTableRow comTableRowOne = ComTable.getRow(0);
//        comTableRowOne.getCell(0).setText("字段名称");
//        comTableRowOne.addNewTableCell().setText("是否为空");
//        comTableRowOne.addNewTableCell().setText("字段类型");
//        comTableRowOne.addNewTableCell().setText("是否主键");
//        comTableRowOne.addNewTableCell().setText("注释");
//
//        for (Map cloumn:listColumn) {
//            //表格内容
//            XWPFTableRow comTableRowTwo = ComTable.createRow();
//            comTableRowTwo.getCell(0).setText(cloumn.get("column_name").toString());
//            comTableRowTwo.getCell(1).setText(cloumn.get("is_nullable").toString());
//            comTableRowTwo.getCell(2).setText(cloumn.get("column_type").toString());
//            comTableRowTwo.getCell(3).setText(cloumn.get("column_key").toString());
//            comTableRowTwo.getCell(4).setText(cloumn.get("column_comment").toString());
//        }
    }

    private static void br(XWPFDocument document) {
        XWPFParagraph paragraph1 = document.createParagraph();
        XWPFRun paragraphRun1 = paragraph1.createRun();
        paragraphRun1.setText("\r");
    }

    private static void setCellTitleText(XWPFDocument xDocument, XWPFTableCell cell,
                                    String text, String bgcolor, int width) {
        setCellText(xDocument,cell,text,bgcolor,width,true,"DDDDDD");
    }
    private static void setCellContentText(XWPFDocument xDocument, XWPFTableCell cell,
                                         String text, String bgcolor, int width) {
        setCellText(xDocument,cell,text,bgcolor,width,false,"FFFFFF");
    }
    private static void setCellText(XWPFDocument xDocument, XWPFTableCell cell,
                                    String text, String bgcolor, int width,boolean bold,String color) {
        CTTc cttc = cell.getCTTc();
        CTTcPr cellPr = cttc.addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
        XWPFParagraph pIO = cell.addParagraph();
        cell.removeParagraph(0);
        cell.setColor(color);
        XWPFRun rIO = pIO.createRun();
        rIO.setFontFamily("微软雅黑");
        rIO.setColor("000000");
        rIO.setFontSize(12);
        rIO.setBold(bold);
        rIO.setText(text);
    }

    //设置表格高度
    private static XWPFTableCell getCellHight(XWPFTable xTable, int rowNomber, int cellNumber) {
        XWPFTableRow row = null;
        row = xTable.getRow(rowNomber);
        row.setHeight(100);
        XWPFTableCell cell = null;
        cell = row.getCell(cellNumber);
        return cell;
    }

}
