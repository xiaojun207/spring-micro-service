package com.microservice.starter.code;

/**
 *
 * 通用（服务器异常，参数校验异常，操作异常）
 */
public class CommonCodeConst {
	/**
	 * 执行成功
	 */
	public static final String SUCCESS = "100200";

	/**
	 * 账户未登录
	 */
	public static final String NO_LOGIN = "100101";

	/**
	 * 账户无权限
	 */
	public static final String NO_PERMISSION = "100102";

	/**
	 * 服务不可用，飞天了
	 */
	public static final String SERVICE_ERROR = "100103";

	/**
	 * 无效的请求
	 */
	public static final String INVALID_REQUEST = "100104";

	/**
	 * 访问频次过快，请稍后访问
	 */
	public static final String REQUEST_LIMIT = "100105";

	/**
	 * 字段不能为空
	 */
	public static final String FIELD_EMPTY = "100106";

	/**
	 * 传入字段校验错误
	 */
	public static final String FIELD_ERROR = "100107";

}
