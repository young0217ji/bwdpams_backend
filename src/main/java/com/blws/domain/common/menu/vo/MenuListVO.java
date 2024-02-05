package com.blws.domain.common.menu.vo;

import com.blws.domain.common.menu.entity.MenuEntity;
import com.blws.domain.model.BooleanState;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuListVO {

	private final String plantID;
	private final String menuId;
	private final String menuName;
	private final String menuNameEN;
	private final String upMenuId;
	private final String url;
	private final String useFlag;
	private final int position;
	private final String text;
	private final boolean expanded;
	private final boolean selected;
	private final String roleId;
	private final String iconClass;

	@Builder
	private MenuListVO(String plantID, String menuId, String menuName, String menuNameEN, String upMenuId, String url,
			String useFlag, int position, String text, boolean expanded, boolean selected, String roleId, String iconClass) {
		this.plantID = plantID;
		this.menuId = menuId;
		this.menuName = menuName;
		this.menuNameEN = menuNameEN;
		this.upMenuId = upMenuId;
		this.url = url;
		this.useFlag = useFlag;
		this.position = position;
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
				.plantID(menuEntity.getPlantID())
				.menuId(menuEntity.getMenuID())
				.menuName(getNameByLocale(menuEntity, locale))
				.menuNameEN(menuEntity.getMenuNameEN())
				.upMenuId(menuEntity.getParentsId())
				.url(menuEntity.getUrl())
				.useFlag(menuEntity.getUseFlag())
				.position(menuEntity.getPosition())
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
		return "ko-KR".equals(locale) ? menuEntity.getMenuName()
				: "en-US".equals(locale) ? menuEntity.getMenuNameEN() : menuEntity.getMenuName();
	}
}