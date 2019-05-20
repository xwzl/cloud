package com.cloud.tool;

import com.cloud.tool.old.MybatisHandy;

/**
 * @author xuweizhi
 * @date 2019/04/03 19:13
 */
public class HandyTest {

    public static void main(String[] args) {
        MybatisHandy handy = new MybatisHandy();
        //如果是子模块，必须设置
        handy.setChildModule(true);
        handy.setChildModuleName("spring-order");
        handy.setBasePackageName("com.cloud");
        handy.setAuthor("xuweizhi");
        handy.setUrl("47.105.218.58:3306/mooc");
       GeneratorUntil.generatorCode(handy);
    }
}
