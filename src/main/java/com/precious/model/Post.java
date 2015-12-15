package com.precious.model;

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
	
	//所属菜单
	private Integer menu_id;
	
	//内容
	private String content;
	
	private String links;
	
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
	
	public Integer getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
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