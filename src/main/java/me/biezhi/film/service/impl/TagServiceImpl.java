package me.biezhi.film.service.impl;

import java.util.List;

import me.biezhi.film.model.Tag;
import me.biezhi.film.service.TagService;
import blade.plugin.sql2o.Model;
import blade.plugin.sql2o.WhereParam;

import com.blade.annotation.Component;

@Component
public class TagServiceImpl implements TagService {
	
	private Model<Tag> model = Model.create(Tag.class);
	
	@Override
	public Tag getTag(Integer id) {
		return model.fetchByPk(id);
	}
	
	@Override
	public Tag getTag(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchOne();
		}
		return null;
	}
	
	@Override
	public List<Tag> getTagList(WhereParam where, String order) {
		if(null != where){
			return model.select().where(where).orderBy(order).fetchList();
		}
		return null;
	}
	
	@Override
	public boolean save( String name, Integer count ) {
		return false;
	}
	
	@Override
	public boolean delete(Integer id) {
		if(null != id){
			return model.delete().eq("id", id).executeAndCommit() > 0;
		}
		return false;
	}
		
}
