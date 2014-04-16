package by.bigsoft.news.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import by.bigsoft.news.dao.INewsDao;
import by.bigsoft.news.model.Article;
import by.bigsoft.news.model.News;
import by.bigsoft.news.model.Region;

@Controller
public class ManageNewsController {
	@Autowired
	@Qualifier("by.bigsoft.news.dao.NewsDao")
	private INewsDao newsDao;
	
	@RequestMapping({"/delete"})
	public String deleteNews(@RequestParam(value = "id", required = true) long id){
		newsDao.deleteNews(id);
		return "redirect:/index";
	}
	
	@RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
	public String addNewsPage(Model model,
			@RequestParam(value = "id", required = false) Integer id){
		System.out.println("id " + id);
		List<Region> regions = newsDao.getRegions();
		model.addAttribute("regions", regions);
		List<Article> articles = newsDao.getArticles();
		model.addAttribute("articles", articles);
		if (id!=null){
			News news = newsDao.getById(id);
			System.out.println("title " +news.getTitle());
			model.addAttribute("id", news.getId());
			model.addAttribute("titleOfNews", news.getTitle());
			model.addAttribute("headerOfNews", news.getHeader());
			model.addAttribute("bodyOfNews", news.getBody());
			Integer idRegionOfNews = news.getRegion().getId();
			
			List<Integer> idArticlesOfNews = new ArrayList<Integer>();
			for (Article a : news.getArticles()) {
			  idArticlesOfNews.add(a.getArticleId());
			}
			if (idRegionOfNews != null) {
				for (Region r : regions) {
					if (idRegionOfNews == r.getId())
						r.setIsSelected(true);
				}
			}
			if (idArticlesOfNews != null) {
				for (Article a : articles) {
					if (idArticlesOfNews.contains(a.getArticleId()))
						a.setIsSelected(true);
				}
			}
		}
		return "edit";
	}
	
	@RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
	public String addNews(
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "header", required = false) String header,
			@RequestParam(value = "body", required = false) String body,
			@RequestParam(value="selectedRegions", required=false) Integer idRegions,
			@RequestParam(value="selectedArticles", required=false) Integer[] idArticles){
		List<Region> regions = newsDao.getRegions();
		List<Article> articles = newsDao.getArticles();
		News news = new News();
		if (id != null){
			news = newsDao.getById(id);
			news.getArticles().clear();
		}
		else{
			news.setDate(new Date());
		}
		news.setTitle(title);
		news.setHeader(header);
		news.setBody(body);
		if (idRegions!= null){
			for (Region r: regions) {
				if (idRegions == r.getId())
					news.setRegion(r);
			}
		}
		if (idArticles!= null){
			List <Integer> idArticlesList = Arrays.asList(idArticles);
			for (Article a: articles) {
				if (idArticlesList.contains(a.getArticleId()))
					news.getArticles().add(a);
			}
		}
		
		System.out.println("Title: " + title + "Header: " + header + "Body: " + body);
		newsDao.addNews(news);
		return "redirect:/index";
	}
	
	@RequestMapping(value="/addRegion", method= RequestMethod.POST)
	public @ResponseBody Region addRegion(@RequestParam(value="name") String name) {
		Region r = new Region();
		r.setName(name);
		newsDao.addRegion(r);
		Region res = new Region();
		res.setId(r.getId());
		res.setName(r.getName());
		return res;
	}
	
	@RequestMapping(value="/addArticle", method= RequestMethod.POST)
	public @ResponseBody Article addArticle(@RequestParam(value="name") String name) {
		Article a = new Article();
		a.setName(name);
		newsDao.addArticle(a);
		Article res = new Article();
		res.setArticleId(a.getArticleId());
		res.setName(a.getName());
		return res;
	}
}
