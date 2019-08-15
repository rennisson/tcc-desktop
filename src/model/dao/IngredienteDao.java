package model.dao;

import java.util.List;

import model.entities.Ingrediente;

public interface IngredienteDao {
	
	void insert(Ingrediente obj);
	void update(Ingrediente obj);
	void deleteById(Integer id);
	Ingrediente findById(Integer id);
	List<Ingrediente> findAll();
}
