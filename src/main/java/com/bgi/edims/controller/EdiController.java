package com.bgi.edims.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bgi.edims.model.EdiSysConfig;
import com.bgi.edims.model.EdiUser;
import com.bgi.edims.service.SystemConfigService;

@Controller
public class EdiController {
	private Logger logger=LoggerFactory.getLogger(EdiController.class);
	private EdiUser getCurrentUser() {
		Subject subject=SecurityUtils.getSubject();
		if(null==subject) {
			return null;
		}
		EdiUser ediUser= (EdiUser) subject.getPrincipal();
		return ediUser;
	}


	
	@Autowired
	private SystemConfigService systemConfigService;
	@RequestMapping("/indexbak")
	public String loginbak(EdiUser user,HttpServletResponse response) {
		EdiUser loginUser=getCurrentUser();
		if(loginUser==null){
			return "login";
		}
		if(SecurityUtils.getSubject().isAuthenticated()) {
			return "index";
		}else {
			return "login";
		}
	}
	
	@RequestMapping("/index")
	public ModelAndView index(EdiUser user,HttpServletResponse response) {
		logger.info("[页面跳转]===访问index===");
		EdiUser loginUser=getCurrentUser();
		if(loginUser==null){
			logger.info("[页面跳转]===用户未登录，跳转登录页===");
			return  new ModelAndView("edilogin");
		}
		if(SecurityUtils.getSubject().isAuthenticated()) {
			logger.info("[页面跳转]===用户已登录，访问首页===");
			ModelAndView model = new ModelAndView("ediindex");
			model.addObject("menus", loginUser.getMenus());
			model.addObject("username", loginUser.getUserName());
			return model;
		}else {
			logger.info("[页面跳转]===用户登录已过期，返回登录页===");
			return  new ModelAndView("edilogin");
		}
	}
	@RequestMapping(value="/login")
	public String getLogin(HttpServletRequest request,EdiUser user,HttpServletResponse response){
		logger.info("[页面跳转]===登录请求===");
		if(user==null){
			logger.info("[页面跳转]===用户信息未填写，返回登录页===");
			return "edilogin";
		} 
		String account=user.getUserName();
		String password=user.getPassWord();
		UsernamePasswordToken token = new UsernamePasswordToken(account,password,false);
		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.login(token);
		} catch(IncorrectCredentialsException e){
			return "edilogin";
		} catch (AuthenticationException e) {
			return "edilogin";
		}		
		return "redirect:/index";
	}
	@RequestMapping(value="/")
	public String index(){
		logger.info("[页面跳转]===访问根目录===");
		return "redirect:/index";
	}
	@RequestMapping(value="/asn")
	public String asn(HttpServletRequest request,Model model,HttpSession session,HttpServletResponse response){
		logger.info("[页面跳转]===访问ASN列表页面===");
		return "asn";
	}
	@RequestMapping(value="/email")
	public ModelAndView email(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		logger.info("[页面跳转]===访问邮件配置页面===");
		ModelAndView model = new ModelAndView("email");
		List<EdiSysConfig> ediSysConfigs=systemConfigService.getSystemConfigs("1");
		for(EdiSysConfig ediSysConfig:ediSysConfigs) {
			model.addObject(ediSysConfig.getConfigKey(), ediSysConfig.getConfigValue());
		}
		return model;
	}
	@RequestMapping(value="/folder")
	public String folder(HttpServletRequest request,Model model,HttpSession session,HttpServletResponse response){
		logger.info("[页面跳转]===访问文件路径修改页面===");
		return "folder";
	}
	@RequestMapping(value="/innerorder")
	public String innerorder(HttpServletRequest request,Model model,HttpSession session,HttpServletResponse response){
		logger.info("[页面跳转]===访问内示订单页面===");
		return "innerorder";
	}
	@RequestMapping(value="/order")
	public String order(HttpServletRequest request,Model model,HttpSession session,HttpServletResponse response){
		logger.info("[页面跳转]===访问销售订单页面===");
		return "order";
	}
	
	@RequestMapping(value="/main")
	public String main(HttpServletRequest request,Model model,HttpSession session,HttpServletResponse response){
		return "main";
	}
	@RequestMapping(value="/role")
	public String role(HttpServletRequest request,Model model,HttpSession session,HttpServletResponse response){
		return "role";
	}
	@RequestMapping(value="/supplier")
	public String supplier(HttpServletRequest request,Model model,HttpSession session,HttpServletResponse response){
		return "supplier";
	}
	@RequestMapping(value="/user")
	public String user(HttpServletRequest request,Model model,HttpSession session,HttpServletResponse response){
		return "user";
	}
	@RequestMapping(value="/changepwd")
	public String changepwd(HttpServletRequest request,Model model,HttpSession session,HttpServletResponse response){
		return "changepwd";
	}
	/**
	 * 登出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
	    Subject subject = SecurityUtils.getSubject(); 
		if (subject.isAuthenticated()) { 
		    subject.getSession().stop();
		}
		return "redirect:/index";
	}
}
