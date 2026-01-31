package com.org.jayanth.dtobestprac;

public class AuthResponseDto {

	private String accessToken;
	private String tokenType="Bearer";
	private long expiresIn;
	private UserInfoDto user;
	public AuthResponseDto(String accessToken, String tokenType, long expiresIn, UserInfoDto user) {
		super();
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.expiresIn = expiresIn;
		this.user = user;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
	public UserInfoDto getUser() {
		return user;
	}
	public void setUser(UserInfoDto user) {
		this.user = user;
	}
	
	
	
}
