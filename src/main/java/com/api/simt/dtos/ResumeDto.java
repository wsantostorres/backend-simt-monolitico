package com.api.simt.dtos;

import com.api.simt.models.ProjectModel;

import java.util.List;

public record ResumeDto (String objectiveDescription, List<ProjectModel> projects){
}
