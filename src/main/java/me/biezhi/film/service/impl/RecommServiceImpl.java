package me.biezhi.film.service.impl;

import java.util.List;
import blade.plugin.sql2o.Model;
import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;
import com.blade.annotation.Component;
import me.biezhi.film.model.Recomm;
import me.biezhi.film.service.RecommService;

@Component
public class RecommServiceImpl implements RecommService {
	
	private Model<Recomm> model = Model.create(Recomm.class);
	
	@Override
	public Recomm getRecomm(Integer id) {
		return model.fetchByPk(id);
	}
	
	@Override
	public Recomm getRecomm(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchOne();
		}
		return null;
	}
	
	@Override
	public List<Recomm> getRecommList(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchList();
		}
		return null;
	}
	
	@Override
	public Page<Recomm> getPageList(WhereParam where, Integer page, Integer pageSize, String order) {
		Page<Recomm> pageRecomm = model.select().where(where).orderBy(order).fetchPage(page, pageSize);
		return pageRecomm;
	}
	
	@Override
	public boolean save( String title, String link, String content, String cover, Integer displayOrder, Boolean isDel, Integer dateline ) {
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
