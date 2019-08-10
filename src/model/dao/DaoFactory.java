package model.dao;

import model.dao.impl.ClienteDaoJDBC;
import model.dao.impl.EnderecoDaoJDBC;
import model.dao.impl.PedidoDaoJDBC;

public class DaoFactory {
	
	public static ClienteDao createClienteDao() {
		return new ClienteDaoJDBC();
	}
	
	public static PedidoDao createPedidoDao() {
		return new PedidoDaoJDBC();
	}
	
	public static EnderecoDao createEnderecoDao() {
		return new EnderecoDaoJDBC();
	}
}
