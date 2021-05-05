package org.kelab.pdf.pdfdrawdemo.controller.vo;

import lombok.Data;

/**
 * @author JirathLiu
 * @date 2021/5/5
 * @description:
 */
@Data
public class BaseVo<T> {
    private Integer code;
    private T data;
    private String msg;

    public BaseVo(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }
}
