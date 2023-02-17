package com.mmall.controller.backend;

import ch.qos.logback.core.joran.event.SaxEventRecorder;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.sun.corba.se.spi.activation.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.tags.form.SelectTag;

import javax.servlet.http.HttpSession;

/**
 * Created by 70457 on 1/25/2023.
 */

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    //注入
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName,@RequestParam(value = "parentId", defaultValue = "0") int parentId){

        //check login
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "User doesn't log in");
        }

        //check admin
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iCategoryService.addCategory(categoryName, parentId);
        }
        else{
            return ServerResponse.createByErrorMessage("No permission, need Admin");
        }
    }

    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName){
        //check login
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "User doesn't log in");
        }

        //check admin
        if(iUserService.checkAdminRole(user).isSuccess()){
            //update categoryname
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        }
        else{
            return ServerResponse.createByErrorMessage("No permission, need Admin");
        }
    }

    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        //check login
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "User doesn't log in");
        }

        //check admin
        if(iUserService.checkAdminRole(user).isSuccess()){
            //find children category information, and no recursion, keep in the same level
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }
        else{
            return ServerResponse.createByErrorMessage("No permission, need Admin");
        }
    }


    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        //check login
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "User doesn't log in");
        }

        //check admin
        if(iUserService.checkAdminRole(user).isSuccess()){
            //find current node id and recursion children node id
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }
        else{
            return ServerResponse.createByErrorMessage("No permission, need Admin");
        }
    }

}
