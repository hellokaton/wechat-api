package me.biezhi.film.model;

import java.io.Serializable;

import blade.plugin.sql2o.Table;

/**
 * PostTag对象
 */
@Table(value = "t_post_tag", PK = "id")
public class PostTag implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Integer pid;
	
	private Integer tid;
	
	public PostTag(){}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}
	
}