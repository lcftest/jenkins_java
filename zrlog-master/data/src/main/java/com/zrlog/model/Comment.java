package com.zrlog.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.zrlog.common.rest.request.PageRequest;
import com.zrlog.data.dto.PageData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 对应数据里面的comment表，用于存放文章对应的评论信息。
 */
public class Comment extends Model<Comment> {
    public static final String TABLE_NAME = "comment";
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public PageData<Comment> find(PageRequest page) {
        PageData<Comment> data = new PageData<>();
        String sql = "select commentId as id,userComment,header,commTime,userMail,userHome,userIp,userName,hide,logId from " + TABLE_NAME + " order by commTime desc limit ?,?";
        data.setRows(find(sql, page.getOffset(), page.getSize()));
        ModelUtil.fillPageData(this, "from " + TABLE_NAME, data, new Object[0]);
        return data;
    }

    public Long count() {
        String sql = "select count(1) from " + TABLE_NAME;
        return Db.findFirst(sql).get("count(1)");
    }

    public Long countToDayComment() {
        String sql = "select count(1) from " + TABLE_NAME + " where DATE_FORMAT(commTime,'%Y_%m_%d')=?";
        return Db.findFirst(sql, new SimpleDateFormat("yyyy_MM_dd").format(new Date())).get("count(1)");
    }

    public List<Comment> findHaveReadIsFalse() {
        String sql = "select commentId as id,userComment,header,userMail,userHome,userIp,userName,hide,logId from " + TABLE_NAME + " where have_read = ?  order by commTime desc ";
        return find(sql, false);
    }

    public List<Comment> findAllByLogId(int logId) {
        return find("select * from " + TABLE_NAME + " where logId=?", logId);
    }

    public void doRead(long id) {
        Db.update("update " + TABLE_NAME + " set have_read = ? where commentId = ? ", true, id);
    }
}
