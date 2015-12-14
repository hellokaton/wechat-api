package me.biezhi.film.model;

import java.io.Serializable;

import blade.plugin.sql2o.Table;

/**
 * Menu对象
 */
@Table(value = "t_menu", PK = "id")
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	//菜单名
	private String name;
	
	//菜单缩略名
	private String slug;
	
	//菜单排序
	private Integer display_order;
	
	public Menu(){}
	
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
	
	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
	
	public Integer getDisplay_order() {
		return display_order;
	}

	public void setDisplay_order(Integer display_order) {
		this.display_order = display_order;
	}
	
}