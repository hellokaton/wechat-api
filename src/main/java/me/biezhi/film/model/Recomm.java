package me.biezhi.film.model;

import java.io.Serializable;

import blade.plugin.sql2o.Table;

/**
 * Recomm对象
 */
@Table(value = "t_recomm", PK = "id")
public class Recomm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	//标题
	private String title;
	
	//链接
	private String link;
	
	//内容
	private String content;
	
	//封面图
	private String cover;
	
	//排序
	private Integer display_order;
	
	//是否删除
	private Boolean is_del;
	
	//发布时间
	private Integer dateline;
	
	public Recomm(){}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
	
	public Integer getDisplay_order() {
		return display_order;
	}

	public void setDisplay_order(Integer display_order) {
		this.display_order = display_order;
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