package by.bigsoft.news.dao;

import java.sql.SQLException;
import java.util.List;

import by.bigsoft.news.model.Article;
import by.bigsoft.news.model.News;
import by.bigsoft.news.model.Region;

public interface INewsDao {
	NewsListModel getLatestNews(int count, int skip);
	List<Region> getRegions();
	News getById(long id);
	void addNews(News news);
	NewsListModel getFilterNews(Integer[] idRegions, Integer[] idArticles, int newsPerPage, int skip);
	List<Article> getArticles();
	void deleteNews(long id);
	int getCountOfNews();
	int addRegion(Region r);
	int addArticle(Article a);
}
