package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import com.sun.corba.se.spi.activation.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.print.attribute.standard.Severity;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by 70457 on 1/12/2023.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService{


    @Autowired
    private UserMapper userMapper;


    @Override
    public ServerResponse<User> login(String username, String password){

        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessgae("User doesn't exit");
        }


        //MD5 Encrypt Password
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        //password login MD5 Encrpyted
        User user= userMapper.selectLogin(username, md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessgae("Password Error");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("Successfully Login", user);
    }

    public ServerResponse<String> register(User user){
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }

        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5 Encrypted
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessgae("Regist Failed");
        }

        return ServerResponse.createBySuccessMessage("Regist succeeded");
    }


    public ServerResponse<String> checkValid(String str, String type){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            //Start Check
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessgae("Usernames Exist");
                }
            }

            if(Const.EMAIL.equals(type)){
                int resltCount = userMapper.checkEmail(str);
                if(resltCount > 0){
                    return ServerResponse.createByErrorMessgae("Emails Exist");
                }
            }
        }
        else{
            return ServerResponse.createByErrorMessgae("Parameter Error");
        }

        return ServerResponse.createBySuccessMessage("Check Succeeded");
    }

    public ServerResponse selectQuestion(String username){
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        //用户不存在是成功response
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMessgae("User doesn't Exist");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessgae("Question for finding lost password is empty");
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer){
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(resultCount > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessgae("Answer to the question error");
    }

    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken){
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessgae("Parameter Error, Token is needed");
        }

        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMessgae("User doesn't Exist");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);

        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessgae("Token is invalid or expired");
        }

        if(org.apache.commons.lang3.StringUtils.equals(token, forgetToken)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);

            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("Password Modification Succeeded");
            }
        }
        else{
            return ServerResponse.createByErrorMessgae("Token Error, Please get token for updating password again");
        }

        return ServerResponse.createByErrorMessgae("Password Modification Failed");
    }

    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user){
        //防止很横向越权，需要校验用户的旧密码，需要查询一个count，不指定id，count必然大于0
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessgae("Old Password Error");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createByErrorMessgae("Update Password Successfully");
        }

        return ServerResponse.createByErrorMessgae("Update Password Failed");
    }

    public ServerResponse<User> updateInformation(User user){
        //username 不能被更新，email需要校验，校验新的email是否存在，若存在，不能是当前用户的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if(resultCount > 0){
            return ServerResponse.createByErrorMessgae("Email exists, please change email");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("Information Update Successfully", updateUser);
        }

        return ServerResponse.createByErrorMessgae("Information Update Failed");
    }

    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessgae("CurrentUser doesn't exist");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);

        return ServerResponse.createBySuccess(user);
    }


}
