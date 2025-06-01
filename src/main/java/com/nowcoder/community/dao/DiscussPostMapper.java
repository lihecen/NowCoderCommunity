package com.nowcoder.community.dao;
import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface DiscussPostMapper {
    //offset: 每页的起始行号
    //limit: 每页限制显示多少条帖子
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);
    //查询数据表中一共有多少条帖子
    //@Param 注解用于给参数取别名
    //如果只有一个参数，并且在 <if> 中使用，则必须加注解
    int selectDiscussPostRows(@Param("userId") int userId);
}
