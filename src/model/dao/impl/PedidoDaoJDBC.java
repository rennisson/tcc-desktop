package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.PedidoDao;
import model.entities.Cliente;
import model.entities.Pedido;

public class PedidoDaoJDBC implements PedidoDao {
	
	private Connection conn;
	
	public PedidoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Pedido obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO pedido "
					+ "(codigo, cliente_codigo, quantidade, nome) "
					+ "VALUES "
					+ "(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, obj.getCodigo());
			st.setInt(2, obj.getCliente().getCodigo());
			st.setInt(3, obj.getQuantidade());
			st.setString(4, obj.getNome());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setCodigo(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Erro inesperado! Nenhuma linha afetada!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Pedido obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE pedido "
					+ "SET quantidade = ?, nome= ? "
					+ "WHERE codigo = ?");
			
			st.setInt(1, obj.getQuantidade());
			st.setString(2, obj.getNome());
			st.setInt(3, obj.getCodigo());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"DELETE FROM pedido "
					+ "WHERE codigo = ?");
			
			st.setInt(1, id);
			
			int rows = st.executeUpdate();
			
			if (rows == 0) {
				throw new DbException("ID requisitado não existe!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Pedido findById(Integer id) {
		return null;
		
	}
	
	private Pedido instantiatePedido(ResultSet rs) throws SQLException {
		Pedido obj = new Pedido();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setNome(rs.getString("nome"));
		obj.setQuantidade(rs.getInt("quantidade"));
		return obj;
	}

	@Override
	public List<Pedido> findAll() {
		return null;
	}
	

}
