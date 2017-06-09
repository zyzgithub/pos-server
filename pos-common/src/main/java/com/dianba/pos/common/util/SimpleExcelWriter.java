package com.dianba.pos.common.util;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zc on 2016/11/25.
 */
public class SimpleExcelWriter {

    private static class SimpleWritableWorkbookBuilder {
        private List<String> titles;
        private List<List<String>> datas = new LinkedList<List<String>>();

        public SimpleWritableWorkbookBuilder(List<String> titles, List<List<String>> datas) {
            this.titles = titles;
            this.datas = datas;
        }

        public void buildWorkBook(OutputStream os) throws WriteException, IOException {
            //创建工作薄
            WritableWorkbook workbook = Workbook.createWorkbook(os);
            //创建新的一页
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
            if (this.titles != null) {
                for (int i = 0; i < titles.size(); i++) {
                    Label label = new Label(i, 0, titles.get(i));
                    sheet.addCell(label);
                }
            }
            if (this.datas != null) {
                for (int row = 0; row < datas.size(); row++) {
                    List<String> data = datas.get(row);
                    for (int column = 0; column < data.size(); column++) {
                        Label label = new Label(column, row + 1, data.get(column));
                        sheet.addCell(label);
                    }

                }
            }
            //把创建的内容写入到输出流中，并关闭输出流
            workbook.write();
            workbook.close();
            os.close();
        }
    }


    public static byte[] getWorkbookBytes(List<String> titles, List<List<String>> datas) throws IOException, WriteException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SimpleWritableWorkbookBuilder simpleWritableWorkbookBuilder = new SimpleWritableWorkbookBuilder(titles, datas);
        simpleWritableWorkbookBuilder.buildWorkBook(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

//    public static void main(String s[]) throws IOException, WriteException {
//        String filePath = "/Users/zc/Desktop/demo.xls";
//        OutputStream os = new FileOutputStream(new File(filePath));
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////        createExcel(os);
//        SimpleWritableWorkbookBuilder b = new SimpleWritableWorkbookBuilder(Arrays.asList("a", "b", "c"), Arrays.asList(Arrays.asList("1", "2", "3"), Arrays.asList("6", "5", "4")));
//        b.buildWorkBook(byteArrayOutputStream);
//
//        EMailClient mail = new EMailClient("zhoucong@0085.com", "2016年11月25日景秀银湾店POS导出商品销售报表", "2016年11月25日景秀银湾店POS导出商品销售报表", "POS导出销售报表附件已发送，请查看！");
////        EMailClient mail = new EMailClient("pengyuanyuan@0085.com", "POS 报表", "报表详情", "是短发是短发");
//
////        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( byteArrayOutputStream.toByteArray());
//        byte[] bs = byteArrayOutputStream.toByteArray();
////        os.write(bs);
//        mail.addAttachfile("销售报表_景秀银湾店2016_11_25.xls", bs);
//        mail.send();
//
//    }
}
