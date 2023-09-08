package com.api.simt.dtos;

import com.api.simt.models.*;

import java.util.List;

public record ResumeDto (String objectiveDescription,
                         List<ProjectModel> projects,
                         List<ExperienceModel> experiences,
                         List<AcademicFormationModel> academics,
                         List<SkillModel> skills,
                         AddressModel address,
                         ContactModel contacts){
}