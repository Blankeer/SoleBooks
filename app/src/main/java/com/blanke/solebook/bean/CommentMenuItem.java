package com.blanke.solebook.bean;

/**
 * Created by blanke on 16-4-10.
 */
public class CommentMenuItem {
    public static enum OP {
        DELETE, SHOW, REPLY
    }

    private OP op;//操作类型
    private String title;

    public CommentMenuItem(OP op, String title) {
        this.op = op;
        this.title = title;
    }

    public OP getOp() {
        return op;
    }

    public void setOp(OP op) {
        this.op = op;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
