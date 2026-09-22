package com.fpt.kbase.service;

import com.fpt.kbase.dto.request.UserCreateRequest;
import com.fpt.kbase.dto.request.UserProfileUpdateRequest;
import com.fpt.kbase.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    // Admin API
    List<UserResponse> getAllUsers();
    UserResponse createUser(UserCreateRequest request);
    UserResponse updateUser(Long id, UserCreateRequest request);

    // Profile API
    UserResponse getMyProfile();
    UserResponse updateMyProfile(UserProfileUpdateRequest request);
}
