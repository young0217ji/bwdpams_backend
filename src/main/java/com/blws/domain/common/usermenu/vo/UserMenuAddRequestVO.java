package com.blws.domain.common.usermenu.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.blws.domain.common.usermenu.entity.UserMenuEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMenuAddRequestVO {
	private final String factoryId;
	private final String userId;
	private final String menuId;
	private final int menuSequence;
	private final String flag;
	
	public UserMenuEntity toEntity() {
		return UserMenuEntity.builder()
				.factoryId(factoryId)
				.userId(userId)
				.menuId(menuId)
				.menuSequence(menuSequence)
				.flag(flag)
				.build();
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
