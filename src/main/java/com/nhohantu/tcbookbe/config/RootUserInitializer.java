package com.nhohantu.tcbookbe.config;

import com.nhohantu.tcbookbe.common.model.system.SysRoleModel;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.model.system.UserSysRoleModel;
import com.nhohantu.tcbookbe.common.repository.BaseSysRoleRepository;
import com.nhohantu.tcbookbe.common.repository.BaseUserInfoRepo;
import com.nhohantu.tcbookbe.common.repository.BaseUserSysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RootUserInitializer implements CommandLineRunner {
    private final BaseUserInfoRepo userRepo;
    private final BaseSysRoleRepository roleRepo;
    private final BaseUserSysRoleRepository userSysRoleRepo;

    @Value("${app.root.username}")
    private String rootUsername;

    @Value("${app.root.password}")
    private String rootPassword;

    @Value("${app.root.email:root@system.local}") // có default nếu không khai báo
    private String rootEmail;

    @Override
    public void run(String... args) {
        // nếu chưa có user root thì mới tạo
        if (userRepo.findByUsername(rootUsername).isEmpty()) {
            System.out.println("===Không tìm thấy root user===");
            System.out.println("===                             BẮT ĐẦU KHỞI TẠO ROOT USER                             ===");

            // tạo role ROOT nếu chưa có
            SysRoleModel rootRole = roleRepo.findByRoleName("ROOT")
                    .orElseGet(() -> {
                        SysRoleModel role = new SysRoleModel();
                        role.setRoleName("ROOT");
                        role.setRoleLevel("0");
                        role.setRoleDescription("Super Admin");
                        role.setRoleType("SYSTEM");
                        return roleRepo.save(role);
                    });

            // tạo user root
            UserBasicInfoModel rootUser = new UserBasicInfoModel();
            rootUser.setUsername(rootUsername);
            rootUser.setPassword(new BCryptPasswordEncoder().encode(rootPassword)); // hash mật khẩu
            rootUser.setFirstName("System");
            rootUser.setLastName("Administrator");
            rootUser.setEmail(rootEmail);
            rootUser.setPrimaryPhone("0000000000");
            rootUser = userRepo.save(rootUser);

            // gán role ROOT cho user
            UserSysRoleModel userRole = new UserSysRoleModel();
            userRole.setUser(rootUser);
            userRole.setRole(rootRole);
            userRole.setAssignedAt(LocalDateTime.now());
            userRole.setAssignedBy("SYSTEM");
            userSysRoleRepo.save(userRole);

            System.out.println("===✅ Root user đã được khởi tạo: " + rootUsername + " / " + rootPassword);
            System.out.println("===                                                                           ===");
            System.out.println("===                             KHỞI TẠO HOÀN TẤT                             ===");
        }
    }
}