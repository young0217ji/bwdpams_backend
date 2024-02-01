package com.lsitc.domain.common.usermenu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.lsitc.domain.common.usermenu.entity.UserMenuEntity;

@Mapper
public interface UserMenuDAO {
	
	List<UserMenuEntity> selectUserMenuList(UserMenuEntity userMenuEntity);
	int insertUserMenu(UserMenuEntity userMenuEntity);
	int updateUserMenu(UserMenuEntity userMenuEntity);
	int deleteUserMenu(UserMenuEntity userMenuEntity);

}
