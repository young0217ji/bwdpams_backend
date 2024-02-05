package com.blws.domain.common.user.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.blws.domain.common.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class UserInfoGetRequestVO {

	private final String userId;

	public UserEntity toEntity() {
		return UserEntity.builder()
				.userId(userId)
				.build();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
