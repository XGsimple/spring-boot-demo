package com.xkcoding.mq.rabbitmq.workQueue;

import lombok.Data;

/**
 * @author xugangq
 * @description
 * @createTime 2021-01-24 18:50
 */
@Data
public class SMS {
    private String user;
    private String mobile;
    private String msg;

    public SMS(String user, String mobile, String msg) {
        this.user = user;
        this.mobile = mobile;
        this.msg = msg;
    }
}
