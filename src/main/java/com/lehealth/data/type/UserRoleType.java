package com.lehealth.data.type;

import java.util.HashMap;
import java.util.Map;

public enum UserRoleType {
	unknown(0),
	admin(1),
	dotcor(2),
	panient(4);
	
	private UserRoleType(int roleId) {
		this.roleId = roleId;
	}
	
	private final int roleId;
	private static Map<Integer, UserRoleType> map = new HashMap<Integer, UserRoleType>();
	
	static{
		for(UserRoleType type : UserRoleType.values()){
			map.put(type.getRoleId(), type);
		}
	}
	
	public int getRoleId() {
		return roleId;
	}
	
	public static UserRoleType getTypeById(int roleId){
		if(map.containsKey(roleId)){
			return map.get(roleId);
		}else{
			return UserRoleType.unknown;
		}
	}
}
