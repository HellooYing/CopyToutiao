package com.toutiao.model;

import java.util.Date;

/**
 * Created by rainday on 16/6/30.
 */
public class News {

  private int id;

  private String title;

  private String link;

  private String image;

  private int like_count;

  private int comment_count;

  private Date created_date;

  private int user_id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public int getLikeCount() {
    return like_count;
  }

  public void setLikeCount(int like_count) {
    this.like_count = like_count;
  }

  public int getCommentCount() {
    return comment_count;
  }

  public void setCommentCount(int comment_count) {
    this.comment_count = comment_count;
  }

  public Date getCreatedDate() {
    return created_date;
  }

  public void setCreatedDate(Date created_date) {
    this.created_date = created_date;
  }

  public int getUserId() {
    return user_id;
  }

  public void setUserId(int user_id) {
    this.user_id = user_id;
  }
}
