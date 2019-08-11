package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.ClienteDao;
import model.entities.Cliente;

public class ClienteDaoJDBC implements ClienteDao {
	
	private Connection conn;
	
	public ClienteDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Cliente obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO cliente "
					+ "(email, nome, telefone) "
					+ "VALUES "
					+ "(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getEmail());
			st.setString(2, obj.getNome());
			st.setString(3, obj.getTelefone());
			
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
	public void update(Cliente obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE cliente "
					+ "SET email = ?, nome= ?, telefone = ? "
					+ "WHERE codigo = ?");
			
			st.setString(1, obj.getEmail());
			st.setString(2, obj.getNome());
			st.setString(3, obj.getTelefone());
			st.setInt(4, obj.getCodigo());
			
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
					"DELETE FROM cliente "
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
	public Cliente findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT cliente.* "
					+ "FROM cliente "
					+ "WHERE cliente.codigo = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Cliente obj = instantiateCliente(rs);
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

	private Cliente instantiateCliente(ResultSet rs) throws SQLException {
		Cliente obj = new Cliente();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setNome(rs.getString("nome"));
		obj.setEmail(rs.getString("email"));
		obj.setTelefone(rs.getString("telefone"));
		return obj;
	}

	@Override
	public List<Cliente> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT cliente.* "
					+ "FROM cliente "
					+ "ORDER BY codigo ASC");

			rs = st.executeQuery();

			List<Cliente> list = new ArrayList<>();

			while (rs.next()) {
				Cliente obj = instantiateCliente(rs);
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

}
