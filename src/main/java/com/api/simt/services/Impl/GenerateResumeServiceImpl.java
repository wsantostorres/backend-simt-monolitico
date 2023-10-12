package com.api.simt.services.Impl;

import com.api.simt.models.*;
import com.api.simt.services.GenerateResumeService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GenerateResumeServiceImpl implements GenerateResumeService {
    public byte[] generateResumesZip(List<StudentModel> students) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {

            for (StudentModel student : students) {
                String resumeHtml = generateResumesHtml(student);

                /* Converter HTML para XHTML */
                Document resumeXhtml = Jsoup.parse(resumeHtml);

                /* Converter XHTML em PDF com OpenHTMLToPDF */
                ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(resumeXhtml.toString(), null);
                builder.toStream(pdfOutputStream);
                builder.run();

                // Adicionar o arquivo PDF do currículo ao arquivo ZIP
                ZipEntry entry = new ZipEntry(student.getFullName() + student.getRegistration() + ".pdf");
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(pdfOutputStream.toByteArray());
                zipOutputStream.closeEntry();
            }

            zipOutputStream.finish();
            zipOutputStream.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String generateResumesHtml(StudentModel student) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ResumeTemplate.html");

            if (inputStream != null) {
                String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                /* ENDEREÇO E CONTATO */
                StringBuilder addressAndContactHtml = new StringBuilder();
                AddressModel addressStudent = student.getResume().getAddress();
                String address = "<p> Endereço: " + addressStudent.getStreet() + ", "
                        + addressStudent.getNumber() + " - " + addressStudent.getCity() + "</p>";
                String email = "<p> Email: " + student.getResume().getContact().getEmail() + "</p>";
                String phone = "<p> Nº Telefone: " + student.getResume().getContact().getPhone() + "</p>";
                String linkedin = "<p> Linkedin: " + student.getResume().getContact().getLinkedin() + "</p>";
                addressAndContactHtml.append(email);
                addressAndContactHtml.append(phone);
                addressAndContactHtml.append(linkedin);
                addressAndContactHtml.append(address);

                /* HABILIDADES */
                StringBuilder skillsHtml = new StringBuilder();
                for(SkillModel skill : student.getResume().getSkills()){
                    String valueSkill = "<li>" + skill.getNameSkill() + "</li>";
                    skillsHtml.append(valueSkill);
                }

                /* FORMAÇÕES ACADÊMICAS */
                StringBuilder formationsHtml = new StringBuilder();
                for(AcademicFormationModel academic : student.getResume().getAcademics()){
                    String valuesAcademic = "<li>" + "<p>" + academic.getInitialYear() + "-" + academic.getClosingYear() + " | " + academic.getFoundation() + "</p>" +
                             "<span>" + academic.getSchooling() + "</span>" + "</li>";
                    formationsHtml.append(valuesAcademic);
                }

                /* PROJETOS */
                StringBuilder projectsHtml = new StringBuilder();
                for(ProjectModel project : student.getResume().getProjects()){
                    String valuesProject = "<li>" + "<p>" + project.getInitialYear() + "-" + project.getClosingYear() + " | " + project.getFoundation() + "</p>" +
                            "<span>" + project.getTitleProject() + "</span>" + "</li>";
                    projectsHtml.append(valuesProject);
                }

                /* EXPERIÊNCIAS */
                StringBuilder experiencesHtml = new StringBuilder();
                for(ExperienceModel experience : student.getResume().getExperiences()){
                    String valuesExperience = "<li>" + "<p>" + experience.getInitialYear() + "-" + experience.getClosingYear() + " | " + experience.getCompany() + "</p>" +
                            "<span>" + experience.getFunctionName() + "</span>" + "</li>";
                    experiencesHtml.append(valuesExperience);
                }

                /* Substituir informações do currículo do Aluno aqui */
                htmlTemplate = htmlTemplate.replace("{FULLNAME}", student.getFullName());
                htmlTemplate = htmlTemplate.replace("{CONTATOENDERECO}", addressAndContactHtml);
                htmlTemplate = htmlTemplate.replace("{OBJECTIVEDESCRIPTION}",
                        student.getResume().getObjectiveDescription());
                htmlTemplate = htmlTemplate.replace("{HABILIDADES}", skillsHtml);
                htmlTemplate = htmlTemplate.replace("{FORMACAO}", formationsHtml);
                htmlTemplate = htmlTemplate.replace("{PROJETOS}", projectsHtml);
                htmlTemplate = htmlTemplate.replace("{EXPERIENCIAS}", experiencesHtml);

                return htmlTemplate;
            } else {
                return "Erro: Arquivo HTML não encontrado.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro na leitura do arquivo HTML.";
        }
    }
}
