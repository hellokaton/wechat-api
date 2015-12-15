package com.jewel.service.impl;

import java.util.List;

import blade.plugin.sql2o.Model;
import blade.plugin.sql2o.WhereParam;

import com.blade.annotation.Component;
import com.blade.annotation.Inject;
import com.jewel.model.PostTag;
import com.jewel.service.PostTagService;
import com.jewel.service.TagService;

@Component
public class PostTagServiceImpl implements PostTagService {
	
	private Model<PostTag> model = Model.create(PostTag.class);
	
	@Inject
	private TagService tagService;
	
	@Override
	public PostTag getPostTag(Integer id) {
		return model.fetchByPk(id);
	}
	
	@Override
	public PostTag getPostTag(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchOne();
		}
		return null;
	}
	
	@Override
	public List<PostTag> getPostTagList(WhereParam where, String order) {
		if(null != where){
			return model.select().where(where).orderBy(order).fetchList();
		}
		return null;
	}
	
	@Override
	public boolean save( Integer pid, Integer tid ) {
		return false;
	}
	
	@Override
	public boolean deleteByPid(Integer pid) {
		if(null != pid){
			WhereParam where = WhereParam.me();
			where.eq("pid", pid);
			List<PostTag> postTags = this.getPostTagList(where, "id desc");
			if (null != postTags && postTags.size() > 0) {
				model.delete().eq("pid", pid).executeAndCommit();
				for(PostTag postTag : postTags){
					Integer tid = postTag.getTid();
					tagService.updateCount(tid, -1);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean delete(Integer pid, Integer tid) {
		WhereParam where = WhereParam.me();
		where.eq("pid", pid);
		where.eq("tid", tid);
		PostTag postTag = this.getPostTag(where);
		if(null != postTag){
			model.delete().eq("pid", pid).eq("tid", tid).executeAndCommit();
			tagService.updateCount(tid, -1);
			return true;
		}
		return false;
	}
		
}
