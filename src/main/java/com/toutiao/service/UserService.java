package com.toutiao.service;

import com.toutiao.dao.UserDAO;
import com.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
