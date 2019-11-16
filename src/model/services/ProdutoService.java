package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProdutoDao;
import model.entities.Produto;

public class ProdutoService {
	
	private ProdutoDao dao = DaoFactory.createProdutoDao();
	
	public List<Produto> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Produto obj) {
		List<Produto> list = dao.findByName(obj.getNome());
		
		if (list.size() <= 0) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Produto obj) {
		dao.deleteByName(obj.getNome());
	}
	
	public List<Produto> findByName(String nome) {
		return dao.findByName(nome);
	}
}
