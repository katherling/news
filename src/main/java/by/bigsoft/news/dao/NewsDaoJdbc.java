package by.bigsoft.news.dao;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.Connection;

import by.bigsoft.news.model.Article;
import by.bigsoft.news.model.News;
import by.bigsoft.news.model.Region;

@Repository
@Qualifier("by.bigsoft.news.dao.NewsDaoJdbc")
public class NewsDaoJdbc implements INewsDao{

private Connection con;
	
	public NewsDaoJdbc() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/news",
					"testUser", "test");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	
	public void dispose() {
		try {
			con.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public NewsListModel getLatestNews(int count, int skip) {
		ArrayList<News> news = new ArrayList<News>();
		PreparedStatement statement;
		int total=getCountOfNews();
		try {
			statement = con.prepareStatement("select * from News n order by n.date desc limit ? offset ?");
			statement.setInt(1, count);
	        statement.setInt(2, skip);
	        ResultSet rs = statement.executeQuery();
	        while (rs.next()) {
	  		  News news1 = new News();
	  		  news1.setId(rs.getInt("Id"));
	  		  news1.setTitle(rs.getString("Title"));
	  		  news1.setHeader(rs.getString("Header"));
	  		  news1.setBody(rs.getString("Body"));
	  		  news1.setDate(rs.getTimestamp("Date"));
	  		  news1.setRegion(getRegionById(rs.getInt("region_id")));
	  		  news1.setArticles(getArticlesByNewsId(news1.getId()));
	  		  news.add(news1);
	  		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return new NewsListModel(news, total);
	}

	public Region getRegionById(int id) throws SQLException {
		Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM region Where Id =" + id);
        if (rs.next()) {
  		  Region region = new Region();
  		  region.setId(rs.getInt("Id"));
  		  region.setName(rs.getString("name"));
  		  return region;
  		}
        else return null;
	}
	
	public List<Article> getArticlesByNewsId(long id) throws SQLException {
		ArrayList<Article> articles = new ArrayList<Article>();
		ArrayList<News> news = new ArrayList<News>();
		PreparedStatement statement;
		statement = con.prepareStatement("select * from news_article na"+
        		" join article a on na.article_id = a.article_id"+
        		" where na.id = ?");
        statement.setLong(1, id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
  		  Article art = new Article();
  		  art.setArticleId(rs.getInt("a.article_id"));
  		  art.setName(rs.getString("a.name"));
  		  articles.add(art);
  		}
        return articles;
        
	}
	
	
	public News getById(long id) {
    		try {
    			Statement statement = con.createStatement();
    	        ResultSet rs = statement.executeQuery("SELECT * FROM news Where Id =" + id);
    	        if (rs.next()) {
    	        	News news = new News();
    	        	news.setId(rs.getInt("Id"));
    	    		news.setTitle(rs.getString("Title"));
    	    		news.setHeader(rs.getString("Header"));
    	    		news.setBody(rs.getString("Body"));
				news.setDate(rs.getTimestamp("Date"));
				news.setRegion(getRegionById(rs.getInt("region_id")));
	    		//news.setArticles(articles);
	  		  return news;
	  		}
	        else return null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return null;
	}

	public void addNews(News news) {
		
		try {
			PreparedStatement statement = con.prepareStatement("Insert into news "
	        		+ "(title, header, body, date, region_id) "
	        		+ "values (?, ?, ?, ?, ?)");
			
			statement.setString(1, news.getTitle());
			statement.setString(2, news.getHeader());
			statement.setString(3, news.getBody());
			statement.setTimestamp(4, (Timestamp) news.getDate());
			statement.setInt(5, news.getRegion().getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public List<Region> getRegions() {
		ArrayList<Region> regions = new ArrayList<Region>();
  		  try {
  			Statement statement = con.createStatement();
  	        ResultSet rs = statement.executeQuery("SELECT * FROM region");
  	        while (rs.next()) {
  	  		  Region region = new Region();
  	  		  region.setId(rs.getInt("Id"));
			region.setName(rs.getString("name"));
			 regions.add(region);
  	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		 
  		return regions;
	}

	public NewsListModel getFilterNews(Integer[] idRegions, Integer[] idArticles, int newsPerPage, int skip) {
		ArrayList<News> result = new ArrayList<News>();
		int total = getCountOfNews();

		ArrayList<News> allNews = (ArrayList<News>) getLatestNews(total, 0).getNews();
		if (idArticles == null) idArticles = new Integer[0];
		if (idRegions == null) idRegions = new Integer[0];
		List<Integer> idArticlesList = Arrays.asList(idArticles);
		List<Integer> idRegionsList = Arrays.asList(idRegions);
		for (News n : allNews) {
		  if (idRegionsList.size() != 0) {
		    if (!idRegionsList.contains(n.getRegion().getId())) continue;
		  }
		  
		  if (idArticlesList.size() != 0) {
		    boolean continuing = false;
		    for (Article a : n.getArticles()) {
		      if (!idArticlesList.contains(a.getArticleId())) {
		        continuing = true;
		        break;
		      }
		    }
		    if (continuing) continue;
		  }

		  result.add( n );
		}
		total = result.size();
		ArrayList<News> croppedResult = new ArrayList<News>();

		for (int i=skip; i < result.size(); i++) {
		  if (croppedResult.size() == newsPerPage) break;
		  croppedResult.add(result.get(i));
		}

		return new NewsListModel(croppedResult, total);
	}

	public List<Article> getArticles() {
		ArrayList<Article> articles = new ArrayList<Article>();
		  try {
			Statement statement = con.createStatement();
	        ResultSet rs = statement.executeQuery("SELECT * FROM article");
	        while (rs.next()) {
	  		  Article art = new Article();
	  		  art.setArticleId(rs.getInt("article_id"));
			  art.setName(rs.getString("name"));
			  articles.add(art);
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return articles;
	}

	public void deleteNews(long id) {
		Statement statement;
		try {
			statement = con.createStatement();
			statement.execute("Delete FROM news_article Where Id = " + id);
			statement.execute("Delete FROM News Where Id = " + id);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getCountOfNews() {
        try {
        	Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select count(*) from News");
            rs.next(); 
			return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return 0;
	}

	public int addRegion(Region r) {
		try {
			PreparedStatement statement = con.prepareStatement("Insert into region "
	        		+ "(name) "
	        		+ "values (?)");
			
			statement.setString(1, r.getName());
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r.getId();
	}

	public int addArticle(Article a) {
		try {
			PreparedStatement statement = con.prepareStatement("Insert into article "
	        		+ "(name) "
	        		+ "values (?)");
			
			statement.setString(1, a.getName());
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a.getArticleId();
	}
	
}
