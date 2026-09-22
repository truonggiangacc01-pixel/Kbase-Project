package com.fpt.kbase.service.impl;

import com.fpt.kbase.dto.request.UserCreateRequest;
import com.fpt.kbase.dto.request.UserProfileUpdateRequest;
import com.fpt.kbase.dto.response.UserResponse;
import com.fpt.kbase.entity.User;
import com.fpt.kbase.repository.UserRepository;
import com.fpt.kbase.security.UserDetailsImpl;
import com.fpt.kbase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng hiện tại"));
    }

    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getSystemRole()
        );
    }

    // --- Dành cho ADMIN ---
    
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setSystemRole(request.getSystemRole());

        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(Long id, UserCreateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Nếu email thay đổi thì check trùng
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setSystemRole(request.getSystemRole());

        // Nếu password gửi lên khác rỗng thì cập nhật
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(encoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    // --- Dành cho BẤT KỲ USER NÀO ---

    @Override
    public UserResponse getMyProfile() {
        User user = getCurrentUser();
        return convertToResponse(user);
    }

    @Override
    public UserResponse updateMyProfile(UserProfileUpdateRequest request) {
        User user = getCurrentUser();
        
        // Theo yêu cầu của user, chỉ cho cập nhật fullName
        user.setFullName(request.getFullName());

        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }
}
