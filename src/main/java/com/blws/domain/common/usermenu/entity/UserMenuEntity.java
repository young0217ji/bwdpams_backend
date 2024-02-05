package com.blws.domain.common.usermenu.entity;

import com.blws.global.common.BaseAbstractEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMenuEntity extends BaseAbstractEntity{

	private String plantId;
	private String userId;
	private String menuId;
	private String menuName;
	private String url;
	private int menuSequence;
	private String flag;
	
	@Builder
	private UserMenuEntity(String plantId, String userId, String menuId, String menuName, String url, int menuSequence, String flag) {
		this.plantId = plantId;
		this.userId = userId;
		this.menuId = menuId;
		this.menuName = menuName;
		this.url = url;
		this.menuSequence = menuSequence;
		this.flag = flag;
	}
}
