package com.lsitc.domain.common.menu.vo;

import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.lsitc.domain.common.menu.entity.MenuEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MainMenuListGetRequestVO {

	private final String locale;
	private final String userId;

	public MenuEntity toEntity() {

		String roleId = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
		return MenuEntity.builder()
				.useFlag("Yes")
				.roleId(roleId)
				.userId(userId)
				.build();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
