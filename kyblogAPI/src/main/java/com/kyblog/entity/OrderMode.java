package com.kyblog.entity;

/**
 * @program: kyblog
 * @description: 排序模式类型
 * @author: Kim_yuan
 * @create: 2021-04-23 12:31
 **/

public class OrderMode {
    private String column;
    private String dir="desc";

    @Override
    public String toString() {
        return "OrderMode{" +
                "column='" + column + '\'' +
                ", dir='" + dir + '\'' +
                '}';
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
