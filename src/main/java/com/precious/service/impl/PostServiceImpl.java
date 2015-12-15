package com.precious.service.impl;

import java.util.List;

import blade.kit.DateKit;
import blade.kit.StringKit;
import blade.plugin.sql2o.Model;
import blade.plugin.sql2o.Page;
import blade.plugin.sql2o.WhereParam;

import com.blade.annotation.Component;
import com.blade.annotation.Inject;
import com.precious.model.Post;
import com.precious.model.Tag;
import com.precious.service.PostService;
import com.precious.service.PostTagService;
import com.precious.service.TagService;

@Component
public class PostServiceImpl implements PostService {
	
	private Model<Post> model = Model.create(Post.class);
	
	@Inject
	private TagService tagService;
	
	@Inject
	private PostTagService postTagService;
	
	@Override
	public Post getPost(Integer pid) {
		return model.fetchByPk(pid);
	}
	
	@Override
	public Post getPost(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchOne();
		}
		return null;
	}
	
	@Override
	public List<Post> getPostList(WhereParam where) {
		if(null != where){
			return model.select().where(where).fetchList();
		}
		return null;
	}
	
	@Override
	public Page<Post> getPageList(WhereParam where, Integer page, Integer pageSize, String order) {
		Page<Post> pagePost = model.select().where(where).orderBy(order).fetchPage(page, pageSize);
		return pagePost;
	}
	
	@Override
	public boolean save( String title, String slug, String cover, Integer menu_id, String content, String links, String tags) {
		try {
			Integer pid = model.insert()
			.param("title", title)
			.param("slug", slug)
			.param("cover", cover)
			.param("menu_id", menu_id)
			.param("content", content)
			.param("links", links)
			.param("dateline", DateKit.getCurrentUnixTime())
			.executeAndCommit();
			
			if (StringKit.isNotBlank(tags)) {
				String[] tag_arr = tags.split(",");
				for (String tag : tag_arr) {
					Tag t = tagService.getTag(tag);
					if (null == t) {
						t = tagService.save(tag, 0);
					} else {
						Integer id = t.getId();
						tagService.updateCount(id, 1);
					}
					postTagService.save(pid, t.getId());
				}
			}
			return pid > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean delete(Integer pid) {
		if(null != pid){
			// 删除文章
			model.delete().eq("pid", pid).executeAndCommit();
			// 删除文章标签关联
			postTagService.deleteByPid(pid);
			
		}
		return false;
	}

	@Override
	public boolean update(Integer pid, String title, String slug, String cover,
			Integer menu_id, String content, String links) {
		return false;
	}
		
}
