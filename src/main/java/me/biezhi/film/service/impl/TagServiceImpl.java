package me.biezhi.film.service.impl;

import java.util.List;
import blade.plugin.sql2o.Model;
import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;
import com.blade.annotation.Component;
import me.biezhi.film.model.Tag;
import me.biezhi.film.service.TagService;

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
	public List<Tag> getTagList(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchList();
		}
		return null;
	}
	
	@Override
	public Page<Tag> getPageList(WhereParam where, Integer page, Integer pageSize, String order) {
		Page<Tag> pageTag = model.select().where(where).orderBy(order).fetchPage(page, pageSize);
		return pageTag;
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
