package com.fpt.kbase.service.impl;

import com.fpt.kbase.dto.request.ProjectRequest;
import com.fpt.kbase.dto.response.ProjectResponse;
import com.fpt.kbase.dto.response.UserSummaryResponse;
import com.fpt.kbase.entity.Project;
import com.fpt.kbase.entity.User;
import com.fpt.kbase.repository.ProjectRepository;
import com.fpt.kbase.repository.UserRepository;
import com.fpt.kbase.security.UserDetailsImpl;
import com.fpt.kbase.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    // Hàm tiện ích để lấy User đang đăng nhập từ Security Context (lấy từ JWT token)
    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng hiện tại"));
    }

    // Hàm tiện ích chuyển đổi Entity -> DTO
    private ProjectResponse convertToResponse(Project project) {
        UserSummaryResponse ownerDto = new UserSummaryResponse(
                project.getCreatedBy().getId(),
                project.getCreatedBy().getFullName(),
                project.getCreatedBy().getEmail()
        );

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                project.getUpdatedAt(),
                ownerDto
        );
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        User currentUser = getCurrentUser();

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedBy(currentUser); // Set owner

        Project savedProject = projectRepository.save(project);
        
        return convertToResponse(savedProject);
    }

    @Override
    public List<ProjectResponse> getAllProjectsOfUser() {
        User currentUser = getCurrentUser();
        // Lấy tất cả project do user này tạo ra
        List<Project> projects = projectRepository.findByCreatedById(currentUser.getId());
        
        return projects.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dự án không tồn tại với ID: " + id));

        // Bảo mật: Kiểm tra xem user hiện tại có phải chủ dự án không (Tạm thời)
        User currentUser = getCurrentUser();
        if (!project.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền truy cập vào dự án này");
        }

        return convertToResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dự án không tồn tại với ID: " + id));

        User currentUser = getCurrentUser();
        if (!project.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền sửa dự án này");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        Project updatedProject = projectRepository.save(project);
        return convertToResponse(updatedProject);
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dự án không tồn tại với ID: " + id));

        User currentUser = getCurrentUser();
        if (!project.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa dự án này");
        }

        projectRepository.delete(project);
    }
}
