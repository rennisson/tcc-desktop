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
import model.dao.IngredienteDao;
import model.entities.Ingrediente;

public class IngredienteDaoJDBC implements IngredienteDao {
	
	private Connection conn;
	
	public IngredienteDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Ingrediente obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO ingrediente "
					+ "(descricao, preco, quantidade, peso) "
					+ "VALUES "
					+ "(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getDescricao());
			st.setDouble(2, obj.getPreco());
			st.setInt(3, obj.getQuantidade());
			st.setInt(4, obj.getPeso());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
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
	public void update(Ingrediente obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE ingrediente "
					+ "SET descricao = ?, preco = ?, quantidade = ?, peso = ? "
					+ "WHERE codigo = ?");
			
			st.setString(1, obj.getDescricao());
			st.setDouble(2, obj.getPreco());
			st.setInt(3, obj.getQuantidade());
			st.setInt(4, obj.getPeso());
			st.setInt(5, obj.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
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
					"DELETE FROM ingrediente "
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
	public List<Ingrediente> findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT ingrediente.* "
					+ "FROM ingrediente "
					+ "WHERE ingrediente.codigo LIKE ?");

			st.setString(1, "%" + id + "%");
			rs = st.executeQuery();
			
			List<Ingrediente> list = new ArrayList<Ingrediente>();
			
			while (rs.next()) {
				Ingrediente obj = instantiateIngrediente(rs);
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

	@Override
	public List<Ingrediente> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT ingrediente.* "
					+ "FROM ingrediente "
					+ "ORDER BY descricao ASC");

			rs = st.executeQuery();

			List<Ingrediente> list = new ArrayList<>();

			while (rs.next()) {
				Ingrediente obj = instantiateIngrediente(rs);
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
	
	@Override
	public List<Ingrediente> findByNome(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT ingrediente.* "
					+ "FROM ingrediente "
					+ "WHERE ingrediente.descricao "
					+ "LIKE ? "
					+ "ORDER BY codigo ASC");
			
			st.setString(1, "%" + nome + "%");
			rs = st.executeQuery();

			List<Ingrediente> list = new ArrayList<>();

			while (rs.next()) {
				Ingrediente obj = instantiateIngrediente(rs);
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
	
	private Ingrediente instantiateIngrediente(ResultSet rs) throws SQLException {
		Ingrediente obj = new Ingrediente();
		obj.setId(rs.getInt("codigo"));
		obj.setDescricao(rs.getString("descricao"));
		obj.setPreco(rs.getDouble("preco"));
		obj.setQuantidade(rs.getInt("quantidade"));
		obj.setPeso(rs.getInt("peso"));
		return obj;
	}
}
