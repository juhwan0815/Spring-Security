package io.security.corespringsecurity.service.impl;

import io.security.corespringsecurity.domain.entity.Resources;
import io.security.corespringsecurity.repository.ResourcesRepository;
import io.security.corespringsecurity.service.ResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ResourceServiceImpl implements ResourcesService {

    @Autowired
    private ResourcesRepository resourcesRepository;

    @Override
    @Transactional
    public Resources getResources(Long id) {
        return resourcesRepository.findById(id).orElse(new Resources());
    }

    @Override
    @Transactional
    public List<Resources> getResources() {
        return resourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    @Override
    @Transactional
    public void createResources(Resources resources) {
        resourcesRepository.save(resources);
    }

    @Override
    @Transactional
    public void deleteResources(Long id) {
        resourcesRepository.deleteById(id);
    }
}
