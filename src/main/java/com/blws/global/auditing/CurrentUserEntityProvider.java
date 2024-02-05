package com.blws.global.auditing;

import com.blws.domain.common.user.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;

public enum CurrentUserEntityProvider implements UserProvider<UserEntity, String> {
	INSTANCE;

	@Override
	public UserEntity getUser() {

		Object principal =null;

		try {
			principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch (Exception e){
			e.printStackTrace();
		}

		if (principal instanceof UserEntity) {
			return (UserEntity) principal;
		} else {
			return UserEntity.AnonymousUser();
		}

	}

	@Override
	public String getUserId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserEntity) {
			UserEntity userEntity = (UserEntity) principal;
			return userEntity.getUserId();
		} else {
			return UserEntity.AnonymousUser().getUserId();
		}
	}
}
