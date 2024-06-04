package com.mydrive.backend.services;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CloudStorageServiceFactory {
    private final BeanFactory beanFactory;

    @Autowired
    public CloudStorageServiceFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public CloudStorageService getCloudService(String cloudService) {
        return beanFactory.getBean(cloudService, CloudStorageService.class);

    }
}
