package com.demo.shiro.controller;

import com.demo.shiro.utils.R;
import com.demo.shiro.utils.ShiroUtils;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 登录相关
 * 
 */
@Controller
public class SysLoginController {
	@Autowired
	private Producer producer;
	
	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws ServletException, IOException {
		/*
		 * no-cache:每次请求都会访问服务器，都会访问。 no-store:所有内容都不会被缓存到缓存或 Internet 临时文件中
		 * 
		 * 
		 * Public指示响应可被任何缓存区缓存。
		 * Private指示对于单个用户的整个或部分响应消息，不能被共享缓存处理。这允许服务器仅仅描述当用户的部分响应消息，此响应消息对于其他用户的请求无效。 　　no-cache指示请求或响应消息不能缓存
		 * no-store用于防止重要的信息被无意的发布。在请求消息中发送将使得请求和响应消息都不使用缓存。
		 * max-age指示客户机可以接收生存期不大于指定时间（以秒为单位）的响应。
		 * min-fresh指示客户机可以接收响应时间小于当前时间加上指定时间的响应。
		 * max-stale指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可以接收超出超时期指定值之内的响应消息。
		 */
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        
        ServletOutputStream out = response.getOutputStream();
        //使用支持给定格式的任意 ImageWriter 将一个图像写入 ImageOutputStream。
		/*
		 * 参数： 
		 * im - 要写入的 RenderedImage。 
		 * formatName - 包含格式非正式名称的 String。
		 * output - 将在其中写入数据的 OutputStream。 
		 * 返回： 如果没有找到合适的 writer，则返回 false。
		 */
        ImageIO.write(image, "jpg", out);
	}
	
	/**
	 * 登录
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public R login(String username, String password, String captcha)throws IOException {
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if(!captcha.equalsIgnoreCase(kaptcha)){
			return R.error("验证码不正确");
		}
		
		try{
			Subject subject = ShiroUtils.getSubject();
			//sha256加密
			password = new Sha256Hash(password).toHex();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
		}catch (UnknownAccountException e) {
			//没有找到账户
			return R.error(e.getMessage());
		}catch (IncorrectCredentialsException e) {
			//密码不匹配
			return R.error(e.getMessage());
		}catch (LockedAccountException e) {
			//帐号被锁定
			return R.error(e.getMessage());
		}catch (AuthenticationException e) {
			//验证失败，AuthenticationException是以上三个异常的父类
			return R.error("账户验证失败");
		}
	    
		return R.ok();
	}
	
	/**
	 * 退出
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		ShiroUtils.logout();
		return "redirect:login.html";
	}
	
}
