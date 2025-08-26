package com.realworld.conduit.util;

import java.text.Normalizer;
import java.util.UUID;

public class SlugUtils {
    
    public static String generateSlug(String title) {
        if (title == null || title.trim().isEmpty()) {
            return UUID.randomUUID().toString();
        }
        
        // 공백을 하이픈으로 변경하고 소문자로 변환
        String slug = title.trim()
                .toLowerCase()
                .replaceAll("\\s+", "-")  // 공백을 하이픈으로
                .replaceAll("[^a-z0-9\\-가-힣]", "")  // 알파벳, 숫자, 하이픈, 한글만 허용
                .replaceAll("-+", "-")  // 연속된 하이픈을 하나로
                .replaceAll("^-|-$", "");  // 앞뒤 하이픈 제거
        
        // 빈 문자열인 경우 UUID 사용
        if (slug.isEmpty()) {
            slug = UUID.randomUUID().toString();
        }
        
        return slug;
    }
    
    public static String generateUniqueSlug(String title, String suffix) {
        String baseSlug = generateSlug(title);
        return baseSlug + "-" + suffix;
    }
}