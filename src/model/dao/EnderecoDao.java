package model.dao;

import java.util.List;

import model.entities.Endereco;

public interface EnderecoDao {
	
	Endereco insert(Endereco obj);
	void update(Endereco obj);
	void deleteById(Integer id);
	Endereco findById(Integer id);
	List<Endereco> findAll();
}
