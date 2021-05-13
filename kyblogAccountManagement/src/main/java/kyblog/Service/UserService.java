package kyblog.Service;

import com.kyblog.Dao.UserDao;
import com.kyblog.entity.Ticket;
import com.kyblog.entity.User;
import com.kyblog.utils.BlogUtils;
import com.kyblog.utils.RedisKeyUtils;
import kyblog.Dao.UserDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;

    public User findByAccount(String account) {
        return userDao.selectByAccount(account);
    }

    public int updatePassword(Long uid, String password) {
        return userDao.updatePassword(uid, password);
    }

    public Map<String, Object> login(String account, String password) {
        User user = userDao.selectByAccount(account);
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            map.put("errorMsg", "用户不存在");
            return map;
        }
        if (password == null) {
            map.put("errorMsg", "密码不能为空");
            return map;
        }
        String temp = BlogUtils.md5(user.getSalt() + password);
        if (!user.getPassword().equals(temp)) {
            map.put("errorMsg", "密码错误");
            return map;
        }

        Ticket ticket = new Ticket();
        ticket.setTicket(BlogUtils.generateUUID());
        ticket.setUserId(user.getUid());
        String ticketKey = RedisKeyUtils.getTicketKey(ticket.getTicket());
        redisTemplate.opsForValue().set(ticketKey, ticket, 60*60*24, TimeUnit.SECONDS);
        map.put("ticket", ticket.getTicket());
        return map;
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getAccount())) {
            map.put("errorMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("errorMsg", "密碼不能為空");
            return map;
        }
        User u = userDao.selectByAccount(user.getAccount());
        if (u != null) {
            map.put("errorMsg", "账号已存在");
            return map;
        }
        user.setSalt(BlogUtils.generateUUID().substring(0,5));
        user.setPassword(BlogUtils.md5(user.getSalt() + user.getPassword()));
        userDao.insertUser(user);
        return map;
    }
}
