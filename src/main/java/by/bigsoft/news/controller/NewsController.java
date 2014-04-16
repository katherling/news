package by.bigsoft.news.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import by.bigsoft.news.dao.INewsDao;
import by.bigsoft.news.dao.NewsListModel;
import by.bigsoft.news.model.Article;
import by.bigsoft.news.model.News;
import by.bigsoft.news.model.Region;

@Controller
public class NewsController {
	@Autowired
	@Qualifier("by.bigsoft.news.dao.NewsDao")
	private INewsDao newsDao;
	private final int newsPerPage = 7;
	
//	@RequestMapping ({"/","/index"})
//	public String showIndex(Model model) {
//		List<News> latestNews = newsDao.getLatestNews(newsPerPage);
//		model.addAttribute("newsList", latestNews);
//		prepareModel(model, null, null);
//		//model.addAttribute("name", "Kate");
//		return "index";
//	}
	
	@RequestMapping({"/", "/index"})
	public String getRegionArticleFilter(Model model,
			@RequestParam(value="selectedRegions", required=false) Integer[] idRegions,
			@RequestParam(value="selectedArticles", required=false) Integer[] idArticles,
			@RequestParam(value="page", required=false) Integer page)
	{
		if (page == null || page <= 1) page = 1;
		NewsListModel news = newsDao.getFilterNews(idRegions, idArticles, newsPerPage, (page-1)*newsPerPage);

		model.addAttribute("newsList", news.getNews());
		int totalPages = Double.valueOf(Math.ceil((double)news.getTotalCount() / newsPerPage)).intValue();
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		prepareModel(model, 
				idRegions == null ? null : Arrays.asList(idRegions),
				idArticles == null ? null : Arrays.asList(idArticles));
		return "index";
	}
	
	@RequestMapping(value = {"/getNews.json"}, method = RequestMethod.POST)
	public @ResponseBody News getNewsJson(
			@RequestParam(value = "id", required = true) int id){
		News resp = new News();
		News n = newsDao.getById(id);
		resp.setBody(n.getBody());
		return resp;
	}
	
	private void prepareModel(Model model, List<Integer> idRegions,
			List<Integer> idArticles) {
		List<Region> regions = newsDao.getRegions();
		model.addAttribute("regions", regions);
		List<Article> articles = newsDao.getArticles();
		model.addAttribute("articles", articles);
		if (idRegions != null) {
			for (Region r : regions) {
				if (idRegions.contains(r.getId()))
					r.setIsSelected(true);
			}
		}
		if (idArticles != null) {
			for (Article a : articles) {
				if (idArticles.contains(a.getArticleId()))
					a.setIsSelected(true);
			}
		}
	}
}
