package com.fpt.kbase.service;

import com.fpt.kbase.dto.request.ProjectRequest;
import com.fpt.kbase.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
    
    ProjectResponse createProject(ProjectRequest request);
    
    List<ProjectResponse> getAllProjectsOfUser();
    
    ProjectResponse getProjectById(Long id);
    
    ProjectResponse updateProject(Long id, ProjectRequest request);
    
    void deleteProject(Long id);
}
