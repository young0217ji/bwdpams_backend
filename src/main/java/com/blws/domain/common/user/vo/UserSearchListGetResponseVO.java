package com.blws.domain.common.user.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.blws.domain.common.user.entity.UserEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSearchListGetResponseVO {

	private final String userId;
	private final String name;
	private final String eml;
	private final String phoneNo;
	private final String roleId;

	@Builder
	private UserSearchListGetResponseVO(String userId, String name, String eml,
			String phoneNo, String roleId) {
		this.userId = userId;
		this.name = name;
		this.eml = eml;
		this.phoneNo = phoneNo;
		this.roleId = roleId;
	}

	public static UserSearchListGetResponseVO of(UserEntity resultEntity) {
		return builder()
				.userId(resultEntity.getUserId())
				.name(resultEntity.getName())
				.eml(resultEntity.getEml())
				.phoneNo(resultEntity.getPhoneNo())
				.roleId(resultEntity.getRoleId())
				.build();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
