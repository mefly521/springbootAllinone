package poi.word;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public class ExportWordDemo {

    public static void main(String[] args) throws Exception {
        getWord();
    }
    public static void getWord() throws Exception {
        String[] deleteStr = null;
        XWPFDocument xdoc = new XWPFDocument();
        //标题
        XWPFParagraph titleMes = xdoc.createParagraph();
        titleMes.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r1 = titleMes.createRun();
        r1.setBold(true);
        r1.setFontFamily("微软雅黑");
        r1.setText("XXX的计划及进度报告");//活动名称
        r1.setFontSize(20);
        r1.setColor("333333");
        r1.setBold(true);
        //标题信息
        XWPFParagraph userMes = xdoc.createParagraph();//设置活动主题
        userMes.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r2=userMes.createRun();
        r2.setText("标题信息");
        r2.setFontSize(14);
        r2.setFontFamily("微软雅黑");
        r2.setColor("a6a6a6");
        //温馨提示
        XWPFParagraph hintMes = xdoc.createParagraph();
        hintMes.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun runHint = hintMes.createRun();
        runHint.setText("温馨提示：本文档分为工作清单和工作明细两部分。");
        runHint.setFontSize(13);
        runHint.setFontFamily("微软雅黑");
        runHint.setColor("a6a6a6");
        //标题一：计划清单
        XWPFParagraph headLine1 = xdoc.createParagraph();
        headLine1.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun runHeadLine1 = headLine1.createRun();
        runHeadLine1.setText("一、计划清单");
        runHeadLine1.setFontSize(14);
        runHeadLine1.setFontFamily("微软雅黑");
        runHeadLine1.setColor("a6a6a6");
        XWPFTable dTable = xdoc.createTable(5, 3);
        createTable(dTable, xdoc);
        setEmptyRow(xdoc,r1);

        //标题二：计划清单
        XWPFParagraph headLine2 = xdoc.createParagraph();
        headLine2.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun runHeadLine2 = headLine2.createRun();
        runHeadLine2.setText("二、计划明细");
        runHeadLine2.setFontSize(14);
        runHeadLine2.setFontFamily("微软雅黑");
        runHeadLine2.setColor("a6a6a6");
        //表格
        for (int i=0;i<4;i++) {
            XWPFTable xTable = xdoc.createTable(7, 2);
            createSimpleTable(xTable, xdoc);
            setEmptyRow(xdoc, r1);
        }
        // 在服务器端生成
        FileOutputStream fos = new FileOutputStream( "d:\\测试.docx");
        xdoc.write(fos);
        fos.close();
    }

    public static void createTable(XWPFTable xTable,XWPFDocument xdoc){
        String bgColor="111111";
        CTTbl ttbl = xTable.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
        tblWidth.setW(new BigInteger("8600"));
        tblWidth.setType(STTblWidth.DXA);
        setCellText(xdoc, getCellHight(xTable, 0, 0), "序号",bgColor,1000);
        setCellText(xdoc, getCellHight(xTable, 0, 1), "阶段",bgColor,3800);
        setCellText(xdoc, getCellHight(xTable, 0, 2), "计划工作任务",bgColor,3800);
        int number=0;
        for(int i=1;i<5;i++){
            number++;
            setCellText(xdoc, getCellHight(xTable, number,0), number+"",bgColor,1000);
            setCellText(xdoc, getCellHight(xTable, number,1), "阶段名称",bgColor,3800);
            setCellText(xdoc, getCellHight(xTable, number,2), "任务名称",bgColor,3800);
        }
    }

    //设置表格高度
    private static XWPFTableCell getCellHight(XWPFTable xTable,int rowNomber,int cellNumber){
        XWPFTableRow row = null;
        row = xTable.getRow(rowNomber);
        row.setHeight(100);
        XWPFTableCell cell = null;
        cell = row.getCell(cellNumber);
        return cell;
    }

    /**
     *
     * @param xDocument
     * @param cell
     * @param text
     * @param bgcolor
     * @param width
     */
    private static void setCellText(XWPFDocument xDocument, XWPFTableCell cell,
                                    String text, String bgcolor, int width) {
        CTTc cttc = cell.getCTTc();
        CTTcPr cellPr = cttc.addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
        XWPFParagraph pIO =cell.addParagraph();
        cell.removeParagraph(0);
        XWPFRun rIO = pIO.createRun();
        rIO.setFontFamily("微软雅黑");
        rIO.setColor("000000");
        rIO.setFontSize(12);
        rIO.setBold(true);
        rIO.setText(text);
    }

    //设置表格间的空行
    public static void setEmptyRow(XWPFDocument xdoc,XWPFRun r1){
        XWPFParagraph p1 = xdoc.createParagraph();
        p1.setAlignment(ParagraphAlignment.CENTER);
        p1.setVerticalAlignment(TextAlignment.CENTER);
        r1 = p1.createRun();
    }

    /**
     * 创建计划明细表
     * @param task
     * @param xTable
     * @param xdoc
     * @throws IOException
     */
    public static void createSimpleTable(XWPFTable xTable, XWPFDocument xdoc)
            throws IOException {
        String bgColor="FFFFFF";
        CTTbl ttbl = xTable.getCTTbl();
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
        tblWidth.setW(new BigInteger("8600"));
        tblWidth.setType(STTblWidth.DXA);
        for (int i = 0; i < 7; i++) {
            setCellText(xdoc, getCellHight(xTable, i, 0), "阶段",bgColor,3300);
            setCellText(xdoc,getCellHight(xTable, i, 1), "阶段名称",bgColor,3300);
        }
    }
}
