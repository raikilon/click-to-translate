package ch.clicktotranslate.gateway.routing;

public final class ApiRoutes {

	public static final String AUTH_BASE = "/auth";

	public static final String AUTH_LOGIN = AUTH_BASE + "/login";

	public static final String AUTH_ME = AUTH_BASE + "/me";

	public static final String AUTH_LOGOUT = AUTH_BASE + "/logout";

	public static final String OAUTH2_ALL = "/oauth2/**";

	public static final String LOGIN_OAUTH2_ALL = "/login/oauth2/**";

	private ApiRoutes() {
	}

}
