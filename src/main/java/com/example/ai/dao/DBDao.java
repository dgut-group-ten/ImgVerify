package com.example.ai.dao;

import com.example.ai.domain.Image;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBDao extends JpaSpecificationExecutor<Image> , PagingAndSortingRepository<Image, Long> {
}
