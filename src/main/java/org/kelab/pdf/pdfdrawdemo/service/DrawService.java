package org.kelab.pdf.pdfdrawdemo.service;

import org.kelab.pdf.pdfdrawdemo.controller.vo.BaseVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author JirathLiu
 * @date 2021/5/5
 * @description:
 */
public interface DrawService {
    /**
     * 绘制
     * @param imageFile 图片文件
     * @param pageNum 页码
     * @param width 宽度
     * @param height 高度
     * @param left 左
     * @param top 右
     * @return
     */
    BaseVo draw(MultipartFile imageFile,int pageNum,
                int width,int height,
                int left,int top);

    /**
     * 获取
     * @param httpServletResponse
     * @return
     */
    BaseVo getPdf(HttpServletResponse httpServletResponse);
}
