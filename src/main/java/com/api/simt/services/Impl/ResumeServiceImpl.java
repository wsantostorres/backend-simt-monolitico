package com.api.simt.services.Impl;

import com.api.simt.models.StudentModel;
import com.api.simt.services.ResumeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ResumeServiceImpl implements ResumeService{
    public byte[] generateResumesZip(List<StudentModel> students) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {

            for (StudentModel student : students) {
                String curriculoHtml = generateResumesHtml(student);

                // Adicionar o arquivo HTML do currículo ao arquivo ZIP
                ZipEntry entry = new ZipEntry(student.getFullName() + student.getRegistration() + ".html");
                zipOutputStream.putNextEntry(entry);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(curriculoHtml.getBytes(StandardCharsets.UTF_8));
                IOUtils.copy(inputStream, zipOutputStream);
                inputStream.close();
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

                /* Substituir informações do currículo do Aluno aqui */
                htmlTemplate = htmlTemplate.replace("{fullName}", student.getFullName());

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
