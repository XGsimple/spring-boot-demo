package com.xkcoding.component;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xugangq
 * @date 2021/6/8 20:19
 */
@Data
@NoArgsConstructor
public class User {
    private Integer id;
    private String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String name) {
        this.name = name;
    }

    //省略 getter/setter
}
