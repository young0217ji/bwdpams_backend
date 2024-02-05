package com.blws.domain.common.usermenu.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.blws.domain.common.usermenu.entity.UserMenuEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMenuListGetRequestVO {
	private final String plantId;
	private final String userId;
	
	public UserMenuEntity toEntity() {
		return UserMenuEntity.builder()
				.plantId(plantId)
				.userId(userId)
				.build();
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
