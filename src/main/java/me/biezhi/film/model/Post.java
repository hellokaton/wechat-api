package me.biezhi.film.model;

import java.io.Serializable;

import blade.plugin.sql2o.Table;

/**
 * Post对象
 */
@Table(value = "t_post", PK = "pid")
public class Post implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer pid;
	
	//标题
	private String title;
	
	//缩略名
	private String slug;
	
	//封面
	private String cover;
	
	//所属分类
	private Integer category_id;
	
	//内容
	private String content;
	
	//浏览量
	private Integer views;
	
	//是否已经删除
	private Boolean is_del;
	
	//发布日期
	private Integer dateline;
	
	public Post(){}
	
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
	
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
	
	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}
	
	public Boolean getIs_del() {
		return is_del;
	}

	public void setIs_del(Boolean is_del) {
		this.is_del = is_del;
	}
	
	public Integer getDateline() {
		return dateline;
	}

	public void setDateline(Integer dateline) {
		this.dateline = dateline;
	}
	
}