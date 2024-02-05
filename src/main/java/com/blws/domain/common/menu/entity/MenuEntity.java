package com.blws.domain.common.menu.entity;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.blws.global.auditing.Auditable;
import com.blws.global.common.BaseAbstractEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuEntity extends BaseAbstractEntity implements Auditable<Long, LocalDateTime> {

	private String plantID;
	private String menuID;
	private String menuName;
	private String menuNameEN;
	private String parentsId;
	private String url;
	private String useFlag;
	private int position;
	private String roleId;
	private String iconClass;
	private String userId;

	@Builder
	private MenuEntity(String plantID, String menuID, String menuName, String menuNameEN, String parentsId, String url,
			String useFlag, int position, String roleId, String iconClass, String userId) {
		this.plantID = plantID;
		this.menuID = menuID;
		this.menuName = menuName;
		this.menuNameEN = menuNameEN;
		this.parentsId = parentsId;
		this.url = url;
		this.useFlag = useFlag;
		this.position = position;
		this.roleId = roleId;
		this.iconClass = iconClass;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
