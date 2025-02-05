package com.blws.domain.common.menu.vo;

import com.blws.domain.common.menu.entity.MenuEntity;
import com.blws.domain.model.BooleanState;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuListVO {

	private final String factoryId;
	private final String menuId;
	private final String menuNm;
	private final String menuNmEn;
	private final String upMenuId;
	private final String url;
	private final String useFlag;
	private final int pos;
	private final String text;
	private final boolean expanded;
	private final boolean selected;
	private final String roleId;
	private final String iconClass;

	@Builder
	private MenuListVO(String factoryId, String menuId, String menuNm, String menuNmEn, String upMenuId, String url,
			String useFlag, int pos, String text, boolean expanded, boolean selected, String roleId, String iconClass) {
		this.factoryId = factoryId;
		this.menuId = menuId;
		this.menuNm = menuNm;
		this.menuNmEn = menuNmEn;
		this.upMenuId = upMenuId;
		this.url = url;
		this.useFlag = useFlag;
		this.pos = pos;
		this.text = text;
		this.expanded = expanded;
		this.selected = selected;
		this.roleId = roleId;
		this.iconClass = iconClass;
	}

	public static MenuListVO of(MenuEntity menuEntity) {
		return of(menuEntity, null);
	}

	public static MenuListVO of(MenuEntity menuEntity, String locale) {
		return builder()
				.factoryId(menuEntity.getFactoryId())
				.menuId(menuEntity.getMenuId())
				.menuNm(getNameByLocale(menuEntity, locale))
				.menuNmEn(menuEntity.getMenuNmEn())
				.upMenuId(menuEntity.getPrntSid())
				.url(menuEntity.getUrl())
				.useFlag(menuEntity.getUseFlag())
				.pos(menuEntity.getPos())
				.text(getNameByLocale(menuEntity, locale))
				.expanded(false)
				.selected(false)
				.roleId(menuEntity.getRoleId())
				.iconClass(menuEntity.getIconClass())
				.build();
	}

	private static String convertBoolean(Boolean booleanValue) {
		return BooleanState.of(booleanValue).getStringValue();
	}

	private static String getNameByLocale(MenuEntity menuEntity, String locale) {
		return "ko-KR".equals(locale) ? menuEntity.getMenuNm()
				: "en-US".equals(locale) ? menuEntity.getMenuNmEn() : menuEntity.getMenuNm();
	}
}