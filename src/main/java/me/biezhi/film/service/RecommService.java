package me.biezhi.film.service;

import java.util.List;

import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

import me.biezhi.film.model.Recomm;

public interface RecommService {
	
	public Recomm getRecomm(Integer id);
	
	public Recomm getRecomm(WhereParam where);
	
	public List<Recomm> getRecommList(WhereParam where);
	
	public Page<Recomm> getPageList(WhereParam where, Integer page, Integer pageSize, String order);
	
	public boolean save( String title, String link, String content, String cover, Integer displayOrder, Boolean isDel, Integer dateline );
	
	public boolean delete(Integer id);
		
}
