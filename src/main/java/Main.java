import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {


    // main测试
    public static void main(String[] args) throws Exception {
        try {

            File student = new File("C:\\Users\\Administrator\\Desktop\\student_code.txt");

            Scanner sc = new Scanner(new FileReader(student));

            while (sc.hasNext()) {

            }
            //1.注册数据库的驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2.获取数据库连接（里面内容依次是："jdbc:mysql://主机名:端口号/数据库名","用户名","登录密码"）
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/student_grade", "root", "1538933906!");
            //3.需要执行的sql语句（?是占位符，代表一个参数）


            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                //1.注册驱动
                Class.forName("com.mysql.jdbc.Driver");
                //2.获取连接
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3366/bjpowernode", "root", "123");
                //3.获取操作数据库对象
                stmt = conn.createStatement();
                //4.执行sql
                String sql = "select * from student where student_code = " +;
                //5.处理查询结果集
                //executeQuery(selsect)
                rs = stmt.executeQuery(sql);
                //empno,ename,sal代表查询结果集的列名
                while (rs.next()) {
                    String empno = rs.getString("empno");
                    String ename = rs.getString("ename");
                    String sal = rs.getString("sal");
                    System.out.println(empno + "," + ename + "," + sal);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                //6.释放资源
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }


            // 1.新建document对象
            Document document = new Document(PageSize.A4, 20, 20, 16, 26);// 建立一个Document对象

            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File("test.pdf");
            file.createNewFile();

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
//            writer.setPageEvent(new Watermark("B18060606"));// 水印
            writer.setPageEvent(new MyHeaderFooter());// 页眉/页脚


            // 3.打开文档
            document.open();
//            document.addTitle("Title@PDF-Java");// 标题
//            document.addAuthor("Author@umiz");// 作者
//            document.addSubject("Subject@iText pdf sample");// 主题
//            document.addKeywords("Keywords@iTextpdf");// 关键字
//            document.addCreator("Creator@umiz`s");// 创建者


            // 4.向文档中添加内容
            new Main().generatePDF(document);

            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 定义全局的字体静态变量
    private static Font titlefont;
    private static Font headfont;
    private static Font keyfont;
    private static Font textfont;
    private static Font tablefont;
    // 最大宽度
    private static int maxWidth = 520;

    // 静态代码块
    static {
        try {
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            titlefont = new Font(bfChinese, 16, Font.BOLD);
            headfont = new Font(bfChinese, 14, Font.BOLD);
            keyfont = new Font(bfChinese, 10, Font.BOLD);
            textfont = new Font(bfChinese, 10, Font.NORMAL);
            tablefont = new Font(bfChinese, 8, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 生成PDF文件
    public void generatePDF(Document document) throws Exception {


        // 段落
        Paragraph paragraph = new Paragraph("西安工业大学北方信息工程学院毕业生成绩单", titlefont);
        paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
        paragraph.setIndentationLeft(12); //设置左缩进
        paragraph.setIndentationRight(12); //设置右缩进
        paragraph.setFirstLineIndent(24); //设置首行缩进
        paragraph.setLeading(20f); //行间距

        paragraph.setSpacingBefore(25f); //设置段落上空白
        paragraph.setSpacingAfter(25f); //设置段落下空白

        // 直线
        Paragraph p1 = new Paragraph();
        p1.add(new Chunk(new LineSeparator()));

        // 点线
        Paragraph p2 = new Paragraph();
        p2.add(new Chunk(new DottedLineSeparator()));

        // 超链接
        Anchor anchor = new Anchor("baidu");
        anchor.setReference("www.baidu.com");

        // 定位
        Anchor gotoP = new Anchor("goto");
        gotoP.setReference("#top");


        document.add(paragraph);


//        document.add(new Chunk(new LineSeparator()));
        document.add(generalTitleTable());

//        document.add(new LineSeparator());
        document.add(generalBodyTable());

    }

    public PdfPTable generalBodyTable() throws IOException, BadElementException {
        PdfPTable table = createTable(new float[]{25, 40, 100, 40, 25, 25, 50, 1, 25, 40, 100, 40, 25, 25, 50});

//        table.setWidthPercentage(100); // 宽度100%填充
        table.setSpacingBefore(5);//设置段落上空白
        table.setSpacingAfter(5);//设置段落上下空白
        table.setHorizontalAlignment(Element.ALIGN_CENTER);//居中
        table.setTotalWidth(PageSize.A4.getWidth() - 20);
        table.setLockedWidth(true);

        for (int i = 0; i < 2; i++) {
            table.addCell(createCell("序号", tablefont, 1, new float[]{1, 1, 1, 1}, new float[]{10, 10}, false));
            table.addCell(createCell("课程编号", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{5, 5}, false));
            table.addCell(createCell("课程名称", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{5, 5}, false));
            table.addCell(createCell("课程属性", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{5, 5}, false));
            table.addCell(createCell("学分", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{5, 5}, false));
            table.addCell(createCell("成绩", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{5, 5}, false));
            table.addCell(createCell("学期", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{5, 5}, false));
            if (i == 0) {
                PdfPCell cell = new PdfPCell();
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthRight(0);
                cell.setBorderWidthTop(0);
                cell.setBorderWidthBottom(0);
                table.addCell(cell);
            }
        }


        for (int i = 0; i < 80; i++) {
            table.addCell(createCell(String.valueOf(i + 1), tablefont, 1, new float[]{1, 1, 1, 1}, new float[]{4, 4}, false));
            table.addCell(createCell("课程编号", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{4, 4}, false));
            table.addCell(createCell("课程名称课课程名称课", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{4, 4}, false));
            table.addCell(createCell("必修", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{4, 4}, false));
            table.addCell(createCell("1", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{4, 4}, false));
            table.addCell(createCell("成绩", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{4, 4}, false));
            table.addCell(createCell("2015-2016(1)", tablefont, 1, new float[]{0, 1, 1, 1}, new float[]{4, 4}, false));

            if (i % 2 == 0) {
                PdfPCell cell = new PdfPCell();
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthRight(0);
                cell.setBorderWidthTop(0);
                cell.setBorderWidthBottom(0);
                table.addCell(cell);
            }
        }


        return table;

    }


    public PdfPTable generalTitleTable() throws IOException, BadElementException {

        // 添加图片
        Image image = Image.getInstance("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp0.meituan.net%2Fdpdeal%2F503a7513b6c64c0ee3bdfecf27d62d9f508288.jpg&refer=http%3A%2F%2Fp0.meituan.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626085324&t=e4501a19467627903794211de4951d9a");
        image.setAlignment(Image.ALIGN_CENTER);


//        image.scalePercent(30); //依照比例缩放


        PdfPTable table = createTable(new float[]{150, 150, 150, 150, 100});
        table.setHorizontalAlignment(Element.ALIGN_CENTER);//居中
        table.setTotalWidth(PageSize.A4.getWidth() - 26);
        table.setLockedWidth(true);


        table.addCell(createCell("学号:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));
        table.addCell(createCell("姓名:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));
        table.addCell(createCell("性别:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));
        table.addCell(createCell("学制:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));
        PdfPCell cell = new PdfPCell();
        cell.setImage(image);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0);
        cell.setRowspan(4);// 4行
        cell.setColspan(1);// 1 列

        cell.setFixedHeight(40);  //放小照片
        table.addCell(cell);


        table.addCell(createCell("入学日期:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));
        table.addCell(createCell("二级学院:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));
        table.addCell(createCell("出生日期:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));
        table.addCell(createCell("毕业结论:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));

        table.addCell(createCell("毕业日期:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));
        table.addCell(createCell("专业名称:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));
        table.addCell(createCell("学位类别:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));
        table.addCell(createCell("班级名称:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false));


        PdfPCell cell1 = createCell("毕(结)业证书编号:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false);
        cell1.setVerticalAlignment(Element.ALIGN_CENTER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setRowspan(1);//
        cell1.setColspan(2);//
        table.addCell(cell1);
        PdfPCell cell2 = createCell("学位证书编号:", textfont, 0, new float[]{0, 0, 0, 0}, new float[]{4, 4}, false);
        cell2.setVerticalAlignment(Element.ALIGN_CENTER);
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2.setRowspan(1);//
        cell2.setColspan(2);//
        table.addCell(cell2);
        return table;
    }


/**------------------------创建表格单元格的方法start----------------------------*/
    /**
     * 创建单元格(指定字体)
     *
     * @param value
     * @param font
     * @return
     */
    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平..）
     *
     * @param value
     * @param font
     * @param align
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并）
     *
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并、设置单元格内边距）
     *
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @param boderFlag
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(15.0f);
        }
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平..、边框宽度：0表示无边框、内边距）
     *
     * @param value
     * @param font
     * @param align
     * @param borderWidth
     * @param paddingSize
     * @param flag
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, float[] borderWidth, float[] paddingSize, boolean flag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        cell.setBorderWidthLeft(borderWidth[0]);
        cell.setBorderWidthRight(borderWidth[1]);
        cell.setBorderWidthTop(borderWidth[2]);
        cell.setBorderWidthBottom(borderWidth[3]);
        cell.setPaddingTop(paddingSize[0]);
        cell.setPaddingBottom(paddingSize[1]);
        if (flag) {
            cell.setColspan(2);
        }
        return cell;
    }
/**------------------------创建表格单元格的方法end----------------------------*/


/**--------------------------创建表格的方法start------------------- ---------*/
    /**
     * 创建默认列宽，指定列数、水平(居中、右、左)的表格
     *
     * @param colNumber
     * @param align
     * @return
     */
    public PdfPTable createTable(int colNumber, int align) {
        PdfPTable table = new PdfPTable(colNumber);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(align);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    /**
     * 创建指定列宽、列数的表格
     *
     * @param widths
     * @return
     */
    public PdfPTable createTable(float[] widths) {
        PdfPTable table = new PdfPTable(widths);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    /**
     * 创建空白的表格
     *
     * @return
     */
    public PdfPTable createBlankTable() {
        PdfPTable table = new PdfPTable(1);
        table.getDefaultCell().setBorder(0);
        table.addCell(createCell("", keyfont));
        table.setSpacingAfter(20.0f);
        table.setSpacingBefore(20.0f);
        return table;
    }
/**--------------------------创建表格的方法end------------------- ---------*/


}


class MyHeaderFooter extends PdfPageEventHelper {
    // 总页数
    PdfTemplate totalPage;
    Font hfFont;

    {
        try {
            hfFont = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 8, Font.NORMAL);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 打开文档时，创建一个总页数的模版
    public void onOpenDocument(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        totalPage = cb.createTemplate(30, 16);
    }

    // 一页加载完成触发，写入页眉和页脚
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable table = new PdfPTable(2);
        try {
            table.setTotalWidth(PageSize.A4.getWidth() - 100);
            table.setWidths(new int[]{24, 24});
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(-10);
//            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            table.getDefaultCell().disableBorderSide(15);//边框线全没了

            table.addCell(new Paragraph("制表单位：教务处", hfFont));// 可以直接使用addCell(str)，不过不能指定字体，中文无法显示


            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
//
            table.addCell(new Paragraph("Time：2021/6/12", hfFont));
            // 总页数
//            PdfPCell cell = new PdfPCell(Image.getInstance(totalPage));
//            cell.setBorder(Rectangle.BOTTOM);


            // 将页眉写到document中，位置可以指定，指定到下面就是页脚 y post 相当于margin-bottom
            table.writeSelectedRows(0, -1, 50, 20, writer.getDirectContent());
        } catch (Exception de) {
            throw new ExceptionConverter(de);
        }
    }


}


class Watermark extends PdfPageEventHelper {
    Font FONT = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD, new GrayColor(0.95f));
    private String waterCont;//水印内容

    public Watermark() {

    }

    public Watermark(String waterCont) {
        this.waterCont = waterCont;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                ColumnText.showTextAligned(writer.getDirectContentUnder(),
                        Element.ALIGN_CENTER,
                        new Phrase(this.waterCont == null ? "HELLO WORLD" : this.waterCont, FONT),
                        (50.5f + i * 350),
                        (40.0f + j * 150),
                        writer.getPageNumber() % 2 == 1 ? 45 : -45);
            }
        }
    }
}
