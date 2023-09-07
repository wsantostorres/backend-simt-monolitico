package com.api.simt.services;

import com.api.simt.dtos.ResumeDto;
import com.api.simt.models.ResumeModel;
import org.springframework.stereotype.Service;

@Service
public interface ResumeService {

    public ResumeModel createResume(ResumeDto resumeDto);
}
