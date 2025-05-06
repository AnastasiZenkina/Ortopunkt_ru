package com.example.medicalcrm.service;
import com.example.medicalcrm.entity.Application;
import com.example.medicalcrm.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List <Application> getAllApplications(){
        return applicationRepository.findAll();
    }

    public Optional <Application> getApplicationById(Long id){
        return applicationRepository.findById(id);
    }

    public Application saveApplication(Application application){
        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id){
        applicationRepository.deleteById(id);
    }
}
