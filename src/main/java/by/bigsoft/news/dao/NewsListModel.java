package by.bigsoft.news.dao;

import java.util.List;

import by.bigsoft.news.model.News;

public class NewsListModel {
	private List<News> news;
	private int totalCount;
	
	public NewsListModel(List<News> news, int totalCount) {
		this.news = news;
		this.totalCount = totalCount;
	}
	
	public List<News> getNews() {
		return news;
	}

	public int getTotalCount() {
		return totalCount;
	}

}
