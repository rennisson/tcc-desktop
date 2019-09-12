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
import model.dao.ProdutoDao;
import model.entities.Produto;

public class ProdutoDaoJDBC implements ProdutoDao {
	
	private Connection conn;
	
	public ProdutoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Produto obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO produto "
					+ "(nome, peso, preco) "
					+ "VALUES "
					+ "(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getPeso());
			st.setDouble(3, obj.getPreco());
			
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
	public void update(Produto obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE produto "
					+ "SET nome= ?, peso = ?, preco = ? "
					+ "WHERE nome = ?");
			
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getPeso());
			st.setDouble(3, obj.getPreco());
			st.setString(4, obj.getNome());
			
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
	public void deleteByName(String nome) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"DELETE FROM produto "
					+ "WHERE nome = ?");
			
			st.setString(1, nome);
			
			int rows = st.executeUpdate();
			
			if (rows == 0) {
				throw new DbException("Produto requisitado não existe!");
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
	public Produto findByName(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT cliente.* "
					+ "FROM cliente "
					+ "WHERE cliente.codigo = ?");

			st.setString(1, nome);
			rs = st.executeQuery();
			if (rs.next()) {
				Produto obj = instantiateProduto(rs);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Produto> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT produto.* "
					+ "FROM produto "
					+ "ORDER BY nome ASC");

			rs = st.executeQuery();

			List<Produto> list = new ArrayList<>();

			while (rs.next()) {
				Produto obj = instantiateProduto(rs);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Produto instantiateProduto(ResultSet rs) throws SQLException {
		Produto obj = new Produto();
		obj.setNome(rs.getString("nome"));
		obj.setPeso(rs.getInt("peso"));
		obj.setPreco(rs.getDouble("preco"));
		return obj;
	}
}
