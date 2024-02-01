package com.lsitc.domain.common.user.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.lsitc.domain.common.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSearchListGetRequestVO {

	private final String userId;
	private final String name;

	public UserEntity toEntity() {
		return UserEntity.builder()
				.userId(userId)
				.name(name)
				.build();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
