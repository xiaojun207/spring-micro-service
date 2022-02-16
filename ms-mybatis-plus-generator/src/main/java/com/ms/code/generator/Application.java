package com.ms.code.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;

public class Application {

    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/ms-auth?serverTimezone=GMT";
        String username = "root";
        String password = "Abc123";
        String outputDir = "./ms-mybatis-plus-generator/out";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("xiaojun207") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir + "/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.ms.mybatisplus.samples.generator") // 设置父包名
                            .moduleName("api") // 设置url父包模块名
//                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, outputDir + "/src/main/resources/mapper")) // 设置mapperXml生成路径
                    ;
                })
                .strategyConfig(builder -> {
//                    builder.addInclude("t_simple") // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
