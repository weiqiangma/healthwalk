package cn.pertech.healthwalk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Date 2020/10/10 10:04
 * @Author mawkun
 */
@ComponentScan("cn.pertech")
@SpringBootApplication
@MapperScan(basePackages = {"cn.pertech.healthwalk.base.dao", "cn.pertech.*.dao"})
public class HealthWalkApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HealthWalkApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(HealthWalkApplication.class);
    }
}
