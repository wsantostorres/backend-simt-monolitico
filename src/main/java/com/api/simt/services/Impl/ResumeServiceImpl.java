package com.api.simt.services.Impl;

import com.api.simt.dtos.ResumeDto;
import com.api.simt.models.ExperienceModel;
import com.api.simt.models.ProjectModel;
import com.api.simt.models.ResumeModel;
import com.api.simt.repositories.ResumeRepository;
import com.api.simt.services.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    ResumeRepository resumeRepository;

    @Override
    public ResumeModel createResume(ResumeDto resumeDto) {
        try {
            ResumeModel resumeModel = new ResumeModel();
            resumeModel.setObjectiveDescription(resumeDto.objectiveDescription());

            /* Adicionando Projetos */
            for (ProjectModel project : resumeDto.projects()) {
                project.setResume(resumeModel);
            }

            /* Adicionando Experiencias */
            for (ExperienceModel experience : resumeDto.experiences()) {
                experience.setResume(resumeModel);
            }

            resumeModel.setProjects(resumeDto.projects());
            resumeModel.setExperiences(resumeDto.experiences());

            ResumeModel savedResume;
            return savedResume = resumeRepository.save(resumeModel);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao salvar o currículo.", e);
        }
    }
}
