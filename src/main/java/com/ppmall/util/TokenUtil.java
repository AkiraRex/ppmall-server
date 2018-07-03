package com.ppmall.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtil {
	public static String createToken() {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		byte[] secretBytes = DatatypeConverter.parseBase64Binary("JWT-TOKEN");
		Key signingKey = new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("username", "token");
		claims.put("role", "admin");
		JwtBuilder builder = Jwts.builder()//这里其实就是new一个JwtBuilder，设置jwt的body
				.setClaims(claims)//如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
				.setId("tokenid") //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
				.setIssuedAt(new Date())//iat: jwt的签发时间
				.setSubject("") //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
				.setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.signWith(signatureAlgorithm, signingKey);//设置签名使用的签名算法和签名使用的秘钥

		return builder.compact();
	}

	public static Claims parseToken(String token) {
		return Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary("JWT-TOKEN"))
				.parseClaimsJws(token)
				.getBody();
	}

	public static void validateToken(String token) {
		try {
			Claims claims = parseToken(token);
			String username = claims.get("username").toString();
			String role = claims.get("role").toString();
			String tokenid = claims.getId();
			System.out.println("[username]:" + username);
			System.out.println("[role]:" + role);
			System.out.println("[tokenid]:" + tokenid);
		} catch (ExpiredJwtException e) {
			System.out.println("token expired");
		} catch (InvalidClaimException e) {
			System.out.println("token invalid");
		} catch (Exception e) {
			System.out.println("token error");
		}
	}
	
	/**
     * 由字符串生成加密key
     * @return
     */
    public SecretKey generalKey(){
        String stringKey = "sss";//本地配置文件中加密的密文7786df7fc3a34e26a61c034d5ec8245d
        byte[] encodedKey = Base64.decodeBase64(stringKey);//本地的密码解码[B@152f6e2
        System.out.println(encodedKey);//[B@152f6e2
        System.out.println(Base64.encodeBase64URLSafeString(encodedKey));//7786df7fc3a34e26a61c034d5ec8245d
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。（后面的文章中马上回推出讲解Java加密和解密的一些算法）
        return key;
    }
	
	public static void main(String[] args) {
		String token = createToken();
		System.out.println(token);
		validateToken(token);
	}


}
