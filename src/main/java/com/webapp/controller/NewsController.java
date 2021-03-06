package com.webapp.controller;

import com.webapp.model.News;
import com.webapp.service.database.dao.NewsDao;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juntao Peng
 * @author Shangzhen Li
 */
@Controller
public class NewsController {

  private NewsDao newsDao;

  @Autowired
  public NewsController(NewsDao newsDao) {
    this.newsDao = newsDao;
  }

  @RequestMapping("/news")
  public ModelAndView service(@RequestParam("action") String action, HttpServletRequest request) {
    ModelAndView mv = new ModelAndView();
    HttpSession session = request.getSession();
    String currentUserType = (String) session.getAttribute("currentUserType");
    if ("admin".equals(currentUserType)) {
      mv.setViewName("mainAdmin");
      adminNewsService(mv, action, request);
    } else if ("user".equals(currentUserType)) {
      mv.setViewName("mainUser");
      userNewsService(mv, action, request);
    }
    return mv;
  }

  private void adminNewsService(ModelAndView mv, String action, HttpServletRequest request) {
    switch (action) {
      case "add":
        mv.addObject("mainPage", "admin/newsModify.jsp");
        break;
      case "delete": {
        int newsId = Integer.parseInt(request.getParameter("newsId"));
        newsDao.deleteNewsById(newsId);
        listNews(mv, true);
        break;
      }
      case "modify": {
        int newsId = Integer.parseInt(request.getParameter("newsId"));
        News news = newsDao.queryNewsById(newsId);
        mv.addObject("news", news);
        mv.addObject("mainPage", "admin/newsModify.jsp");
        break;
      }
      case "save": {
        String strNewsId = request.getParameter("newsId");
        News news;
        Date time = new Date();
        if (!"".equals(strNewsId)) {
          // Save action for a modified news
          int id = Integer.parseInt(strNewsId);
          news = newsDao.queryNewsById(id);
          news.setLastModified(time.getTime());
          news.setId(id);
          news.setTitle(request.getParameter("title"));
          news.setAuthor(request.getParameter("author"));
          news.setDetail(request.getParameter("detail"));
          newsDao.updateNews(news);
        } else {
          news = new News();
          news.setCreated(time.getTime());
          news.setLastModified(time.getTime());
          news.setTitle(request.getParameter("title"));
          news.setAuthor(request.getParameter("author"));
          news.setDetail(request.getParameter("detail"));
          newsDao.insertNews(news);
        }
        listNews(mv, true);
        break;
      }
      case "list":
      default:
        listNews(mv, true);
        break;
    }
  }

  private void userNewsService(ModelAndView mv, String action, HttpServletRequest request) {
    switch (action) {
      case "detail": {
        int newsId = Integer.parseInt(request.getParameter("newsId"));
        News news = newsDao.queryNewsById(newsId);
        mv.addObject("news", news);
        mv.addObject("mainPage", "user/newsDetail.jsp");
        break;
      }
      case "list":
      default:
        listNews(mv, false);
        break;
    }
  }

  private void listNews(ModelAndView mv, boolean isAdmin) {
    if (isAdmin) {
      mv.addObject("mainPage", "admin/news.jsp");
    } else {
      mv.addObject("mainPage", "user/news.jsp");
    }
    List<News> newsList = this.newsDao.listNews(20);
    if (newsList != null) {
      mv.addObject("newsList", newsList);
    }
  }
}
