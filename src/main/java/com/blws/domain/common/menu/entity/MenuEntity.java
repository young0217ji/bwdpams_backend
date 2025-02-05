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

	private String factoryId;
	private String menuId;
	private String menuNm;
	private String menuNmEn;
	private String prntSid;
	private String url;
	private String useFlag;
	private int pos;
	private String roleId;
	private String iconClass;
	private String userId;

	@Builder
	private MenuEntity(String factoryId, String menuId, String menuNm, String menuNmEn, String prntSid, String url,
			String useFlag, int pos, String roleId, String iconClass, String userId) {
		this.factoryId = factoryId;
		this.menuId = menuId;
		this.menuNm = menuNm;
		this.menuNmEn = menuNmEn;
		this.prntSid = prntSid;
		this.url = url;
		this.useFlag = useFlag;
		this.pos = pos;
		this.roleId = roleId;
		this.iconClass = iconClass;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
