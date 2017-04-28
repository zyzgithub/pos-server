package com.dianba.pos.menu.po;

public class OrderWithBLOBs extends Order {
    private String rereason;

    private String remark;

    private String commentContent;

    public String getRereason() {
        return rereason;
    }

    public void setRereason(String rereason) {
        this.rereason = rereason == null ? null : rereason.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent == null ? null : commentContent.trim();
    }
}