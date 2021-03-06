package com.kyblog.api.redisKey;

public class CommentKey extends BasePrefix{

	private CommentKey(String prefix) {
		super(prefix);
	}
	public static CommentKey getById = new CommentKey("id");
	public static CommentKey getByTitle = new CommentKey("name");
	public static CommentKey getIndex = new CommentKey("index");
	public static CommentKey getByArticleId = new CommentKey("aId");
	public static CommentKey getByStatus = new CommentKey("status");

}
