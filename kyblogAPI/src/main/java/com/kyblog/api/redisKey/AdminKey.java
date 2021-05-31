package com.kyblog.api.redisKey;

public class AdminKey extends BasePrefix{

	private AdminKey(String prefix) {
		super(prefix);
	}
	public static AdminKey getById = new AdminKey("id");
	public static AdminKey getByFront = new AdminKey("front");
}
