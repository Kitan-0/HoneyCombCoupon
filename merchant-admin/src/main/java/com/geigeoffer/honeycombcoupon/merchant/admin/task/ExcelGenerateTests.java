package com.geigeoffer.honeycombcoupon.merchant.admin.task;

import com.github.javafaker.Faker;
import groovy.transform.ASTTest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import java.nio.file.Paths;
import java.util.Locale;

public final class ExcelGenerateTests {
    private final int writeNum = 5000;
    private final Faker faker = new Faker(Locale.CHINA);
    private String excelPath = Paths.get("").toAbsolutePath().getParent() + "/tmp";

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class ExcelGenerateDemoData {
//        @ColumnW
        private String userId;
        private String phone;
        private String mail;
    }
}
