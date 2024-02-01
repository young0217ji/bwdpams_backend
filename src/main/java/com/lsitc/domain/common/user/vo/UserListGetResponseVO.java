package com.lsitc.domain.common.user.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.lsitc.domain.common.user.entity.UserEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserListGetResponseVO {

	private final String userId;
	private final String name;
	private final String email;
	private final String phoneNumber;
	private final String roleId;

	@Builder
	private UserListGetResponseVO(String userId, String name, String email,
			String phoneNumber, String roleId) {
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.roleId = roleId;
	}

	public static UserListGetResponseVO of(UserEntity resultEntity) {
		return builder()
				.userId(resultEntity.getUserId())
				.name(resultEntity.getName())
				.email(resultEntity.getEmail())
				.phoneNumber(resultEntity.getPhoneNumber())
				.roleId(resultEntity.getRoleId())
				.build();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
