package com.miaosha.project;

import com.miaosha.project.dao.UserDOMapper;
import com.miaosha.project.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@RestController
@SpringBootApplication(scanBasePackages = {"com.miaosha.project"})
@MapperScan("com.miaosha.project.dao")
public class App 
{

    @Autowired
    private UserDOMapper userDOMapper;

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class, args);
    }

    @RequestMapping("/")
    public String home(){
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if(userDO == null){
            return "Hello,用户对象不存在";
        }else{
            return userDO.getName();
        }
    }
}
