package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PedidoDao;
import model.entities.Pedido;

public class PedidoService {
	
	private PedidoDao dao = DaoFactory.createPedidoDao();
	
	public List<Pedido> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Pedido obj) {
		if (obj.getCodigo() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Pedido obj) {
		dao.deleteById(obj.getCodigo());
	}
	
	public List<Pedido> findByStatus(String status) {
		return dao.findByStatus(status);
	}
}
