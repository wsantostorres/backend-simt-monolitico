package com.api.simt.services;

import com.api.simt.dtos.ResumeDto;
import com.api.simt.models.ResumeModel;
import org.springframework.stereotype.Service;

@Service
public interface ResumeService {
    public ResumeModel getResume(long id);
    public ResumeModel createResume(ResumeDto resumeDto);
    public ResumeModel updateResume(long id, ResumeDto resumeDto);
}
