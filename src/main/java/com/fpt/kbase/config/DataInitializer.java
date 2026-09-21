package com.fpt.kbase.config;

import com.fpt.kbase.entity.User;
import com.fpt.kbase.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Chỉ thêm dữ liệu nếu bảng users đang trống
        if (userRepository.count() == 0) {
            // 1. Tạo tài khoản ADMIN
            User admin = new User();
            admin.setEmail("admin@kbase.vn");
            admin.setPassword(passwordEncoder.encode("123456")); // Mật khẩu mặc định: 123456
            admin.setFullName("Nguyễn Văn Quản Trị");
            admin.setSystemRole("ADMIN");
            admin.setActive(true);
            userRepository.save(admin);

            // 2. Tạo tài khoản USER 1
            User user1 = new User();
            user1.setEmail("dung@kbase.vn");
            user1.setPassword(passwordEncoder.encode("123456"));
            user1.setFullName("Trần Trí Dũng");
            user1.setSystemRole("USER");
            user1.setActive(true);
            userRepository.save(user1);

            // 3. Tạo tài khoản USER 2
            User user2 = new User();
            user2.setEmail("linh@kbase.vn");
            user2.setPassword(passwordEncoder.encode("123456"));
            user2.setFullName("Lê Thùy Linh");
            user2.setSystemRole("USER");
            user2.setActive(true);
            userRepository.save(user2);

            // 4. Tạo tài khoản USER 3
            User user3 = new User();
            user3.setEmail("hoang@kbase.vn");
            user3.setPassword(passwordEncoder.encode("123456"));
            user3.setFullName("Phạm Minh Hoàng");
            user3.setSystemRole("USER");
            user3.setActive(true);
            userRepository.save(user3);

            System.out.println("=============================================");
            System.out.println("    SEED DATA CREATED SUCCESSFULLY!          ");
            System.out.println("    Tạo 1 ADMIN và 3 USER thành công.        ");
            System.out.println("    Mật khẩu mặc định: 123456                ");
            System.out.println("=============================================");
        }
    }
}
