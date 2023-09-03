package com.api.simt.services;

import com.api.simt.models.StudentModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResumeService {
    public byte[] generateResumesZip(List<StudentModel> students);
}
