package br.com.jdo2.poc.envixo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jdo2.poc.envixo.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

}
