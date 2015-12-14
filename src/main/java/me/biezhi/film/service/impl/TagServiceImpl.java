package me.biezhi.film.service.impl;

import java.util.List;

import me.biezhi.film.model.Tag;
import me.biezhi.film.service.TagService;
import blade.kit.StringKit;
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
	public Tag getTag(String name) {
		if(StringKit.isNotBlank(name)){
			return model.select().eq("name", name).fetchOne();
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
	public Tag save( String name, Integer count ) {
		Integer tid = model.insert().param("name", name).param("count", count).executeAndCommit();
		return this.getTag(tid);
	}
	
	@Override
	public boolean delete(Integer id) {
		if(null != id){
			return model.delete().eq("id", id).executeAndCommit() > 0;
		}
		return false;
	}
	
	@Override
	public boolean updateCount(Integer id, Integer count) {
		if(null != id && null != count){
			Tag tag = this.getTag(id);
			Integer newcount = tag.getCount() + count;
			if(newcount < 0){
				newcount = 0;
			}
			return model.update().param("count", newcount).eq("id", id).executeAndCommit() > 0;
		}
		return false;
	}
		
}
