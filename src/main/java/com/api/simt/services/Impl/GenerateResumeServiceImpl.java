package com.api.simt.services.Impl;

import com.api.simt.models.StudentModel;
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
