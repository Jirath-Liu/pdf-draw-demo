package org.kelab.pdf.pdfdrawdemo.controller;

import org.kelab.pdf.pdfdrawdemo.controller.vo.BaseVo;
import org.kelab.pdf.pdfdrawdemo.service.DrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 绘制pdf，接收图片流，合并在pdf中
 * @author JirathLiu
 * @date 2021/5/5
 * @description:
 */
@RestController
public class DrawController {

    @Autowired
    private DrawService drawService;

    @RequestMapping(value = "/draw",method = RequestMethod.POST)
    public BaseVo drawPdf(@RequestParam("image")MultipartFile image,
                          @RequestParam int pageNum,
                          @RequestParam int top, @RequestParam int left,
                          @RequestParam int width, @RequestParam int height) {
        return drawService.draw(image, pageNum, width, height, left, top);
    }

    @RequestMapping(value = "/getPdf",method = RequestMethod.GET)
    public BaseVo getPdf(HttpServletResponse httpServletResponse){
        return drawService.getPdf(httpServletResponse);
    }

}
