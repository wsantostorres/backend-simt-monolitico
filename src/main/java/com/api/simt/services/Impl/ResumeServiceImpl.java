package com.api.simt.services.Impl;

import com.api.simt.dtos.ResumeDto;
import com.api.simt.models.*;
import com.api.simt.repositories.*;
import com.api.simt.services.ResumeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    ResumeRepository resumeRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ExperienceRepository experienceRepository;

    @Autowired
    AcademicFormationRepository academicFormationRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ContactRepository contactRepository;


    @Override
    @Transactional
    public ResumeModel getResume(long id) {
        Optional<ResumeModel> resumeModelOptional = resumeRepository.findById(id);
        return resumeModelOptional.orElse(null);
    }

    @Override
    @Transactional
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
            ContactModel contact = resumeDto.contact();
            contact.setResume(resumeModel);

            resumeModel.setProjects(resumeDto.projects());
            resumeModel.setExperiences(resumeDto.experiences());
            resumeModel.setAcademics(resumeDto.academics());
            resumeModel.setSkills(resumeDto.skills());
            resumeModel.setAddress(resumeDto.address());
            resumeModel.setContact(resumeDto.contact());

            ResumeModel savedResume;
            return savedResume = resumeRepository.save(resumeModel);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao salvar o currículo.", e);
        }
    }

    @Override
    @Transactional
    public ResumeModel updateResume(long id, ResumeDto resumeDto) {
        Optional<ResumeModel> resumeModelOptional = resumeRepository.findById(id);

        if(resumeModelOptional.isPresent()){
            ResumeModel existingResumeModel = resumeModelOptional.get();
            existingResumeModel.setObjectiveDescription(resumeDto.objectiveDescription());

            /* Atualizando Projetos */
            for (ProjectModel project : resumeDto.projects()) {
                if (project.getId() != null) {
                    Optional<ProjectModel> existingProject = projectRepository.findById(project.getId());
                    if (existingProject.isPresent()) {
                        /* Se por acaso um projeto existir e tentar atualizar com campos vazios
                          vou excluir o projeto */
                        if(project.getTitleProject().isEmpty()){
                            projectRepository.deleteById(project.getId());
                        }else{
                            existingProject.get().setFoundation(project.getFoundation());
                            existingProject.get().setTitleProject(project.getTitleProject());
                            existingProject.get().setInitialYear(project.getInitialYear());
                            existingProject.get().setClosingYear(project.getClosingYear());
                        }
                    }else{
                        project.setResume(existingResumeModel);
                        existingResumeModel.getProjects().add(project);
                    }
                }else{
                    project.setResume(existingResumeModel);
                    existingResumeModel.getProjects().add(project);
                }
            }

            /* Atualizando Experiencias */
            for (ExperienceModel experience : resumeDto.experiences()) {
                if (experience.getId() != null) {
                    Optional<ExperienceModel> existingExperience = experienceRepository.findById(experience.getId());
                    if (existingExperience.isPresent()) {
                        if((experience.getCompany().isEmpty() && experience.getFunctionName().isEmpty())
                        || (!experience.getCompany().isEmpty() && experience.getFunctionName().isEmpty())){
                            experienceRepository.deleteById(experience.getId());
                        }else{
                            existingExperience.get().setCompany(experience.getCompany());
                            existingExperience.get().setFunctionName(experience.getFunctionName());
                            existingExperience.get().setInitialYear(experience.getInitialYear());
                            existingExperience.get().setClosingYear(experience.getClosingYear());
                        }
                    }else{
                        experience.setResume(existingResumeModel);
                        existingResumeModel.getExperiences().add(experience);
                    }
                }else{
                    experience.setResume(existingResumeModel);
                    existingResumeModel.getExperiences().add(experience);
                }
            }

            /* Atualizando Formações Acadêmicas */
            for (AcademicFormationModel academic : resumeDto.academics()){
                if(academic.getId() != null){
                    Optional<AcademicFormationModel> existingAcademicFormation
                            = academicFormationRepository.findById(academic.getId());
                    if(existingAcademicFormation.isPresent()){
                        if(academic.getFoundation().isEmpty() || academic.getSchooling().isEmpty()){
                            academicFormationRepository.deleteById(academic.getId());
                        }else {
                            existingAcademicFormation.get().setFoundation(academic.getFoundation());
                            existingAcademicFormation.get().setSchooling(academic.getSchooling());
                            existingAcademicFormation.get().setInitialYear(academic.getInitialYear());
                            existingAcademicFormation.get().setClosingYear(academic.getClosingYear());
                        }
                    }else{
                        academic.setResume(existingResumeModel);
                        existingResumeModel.getAcademics().add(academic);
                    }
                }else{
                    academic.setResume(existingResumeModel);
                    existingResumeModel.getAcademics().add(academic);
                }
            }

            /* Atualizando habilidades */
            for (SkillModel skill : resumeDto.skills()){
                if(skill.getId() != null){
                    Optional<SkillModel> existingSkill
                            = skillRepository.findById(skill.getId());
                    if(existingSkill.isPresent()){
                        if(skill.getNameSkill().isEmpty()){
                            skillRepository.deleteById(skill.getId());
                        }else{
                            existingSkill.get().setNameSkill(skill.getNameSkill());
                        }
                    }else{
                        skill.setResume(existingResumeModel);
                        existingResumeModel.getSkills().add(skill);
                    }
                }else{
                    skill.setResume(existingResumeModel);
                    existingResumeModel.getSkills().add(skill);
                }
            }

            /* Atualizando endereço */
            AddressModel address = resumeDto.address();
            if(existingResumeModel.getAddress() != null){
                existingResumeModel.getAddress().setCity(address.getCity());
                existingResumeModel.getAddress().setStreet(address.getStreet());
                existingResumeModel.getAddress().setNumber(address.getNumber());
            }

            /* Atualizando contato */
            ContactModel contact = resumeDto.contact();
            if(existingResumeModel.getContact() != null){
                existingResumeModel.getContact().setPhone(contact.getPhone());
                existingResumeModel.getContact().setEmail(contact.getEmail());
                existingResumeModel.getContact().setLinkedin(contact.getLinkedin());
            }

            ResumeModel updatedResume;
            return updatedResume = resumeRepository.save(existingResumeModel);
        }

        return null;

    }
}
