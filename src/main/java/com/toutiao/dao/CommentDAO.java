package com.toutiao.dao;

import com.toutiao.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = " content,entity_id,entity_type,created_date,user_id,status ";
    String SELECT_FIELDS = " id,"+INSERT_FIELDS;
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{content},#{entityId},#{entityType},#{createdDate},#{userId},#{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} order by id desc "})
    List<Comment> selectByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    List<Comment> selectById(@Param("id") int id);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Delete({"delete from ",TABLE_NAME," where id=#{id}"})
    void deleteComment(@Param("id") int id);
}
