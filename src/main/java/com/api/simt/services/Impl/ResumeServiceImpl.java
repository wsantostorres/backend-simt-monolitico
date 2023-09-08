package com.api.simt.services.Impl;

import com.api.simt.dtos.ResumeDto;
import com.api.simt.models.*;
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

            /* Adicionando Formações Academicas */
            for (AcademicFormationModel academic : resumeDto.academics()){
                academic.setResume(resumeModel);
            }

            /* Adicionando habilidades */
            for (SkillModel skill : resumeDto.skills()){
                skill.setResume(resumeModel);
            }

            /* Adicionando endereço */
            AddressModel address = resumeDto.address();
            address.setResume(resumeModel);

            /*Adicionando contato*/
            ContactModel contac = resumeDto.contacts();
            contac.setResume(resumeModel);

            resumeModel.setProjects(resumeDto.projects());
            resumeModel.setExperiences(resumeDto.experiences());
            resumeModel.setAcademics(resumeDto.academics());
            resumeModel.setSkills(resumeDto.skills());
            resumeModel.setAddress(resumeDto.address());
            resumeModel.setContacts(resumeDto.contacts());



            ResumeModel savedResume;
            return savedResume = resumeRepository.save(resumeModel);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao salvar o currículo.", e);
        }
    }
}
