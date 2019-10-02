package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ClienteDao;
import model.entities.Cliente;

public class ClienteService {
	
	private ClienteDao dao = DaoFactory.createClienteDao();
	
	public List<Cliente> findAll(){
		return dao.findAll();
	}
	
	public Cliente findById(Integer id) {
		return dao.findById(id);
	}
	
	public void saveOrUpdate(Cliente obj) {
		if (obj.getCodigo() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Cliente obj) {
		dao.deleteById(obj.getCodigo());
	}

	public List<Cliente> findByCodigo(Integer id) {
		return dao.findByCodigo(id);
	}
	
}
