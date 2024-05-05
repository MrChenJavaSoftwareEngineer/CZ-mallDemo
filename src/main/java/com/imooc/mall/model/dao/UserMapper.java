package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    User selectByName(String userName);

    List<User> selectList();

    User selectOneByEmailAddress(String emailAddress);
}