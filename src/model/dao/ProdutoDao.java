package model.dao;

import java.util.List;

import model.entities.Produto;

public interface ProdutoDao {
	
	void insert(Produto obj);
	void update(Produto obj);
	void deleteByName(String nome);
	List<Produto> findByName(String nome);
	List<Produto> findAll();
}
