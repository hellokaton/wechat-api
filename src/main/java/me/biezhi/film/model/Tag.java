package me.biezhi.film.model;

import java.io.Serializable;

import blade.plugin.sql2o.Table;

/**
 * Tag对象
 */
@Table(value = "t_tag", PK = "id")
public class Tag implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	//标签
	private String name;
	
	//标签下的文章个数
	private Integer count;
	
	public Tag(){}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}