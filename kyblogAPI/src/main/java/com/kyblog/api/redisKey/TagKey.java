package com.kyblog.api.redisKey;

public class TagKey extends BasePrefix{

	private TagKey(String prefix) {
		super(prefix);
	}
	public static TagKey getById = new TagKey("id");
	public static TagKey getByName = new TagKey("name");
	public static TagKey getIndex = new TagKey("index");
}
