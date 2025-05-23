package org.tourlink.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.tourlink.common.util.JsonUtils;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * JWT工具类
 */
@Slf4j
public class JwtUtils {
    
    private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24小时
    
    /**
     * 生成JWT令牌
     * @param payload JWT载荷
     * @param secret 密钥
     * @return JWT令牌
     */
    public static String generateToken(JwtPayload payload, String secret) {
        long now = System.currentTimeMillis();
        long expirationTime = now + JWT_TOKEN_VALIDITY;
        
        payload.setExpiration(expirationTime);
        
        return Jwts.builder()
                .setSubject(payload.getUserId().toString())
                .claim("payload", JsonUtils.toJson(payload))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expirationTime))
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * 从JWT令牌中获取载荷
     * @param token JWT令牌
     * @param secret 密钥
     * @return JWT载荷
     */
    public static JwtPayload getPayloadFromToken(String token, String secret) {
        String payloadJson = getClaimFromToken(token, claims -> claims.get("payload", String.class), secret);
        return JsonUtils.fromJson(payloadJson, JwtPayload.class);
    }
    
    /**
     * 从JWT令牌中获取用户ID
     * @param token JWT令牌
     * @param secret 密钥
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token, String secret) {
        return Long.parseLong(getClaimFromToken(token, Claims::getSubject, secret));
    }
    
    /**
     * 从JWT令牌中获取过期时间
     * @param token JWT令牌
     * @param secret 密钥
     * @return 过期时间
     */
    public static Date getExpirationDateFromToken(String token, String secret) {
        return getClaimFromToken(token, Claims::getExpiration, secret);
    }
    
    /**
     * 从JWT令牌中获取声明
     * @param token JWT令牌
     * @param claimsResolver 声明解析器
     * @param secret 密钥
     * @param <T> 声明类型
     * @return 声明
     */
    public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver, String secret) {
        final Claims claims = getAllClaimsFromToken(token, secret);
        return claimsResolver.apply(claims);
    }
    
    /**
     * 从JWT令牌中获取所有声明
     * @param token JWT令牌
     * @param secret 密钥
     * @return 所有声明
     */
    private static Claims getAllClaimsFromToken(String token, String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 验证JWT令牌是否过期
     * @param token JWT令牌
     * @param secret 密钥
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token, String secret) {
        final Date expiration = getExpirationDateFromToken(token, secret);
        return expiration.before(new Date());
    }
    
    /**
     * 验证JWT令牌是否有效
     * @param token JWT令牌
     * @param userId 用户ID
     * @param secret 密钥
     * @return 是否有效
     */
    public static Boolean validateToken(String token, Long userId, String secret) {
        try {
            final Long tokenUserId = getUserIdFromToken(token, secret);
            return (tokenUserId.equals(userId) && !isTokenExpired(token, secret));
        } catch (Exception e) {
            log.error("JWT令牌验证失败", e);
            return false;
        }
    }
    
    /**
     * 获取签名密钥
     * @param secret 密钥字符串
     * @return 签名密钥
     */
    private static Key getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
