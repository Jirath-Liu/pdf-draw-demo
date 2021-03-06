package org.kelab.pdf.pdfdrawdemo.service;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.annotations.PdfRubberStampAnnotation;
import com.spire.pdf.annotations.appearance.PdfAppearance;
import com.spire.pdf.graphics.PdfImage;
import com.spire.pdf.graphics.PdfTemplate;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.kelab.pdf.pdfdrawdemo.controller.vo.BaseVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author JirathLiu
 * @date 2021/5/5
 * @description:
 */
@Service
public class DrawServiceImpl implements DrawService, InitializingBean {
    @Value("${pdf-loc}")
    private String pdfLoc;

    private Lock lock;

    @Override
    public BaseVo draw(MultipartFile imageFile,int pageNum,
                       int width,int height,
                       int left,int top) {
        lock.lock();
        try {
            doDraw(imageFile, pageNum, width, height, left, top);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseVo(500, "failed");
        }finally {
            lock.unlock();
        }
        return new BaseVo(200,"draw");
    }

    private static final int scale=10000;
    public void doDraw(MultipartFile imageFile,int pageNum,
                       int width,int height,
                       float left,float top){
        PdfDocument doc=new PdfDocument();
        FileOutputStream fileOutputStream = null;
        try {
            System.out.println("draw");
            File pdf=ResourceUtils.getFile(pdfLoc);
            BufferedInputStream bufIn = new BufferedInputStream(new FileInputStream(pdf));
            doc.loadFromStream(bufIn);
            //?????????????????????
            PdfPageBase page = doc.getPages().get(pageNum);
            //??????????????????
            PdfImage image = PdfImage.fromStream(imageFile.getInputStream());
            //????????????????????????????????????
            float trueLeft=(float) (page.getActualSize().getHeight()*left)/scale;
            float trueTop=(float) (page.getActualSize().getWidth()*top)/scale;
            //??????PdfTemplate??????
            PdfTemplate template = new PdfTemplate(width, height);
            //????????????????????????
            template.getGraphics().drawImage(image, 0, 0, width, height);
            //??????PdfRubebrStampAnnotation??????????????????????????????
            Rectangle2D rect = new Rectangle2D.Float(
                    //?????????????????????
                    trueLeft,
                    //?????????????????????
                    trueTop,
                    width, height);
            PdfRubberStampAnnotation stamp = new PdfRubberStampAnnotation(rect);
            //??????PdfAppearance??????
            PdfAppearance pdfAppearance = new PdfAppearance(stamp);
            //??????????????????PdfAppearance???????????????
            pdfAppearance.setNormal(template);
            //???PdfAppearance ????????????????????????
            stamp.setAppearance(pdfAppearance);
            //???????????????PDF
            page.getAnnotationsWidget().add(stamp);
            //????????????
            File amb=ResourceUtils.getFile(pdfLoc);
            fileOutputStream=new FileOutputStream(amb);
            BufferedOutputStream bufOut = new BufferedOutputStream(fileOutputStream);
            doc.saveToStream(bufOut, FileFormat.PDF);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public BaseVo getPdf(HttpServletResponse httpServletResponse) {
        try {
            fileDownLoad(httpServletResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseVo(500, "failed");
        }
        return new BaseVo(200, "get");
    }

    private void fileDownLoad(HttpServletResponse response){
        lock.lock();
        try {
            File file = ResourceUtils.getFile(pdfLoc);
            if(!file.exists()){
                throw new RuntimeException("file not exist.");
            }
            response.reset();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName() );

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buff = new byte[1024];
            OutputStream os  = response.getOutputStream();
            int i;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void afterPropertiesSet() {
        lock=new ReentrantLock();
    }
}
