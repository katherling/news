package by.bigsoft.news.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import by.bigsoft.news.model.Article;
import by.bigsoft.news.model.News;
import by.bigsoft.news.model.Region;

@Repository
@Qualifier("by.bigsoft.news.dao.NewsDao")
public class NewsDao implements INewsDao{
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Transactional
	public NewsListModel getLatestNews(int count, int skip) {
		Query q = sessionFactory.getCurrentSession().createQuery("from News order by date desc");
		int total = q.list().size();
		q.setFirstResult(skip);
		q.setMaxResults(count);
		return new NewsListModel(q.list(), total);
	}
	@Transactional
	public News getById(long id) {
		/*News n = (News) sessionFactory.getCurrentSession().load(News.class, id);
		News result = new News();
		result.setBody(n.getBody());
		result.setHeader(n.getHeader());
		result.setTitle(n.getTitle());
		Region region = new Region();
		region.setId(n.getRegion().getId());
		region.setName(n.getRegion().getName());
		List<Article> articles = new ArrayList<Article>();
		for (Article a : n.getArticles()) {
			articles.add(new Article().setArticleId(a.getArticleId()));
		}
		return result;*/
		News n = (News) sessionFactory.getCurrentSession().load(News.class, id);
		Hibernate.initialize(n);
		Hibernate.initialize(n.getArticles());
		Hibernate.initialize(n.getRegion());
		for (Article a : n.getArticles()) {
			a.setNews(null);
		}
		n.getRegion().setNews(null);
		return n;
		/*Session s = sessionFactory.getCurrentSession();
		News n = (News) s.load(News.class, id);
		s.clear();
		return n;*/
		//return (News) sessionFactory.getCurrentSession().load(News.class, id);
	}
	@Transactional
	public void addNews(News news) {
		Session s = sessionFactory.getCurrentSession();
		Object n = s.merge(news);
		s.saveOrUpdate(n);
	}
	@Transactional
	public void removeNews(long id){
		 News news1 =  (News) sessionFactory.getCurrentSession().load(News.class, id);
	        if (null != news1) {
	            sessionFactory.getCurrentSession().delete(news1);
	        }
	}
	@Transactional
	public List<Region> getRegions() {
		
		return sessionFactory.getCurrentSession().createQuery("from Region").list();
	}
	@Transactional
	public List<Article> getArticles() {
		
		return sessionFactory.getCurrentSession().createQuery("from Article").list();
	}
	@Transactional
	public NewsListModel getFilterNews(Integer[] idRegions, Integer[] idArticles, int newsPerPage, int skip){
		int total = 0;
		if(idRegions==null && idArticles!=null){
			Query query =sessionFactory.getCurrentSession()
					.createQuery("select n from News n join n.articles a where a.articleId in (:idArticles) order by n.date desc");
			query.setParameterList("idArticles", idArticles);
			total = query.list().size();
			return new NewsListModel(query.setFirstResult(skip).setMaxResults(newsPerPage).list(), total);
		}
		else if(idRegions!=null && idArticles==null){
			Query query =sessionFactory.getCurrentSession()
					.createQuery("select n from News n where n.region.id in (:idRegions) order by n.date desc");
			query.setParameterList("idRegions", idRegions);
			total = query.list().size();
			return new NewsListModel(query.setFirstResult(skip).setMaxResults(newsPerPage).list(), total);
		}
		else if(idRegions!=null && idArticles!=null){
			Query query =sessionFactory.getCurrentSession()
					.createQuery("select n from News n join n.articles a where n.region.id in (:idRegions) and a.articleId in (:idArticles) order by n.date desc");
			query.setParameterList("idRegions", idRegions);
			query.setParameterList("idArticles", idArticles);
			total = query.list().size();
			return new NewsListModel(query.setFirstResult(skip).setMaxResults(newsPerPage).list(), total);
		}
		else{
			return getLatestNews(newsPerPage, skip);
		}

		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(News.class);
		criteria.createAlias("articles", "a", CriteriaSpecification.LEFT_JOIN);
		if (idRegions != null && idRegions.length != 0) {
			criteria.add(
					Restrictions.in("region.id", idRegions));
		}
		if (idArticles != null && idArticles.length != 0) {
			criteria.add(
					Restrictions.in("a.articleId", idArticles));
		}
		
		criteria.addOrder(Order.desc("date"));
		
		return criteria.list();*/
	}
	
	@Transactional
	public void deleteNews(long id) {
		News news1 =  (News) sessionFactory.getCurrentSession().load(News.class, id);
        if (null != news1) {
            sessionFactory.getCurrentSession().delete(news1);
        }
	}
	public int getCountOfNews() {
		
		return ( (Integer) sessionFactory.getCurrentSession().createQuery("select count(*) from News").uniqueResult()).intValue();
	}
	
	@Transactional
	public int addRegion(Region r) {
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from Region r where r.name = :name").setParameter("name", r.getName());
		if (q.list().size() != 0) {
			return 0;
		}
		s.save(r);
		return r.getId();
	}
	
	@Transactional
	public int addArticle(Article a) {
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from Article a where a.name = :name").setParameter("name", a.getName());
		if (q.list().size() != 0) {
			return 0;
		}
		s.save(a);
		return a.getArticleId();
	}
}
