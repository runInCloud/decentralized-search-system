package com.fly.house.service.file;

import com.fly.house.dao.repository.ArtifactRepository;
import com.fly.house.model.Artifact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by dimon on 04.05.14.
 */
@Service
public class ArtifactServiceImpl implements ArtifactService {

    @Autowired
    private ArtifactRepository artifactRepository;


    @Override
    public Page<Artifact> searchOnlyAvailable(String queryString, Pageable pageable) {
        return artifactRepository.searchOnlyAvailable(queryString, pageable);
    }

    @Override
    public Page<Artifact> search(String queryString, Pageable pageRequest) {
        return artifactRepository.search(queryString, pageRequest);
    }

    @Override
    public Artifact save(Artifact artifact) {
        return artifactRepository.save(artifact);
    }

    @Override
    public void update(Artifact artifact) {
        artifactRepository.update(artifact);
    }

    @Override
    public void delete(Long id) {
        artifactRepository.delete(id);
    }

    @Override
    public Artifact findOne(Long id) {
        return artifactRepository.findOne(id);
    }

    @Override
    public void index(Long id) {
        artifactRepository.index(id);
    }

    @Override
    public long size() {
        return artifactRepository.size();
    }

}
