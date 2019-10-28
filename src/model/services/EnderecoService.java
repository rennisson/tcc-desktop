package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.EnderecoDao;
import model.entities.Endereco;

public class EnderecoService {
	
	private EnderecoDao dao = DaoFactory.createEnderecoDao();
	
	public List<Endereco> findAll(){
		return dao.findAll();
	}
	
	public Endereco saveOrUpdate(Endereco obj) {
		if (obj.getCodigo() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
		return obj;
	}
	
	public void remove(Endereco obj) {
		dao.deleteById(obj.getCodigo());
	}
	
	public Endereco update(Endereco obj) {
		dao.update(obj);
		return obj;
	}
	
	public Endereco findById(Integer id) {
		return dao.findById(id);
	}
	
//	public List<Endereco> findByNome(String nome) {
//		return dao.findByNome(nome);
//	}
}
