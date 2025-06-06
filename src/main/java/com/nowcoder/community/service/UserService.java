package com.nowcoder.community.service;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@Service
public class UserService implements CommunityConstant {
    //依赖注入用户的 Mapper
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    //根据用户 id 查询用户
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        //空值处理
        if(user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        //用户对象存在，用户名为空
        if(StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        //用户密码为空
        if(StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        //用户邮箱为空
        if(StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }
        //验证用户名
        User u = userMapper.selectByName(user.getUsername());
        if(u != null) {
            //用户名已经存在
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }
        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }
        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword()) + user.getSalt());
        //默认为普通用户
        user.setType(0);
        //默认为未激活
        user.setStatus(0);
        //默认随机字符串作为激活码
        user.setActivationCode(CommunityUtil.generateUUID());
        //生成默认头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        //激活路径: http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1) {
            //用户重复激活
            return ACTIVATION_REPEAT;
        } else if(user.getActivationCode().equals(code)) {
            //用户激活成功
            //更新用户的激活状态
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            //用户激活失败
            return ACTIVATION_FAILURE;
        }
    }
}
