package com.nowcoder.community.service;
import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
@Service
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;
    //构造器
    public AlphaService() {
        System.out.println("实例化 AlphaService");
    }
    //初始化
    @PostConstruct
    public void init() {
        System.out.println("初始化 AlphaService");
    }
    @PreDestroy
    public void destroy() {
        System.out.println("销毁 AlphaService");
    }
    public String find() {
        return alphaDao.select();
    }
}
