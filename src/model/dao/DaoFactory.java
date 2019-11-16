package model.dao;

import db.DB;
import model.dao.impl.EnderecoDaoJDBC;
import model.dao.impl.IngredienteDaoJDBC;
import model.dao.impl.PedidoDaoJDBC;
import model.dao.impl.ProdutoDaoJDBC;

public class DaoFactory {
	
	public static PedidoDao createPedidoDao() {
		return new PedidoDaoJDBC(DB.getConnection());
	}
	
	public static EnderecoDao createEnderecoDao() {
		return new EnderecoDaoJDBC(DB.getConnection());
	}

	public static ProdutoDao createProdutoDao() {
		return new ProdutoDaoJDBC(DB.getConnection());
	}

	public static IngredienteDao createIngredienteDao() {
		return new IngredienteDaoJDBC(DB.getConnection());
	}
}
