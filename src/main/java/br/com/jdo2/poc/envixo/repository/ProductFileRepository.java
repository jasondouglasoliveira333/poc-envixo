package br.com.jdo2.poc.envixo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jdo2.poc.envixo.model.ProductFile;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFile, Integer>{

}
