package vn.actvn.onthionline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;

@SpringBootApplication
@EnableGlobalAuthentication
public class OnthionlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnthionlineApplication.class, args);
    }


}
