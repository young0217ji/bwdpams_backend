package com.blws.domain.common.usermenu.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blws.domain.common.usermenu.exception.UserMenuException;
import com.blws.domain.common.usermenu.service.UserMenuService;
import com.blws.domain.common.usermenu.vo.UserMenuAddRequestVO;
import com.blws.domain.common.usermenu.vo.UserMenuListGetRequestVO;
import com.blws.domain.common.usermenu.vo.UserMenuListGetResponseVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/common/userMenu")
@RestController
@RequiredArgsConstructor
public class UserMenuController {
	
	private final UserMenuService userMenuService;

	@GetMapping("/list")
	public List<UserMenuListGetResponseVO> getUserMenuList(
			@Valid final UserMenuListGetRequestVO userMenuListGetRequestVO) throws UserMenuException{
		List<UserMenuListGetResponseVO> userMenuList = userMenuService.getUserMenuList(userMenuListGetRequestVO);
		log.info(userMenuList.toString());
		return userMenuList; 
	}
	
	@PostMapping("/update")
	public int updateUserMenu(
			@RequestBody @Valid final List<UserMenuAddRequestVO> userMenuAddRequestVO) throws UserMenuException{
		log.info(userMenuAddRequestVO.toString());
		return userMenuService.updateUserMenu(userMenuAddRequestVO);
	}
}
