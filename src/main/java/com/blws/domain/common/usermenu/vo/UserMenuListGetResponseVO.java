package com.blws.domain.common.usermenu.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.blws.domain.common.usermenu.entity.UserMenuEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserMenuListGetResponseVO {
	
	private final String menuId;
	private final String menuName;
	private final String url;
	private final int menuSequence;
	

	@Builder
	public UserMenuListGetResponseVO(String menuId, String menuName, String url, int menuSequence) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.url = url;
		this.menuSequence = menuSequence;
	}
	
	public static UserMenuListGetResponseVO of(UserMenuEntity resultEntity) {
		return builder()
				.menuId(resultEntity.getMenuId())
				.menuName(resultEntity.getMenuName())
				.url(resultEntity.getUrl())
				.menuSequence(resultEntity.getMenuSequence())
				.build();
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
