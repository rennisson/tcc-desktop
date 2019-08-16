package model.dao;

import db.DB;
import model.dao.impl.ClienteDaoJDBC;
import model.dao.impl.EnderecoDaoJDBC;
import model.dao.impl.PedidoDaoJDBC;

public class DaoFactory {
	
	public static ClienteDao createClienteDao() {
		return new ClienteDaoJDBC(DB.getConnection());
	}
	
	public static PedidoDao createPedidoDao() {
		return new PedidoDaoJDBC(DB.getConnection());
	}
	
	public static EnderecoDao createEnderecoDao() {
		return new EnderecoDaoJDBC();
	}
}
