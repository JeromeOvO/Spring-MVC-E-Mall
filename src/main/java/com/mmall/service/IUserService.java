package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by 70457 on 1/12/2023.
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);
}
