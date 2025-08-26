package com.realworld.conduit.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SlugUtilsTest {

    @Test
    @DisplayName("영문 제목으로 slug 생성 테스트")
    void generateSlug_EnglishTitle() {
        String title = "How to Train Your Dragon";
        String slug = SlugUtils.generateSlug(title);
        
        assertThat(slug).isEqualTo("how-to-train-your-dragon");
    }

    @Test
    @DisplayName("한글 제목으로 slug 생성 테스트")
    void generateSlug_KoreanTitle() {
        String title = "자바스크립트 기초 학습";
        String slug = SlugUtils.generateSlug(title);
        
        assertThat(slug).isEqualTo("자바스크립트-기초-학습");
    }

    @Test
    @DisplayName("특수문자가 포함된 제목으로 slug 생성 테스트")
    void generateSlug_SpecialCharacters() {
        String title = "Java & Spring Boot: 완벽 가이드 (2024)";
        String slug = SlugUtils.generateSlug(title);
        
        assertThat(slug).isEqualTo("java-spring-boot-완벽-가이드-2024");
    }

    @Test
    @DisplayName("연속 공백이 포함된 제목으로 slug 생성 테스트")
    void generateSlug_MultipleSpaces() {
        String title = "Test    Title   With    Spaces";
        String slug = SlugUtils.generateSlug(title);
        
        assertThat(slug).isEqualTo("test-title-with-spaces");
    }

    @Test
    @DisplayName("앞뒤 공백이 포함된 제목으로 slug 생성 테스트")
    void generateSlug_LeadingTrailingSpaces() {
        String title = "  Test Title  ";
        String slug = SlugUtils.generateSlug(title);
        
        assertThat(slug).isEqualTo("test-title");
    }

    @Test
    @DisplayName("특수문자만 포함된 제목으로 slug 생성 테스트")
    void generateSlug_OnlySpecialCharacters() {
        String title = "!@#$%^&*()";
        String slug = SlugUtils.generateSlug(title);
        
        // 특수문자만 있으면 UUID 생성
        assertThat(slug).isNotEmpty();
        assertThat(slug).contains("-");
        assertThat(slug.length()).isGreaterThan(10);
    }

    @Test
    @DisplayName("빈 문자열로 slug 생성 테스트")
    void generateSlug_EmptyString() {
        String title = "";
        String slug = SlugUtils.generateSlug(title);
        
        // 빈 문자열이면 UUID 생성
        assertThat(slug).isNotEmpty();
        assertThat(slug).contains("-");
        assertThat(slug.length()).isGreaterThan(10);
    }

    @Test
    @DisplayName("null 제목으로 slug 생성 테스트")
    void generateSlug_NullTitle() {
        String title = null;
        String slug = SlugUtils.generateSlug(title);
        
        // null이면 UUID 생성
        assertThat(slug).isNotEmpty();
        assertThat(slug).contains("-");
        assertThat(slug.length()).isGreaterThan(10);
    }

    @Test
    @DisplayName("공백만 포함된 제목으로 slug 생성 테스트")
    void generateSlug_OnlySpaces() {
        String title = "   ";
        String slug = SlugUtils.generateSlug(title);
        
        // 공백만 있으면 UUID 생성
        assertThat(slug).isNotEmpty();
        assertThat(slug).contains("-");
        assertThat(slug.length()).isGreaterThan(10);
    }

    @Test
    @DisplayName("숫자가 포함된 제목으로 slug 생성 테스트")
    void generateSlug_WithNumbers() {
        String title = "JavaScript ES6 2024 Guide";
        String slug = SlugUtils.generateSlug(title);
        
        assertThat(slug).isEqualTo("javascript-es6-2024-guide");
    }

    @Test
    @DisplayName("하이픈이 포함된 제목으로 slug 생성 테스트")
    void generateSlug_WithHyphens() {
        String title = "Next.js - React Framework";
        String slug = SlugUtils.generateSlug(title);
        
        assertThat(slug).isEqualTo("nextjs-react-framework");
    }

    @Test
    @DisplayName("연속 하이픈 처리 테스트")
    void generateSlug_MultipleHyphens() {
        String title = "Test---Title---With---Hyphens";
        String slug = SlugUtils.generateSlug(title);
        
        assertThat(slug).isEqualTo("test-title-with-hyphens");
    }

    @Test
    @DisplayName("고유 slug 생성 테스트")
    void generateUniqueSlug_WithSuffix() {
        String title = "Test Article";
        String suffix = "123";
        String slug = SlugUtils.generateUniqueSlug(title, suffix);
        
        assertThat(slug).isEqualTo("test-article-123");
    }

    @Test
    @DisplayName("긴 제목으로 slug 생성 테스트")
    void generateSlug_LongTitle() {
        String title = "This is a very long title that contains many words and should be converted to a proper slug format";
        String slug = SlugUtils.generateSlug(title);
        
        assertThat(slug).isEqualTo("this-is-a-very-long-title-that-contains-many-words-and-should-be-converted-to-a-proper-slug-format");
        assertThat(slug).doesNotContain(" ");
        assertThat(slug).isLowerCase();
    }

    @Test
    @DisplayName("혼합 언어 제목으로 slug 생성 테스트")
    void generateSlug_MixedLanguage() {
        String title = "JavaScript 자바스크립트 Programming";
        String slug = SlugUtils.generateSlug(title);
        
        assertThat(slug).isEqualTo("javascript-자바스크립트-programming");
    }
}