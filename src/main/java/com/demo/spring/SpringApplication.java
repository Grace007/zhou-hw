package com.demo.spring;


import com.demo.spring.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author eli
 * @date 2017/9/14 14:17
 */
public class SpringApplication {
    private  static Logger logger = LoggerFactory.getLogger(SpringApplication.class);

    public static void main(String[] args) {
        logger.info("spring启动开始...");
        new ClassPathXmlApplicationContext("classpath:springdemo/*.xml");
        SpringTest springTest=(SpringTest) SpringContextUtil.getBean("springTest");
        springTest.show();
        logger.info("spring启动完成...");
    }
}
