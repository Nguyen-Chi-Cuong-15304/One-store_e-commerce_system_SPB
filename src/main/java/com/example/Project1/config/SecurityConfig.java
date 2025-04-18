package com.example.Project1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.Project1.service.WebUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll() 
                .requestMatchers("/login","/mylogin", "/register", "/about").permitAll()  
                //.requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/logout").permitAll()
                .requestMatchers("/addproduct").hasRole("admin")
                .requestMatchers("/product/**").hasRole("admin")   
                .requestMatchers("/supplier/**").hasRole("admin") 
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/banner_slider_image/**").permitAll()
                .requestMatchers("/product_image/**").permitAll()
                .requestMatchers("/supplier_image/**").permitAll()
                .requestMatchers("/product_customer/**").permitAll()
                .requestMatchers("/checkoutpage").permitAll()
                .requestMatchers("/userInfo/**").permitAll()
                .requestMatchers("/navi/**").permitAll()
                .requestMatchers("/admin/order/**").hasRole("admin")
                .requestMatchers("/api/**").hasRole("admin")
                .requestMatchers("/css/**", "/js/**").permitAll()
                .requestMatchers("/service/**").permitAll()
                       
                .anyRequest().authenticated()                    // Mọi yêu cầu khác đều phải đăng nhập
            )
            .formLogin(form -> form
                .loginPage("/login")                             // Định nghĩa trang đăng nhập tùy chỉnh
                .loginProcessingUrl("/perform_login")            // Đường dẫn xử lý khi submit form login
                .usernameParameter("email")                     // Username parameter, tên của ô input trên form
                .defaultSuccessUrl("/", true)                // Chuyển đến trang home sau khi đăng nhập thành công
                // .usernameParameter("email")
                // .passwordParameter("password")
                .permitAll()                                     // Cho phép tất cả truy cập trang đăng nhập
            )
            .logout(config -> config
                .logoutSuccessUrl("/")                          // Chuyển về trang chủ sau khi đăng xuất
            )
            .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    WebUserService webUserService;
    //  @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.userDetailsService(webUserService).passwordEncoder(passwordEncoder());
    // }
}
