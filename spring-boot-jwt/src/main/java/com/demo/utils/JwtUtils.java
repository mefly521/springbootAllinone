package com.demo.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mifei
 * @create 2020-07-31 13:47
 **/
public class JwtUtils {
	private static final String SECRET = "org.qiqiang.secret";
	private static final String ISSUER = "user";

	/**
	 * 生成token
	 *
	 * @param claims
	 * @return
	 */
	public static String createToken(Map<String, String> claims) throws Exception {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET);
			JWTCreator.Builder builder = JWT.create()
					.withIssuer(ISSUER)
					//设置过期时间为2小时
					.withExpiresAt(DateUtil.offsetHour(new Date(), 72));
			claims.forEach(builder::withClaim);
			return builder.sign(algorithm);
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			throw new Exception("生成token失败");
		}
	}

	/**
	 * 验证jwt，并返回数据
	 */
	public static Map<String, String> verifyToken(String token) throws Exception {
		Algorithm algorithm;
		Map<String, Claim> map;
		long exp;
		try {
			algorithm = Algorithm.HMAC256(SECRET);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
			DecodedJWT jwt = verifier.verify(token);
			map = jwt.getClaims();
			exp = jwt.getExpiresAt().getTime();
		} catch (Exception e) {
			throw new Exception("鉴权失败");
		}
		Map<String, String> resultMap = new HashMap<>(map.size());
		map.forEach((k, v) -> resultMap.put(k, v.asString()));
		resultMap.put("exp", exp + "");
		return resultMap;
	}
}