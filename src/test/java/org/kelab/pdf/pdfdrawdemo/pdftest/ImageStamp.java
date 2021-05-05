package org.kelab.pdf.pdfdrawdemo.pdftest;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.annotations.PdfRubberStampAnnotation;
import com.spire.pdf.annotations.appearance.PdfAppearance;
import com.spire.pdf.graphics.PdfImage;
import com.spire.pdf.graphics.PdfTemplate;

import java.awt.geom.Rectangle2D;


public class ImageStamp {
    public static void main(String[] args) {
        //创建PdfDocument对象，加载PDF测试文档
        PdfDocument doc = new PdfDocument();
        doc.loadFromFile("test.pdf");
        //获取PDF文件的页数
        int pageCount = doc.getPages().getCount();
        //循环给PDF添加印章
        for (int i=0; i<pageCount;i++) {
            //获取文档第几页
            PdfPageBase page = doc.getPages().get(i);
            //加载印章图片
            PdfImage image = PdfImage.fromFile("ppp.gif");
            //获取印章图片的宽度和高度
            int width = 254;
            int height = 346;
            //创建PdfTemplate对象
            PdfTemplate template = new PdfTemplate(width, height);
            //将图片绘制到模板
            template.getGraphics().drawImage(image, 0, 0, width, height);
            //创建PdfRubebrStampAnnotation对象，指定大小和位置
            Rectangle2D rect = new Rectangle2D.Float((float) (page.getActualSize().getWidth() - width - 50), (float) (page.getActualSize().getHeight() - height - 100), width, height);
            PdfRubberStampAnnotation stamp = new PdfRubberStampAnnotation(rect);
            //创建PdfAppearance对象
            PdfAppearance pdfAppearance = new PdfAppearance(stamp);
            //将模板应用为PdfAppearance的一般状态
            pdfAppearance.setNormal(template);
            //将PdfAppearance 应用为图章的样式
            stamp.setAppearance(pdfAppearance);
            //添加图章到PDF
            page.getAnnotationsWidget().add(stamp);
        }
        //保存文档
        doc.saveToFile("test-new.pdf",FileFormat.PDF);
    }
}