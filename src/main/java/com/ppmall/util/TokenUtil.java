package com.ppmall.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

import com.ppmall.common.Const;
import com.ppmall.pojo.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtil {
	
	private static String SCRECT_KEY = PropertiesUtil.getProperty("jwt.screctkey");
	
	public static String createToken(Map claims, String subject, long ttlAtMillis) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		byte[] secretBytes = DatatypeConverter.parseBase64Binary(SCRECT_KEY);// 
		Key signingKey = generateKey();
		
		JwtBuilder builder = Jwts.builder()// 这里其实就是new一个JwtBuilder，设置jwt的body
				.setHeaderParam("typ", "JWT") // 设置header
				.setHeaderParam("alg", "HS256")// 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
				.setId(UUIDUtil.getUUID().replace("-", "")) // 设置jti(JWT
				.setClaims(claims)// ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
				.setIssuedAt(new Date())// iat: jwt的签发时间
				.setSubject(subject) // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
				.setExpiration(new Date(ttlAtMillis)).signWith(signatureAlgorithm, signingKey);// 设置签名使用的签名算法和签名使用的秘钥

		return builder.compact();
	}

	public static Claims parseToken(String token) {
		return Jwts.parser()
				.setSigningKey(generateKey())
				.parseClaimsJws(token)
				.getBody();
	}

	/**
	 * 由字符串生成加密key
	 * 
	 * @return
	 */
	public static SecretKey generateKey() {
		byte[] encodedKey = Base64.decodeBase64(SCRECT_KEY);
		SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥，使用																				// leng
		return key;
	}

	public static void validateToken(String token) {
		try {
			Claims claims = parseToken(token);
			System.out.println(claims.get(Const.CURRENT_USER));
		} catch (ExpiredJwtException e) {
			System.out.println("token expired");
		} catch (InvalidClaimException e) {
			System.out.println("token invalid");
		} catch (Exception e) {
			System.out.println("token error");
		}
	}

	
	public static void main(String[] args) {
		String subject = "sss";
		User user = new User();
		user.setUsername("ssadfasdf");
		Map claims = new HashMap<>();
		claims.put(Const.CURRENT_USER, "ssss");
		System.out.println(new Date().getTime() + Const.ExpiredType.ONE_MONTH);
//		String token = createToken(claims, subject, new Date().getTime() + Const.ExpiredType.ONE_MONTH);
		 String token =
		 "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMiIsImN1cnJlbnRVc2VyIjp7ImlkIjoyMiwidXNlcm5hbWUiOiJxcXFxIiwicGFzc3dvcmQiOiIzQkFENkFGMEZBNEI4QjMzMEQxNjJFMTk5MzhFRTk4MSIsImVtYWlsIjpudWxsLCJwaG9uZSI6IjEzNjUyNzM5MjExIiwicXVlc3Rpb24iOiLpl67popgiLCJhbnN3ZXIiOiLnrZTmoYgiLCJyb2xlIjowLCJ3ZWNoYXRPcGVuaWQiOm51bGwsImNyZWF0ZVRpbWUiOjE1MjY2MDcxNzcwMDAsInVwZGF0ZVRpbWUiOjE1MjY2MDcxNzcwMDB9LCJleHAiOjI1OTIwMDAsImlhdCI6MTUzMDU5NzM5MCwianRpIjoiYWEyYjg4YTMxMTNiNDZmNDk2Y2QyYTIwY2E4MTdiYmYifQ.kb7rnEKLLW6rP0zzs8hWTRkxjjQMliWfVZyzXJvgQNM";
		 System.out.println(token);
		validateToken(token);
	}

}
