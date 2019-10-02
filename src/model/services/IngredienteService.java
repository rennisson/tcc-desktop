package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.IngredienteDao;
import model.entities.Cliente;
import model.entities.Ingrediente;

public class IngredienteService {
	
	private IngredienteDao dao = DaoFactory.createIngredienteDao();
	
	public List<Ingrediente> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Ingrediente obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Ingrediente obj) {
		dao.deleteById(obj.getId());
	}
	
	public List<Ingrediente> findByNome(String nome) {
		return dao.findByNome(nome);
	}
}
