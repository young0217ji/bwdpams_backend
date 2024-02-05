package com.blws.domain.common.usermenu.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.blws.domain.common.usermenu.dao.UserMenuDAO;
import com.blws.domain.common.usermenu.entity.UserMenuEntity;
import com.blws.domain.common.usermenu.vo.UserMenuAddRequestVO;
import com.blws.domain.common.usermenu.vo.UserMenuListGetRequestVO;
import com.blws.domain.common.usermenu.vo.UserMenuListGetResponseVO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserMenuService {
	
	private final UserMenuDAO userMenuDAO;
	
	public List<UserMenuListGetResponseVO> getUserMenuList(final UserMenuListGetRequestVO userMenuListGetRequestVO){
		UserMenuEntity requestEntity = userMenuListGetRequestVO.toEntity();
		log.info(requestEntity.toString());
		List<UserMenuEntity> responseList = userMenuDAO.selectUserMenuList(requestEntity);
		return responseList.stream().map(UserMenuListGetResponseVO::of).collect(Collectors.toList());
	}
	
	@Transactional
	public int updateUserMenu(final List<UserMenuAddRequestVO> userMenuAddRequestVO) {
		int result = 0;
		for (UserMenuAddRequestVO vo : userMenuAddRequestVO) {
			UserMenuEntity requestEntity = vo.toEntity();
			if(requestEntity.getFlag().equals("C")) {
				result += userMenuDAO.insertUserMenu(requestEntity);
			}else if(requestEntity.getFlag().equals("U")) {
				result += userMenuDAO.updateUserMenu(requestEntity);
			}else if(requestEntity.getFlag().equals("D")) {
				result += userMenuDAO.deleteUserMenu(requestEntity);
			}
		}
		return result;
	}

}
