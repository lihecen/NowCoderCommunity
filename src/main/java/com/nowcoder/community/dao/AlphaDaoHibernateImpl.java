package com.nowcoder.community.dao;
import org.springframework.stereotype.Repository;
//访问数据库的Bean
@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
