package com.blws.domain.common.menu.vo;

import com.blws.domain.common.menu.entity.MenuEntity;
import com.blws.domain.model.BooleanState;
import com.blws.global.common.TreeAbstractVO;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuTreeVO extends TreeAbstractVO {

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
	private final String path;

	@Builder
	private MenuTreeVO(String factoryId, String menuId, String menuNm, String menuNmEn, String upMenuId, String url,
			String useFlag, int pos, String text, boolean expanded, boolean selected, String roleId, String iconClass, String path) {
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
		this.path = path;
	}

	public static MenuTreeVO of(MenuEntity menuEntity) {
		return of(menuEntity, null, false);
	}

	public static MenuTreeVO of(MenuEntity menuEntity, String locale, boolean isHorizontal) {
		return builder()
				.factoryId(menuEntity.getFactoryId())
				.menuId(menuEntity.getMenuId())
				.menuNm(getNameByLocale(menuEntity, locale))
				.menuNmEn(menuEntity.getMenuNmEn())
				.upMenuId(menuEntity.getPrntSid())
				.url(isHorizontal ? null : menuEntity.getUrl())
				.useFlag(menuEntity.getUseFlag())
				.pos(menuEntity.getPos())
				.text(getNameByLocale(menuEntity, locale))
				.expanded(false)
				.selected(false)
				.roleId(menuEntity.getRoleId())
				.iconClass(menuEntity.getIconClass())
				.path(isHorizontal ? menuEntity.getUrl() : null)
				.build();
	}

	private static String convertBoolean(Boolean booleanValue) {
		return BooleanState.of(booleanValue).getStringValue();
	}

	private static String getNameByLocale(MenuEntity menuEntity, String locale) {
		return "ko-KR".equals(locale) ? menuEntity.getMenuNm()
				: "en-US".equals(locale) ? menuEntity.getMenuNmEn() : menuEntity.getMenuNm();
	}

	@Override
	public String id() {
		return this.menuId;
	}

	@Override
	public String parentsId() {
		return this.upMenuId;
	}
}