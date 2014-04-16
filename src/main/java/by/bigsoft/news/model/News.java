package by.bigsoft.news.model;

import java.util.Date;

import org.hibernate.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="News")
@Table(name="news")
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private long id;
	@Column(name = "header", length=256)
	private String header;
	
	@Column(name="title")
	private String title;
	
	@Column(name = "body")
	private String body;
	@Column(name="date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@ManyToOne( fetch = FetchType.EAGER)
	@JoinColumn(name = "region_id")
	private Region region;
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name="news_article", joinColumns={@JoinColumn(name = "article_id") }, 	
				inverseJoinColumns = {@JoinColumn(name = "id") })
	private List<Article> articles;
	
	public News(){
		this.articles = new ArrayList <Article>();
	}
	
	public News(int id, String header, String title, String body, Date date,
			Region region) {
		super();
		this.id = id;
		this.header = header;
		this.title = title;
		this.body = body;
		this.date = date;
		this.region = region;
	}
	
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public List<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
}
