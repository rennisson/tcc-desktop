package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.EnderecoDao;
import model.entities.Endereco;

public class EnderecoDaoJDBC implements EnderecoDao {
	
	private Connection conn;
	
	public EnderecoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Endereco obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO endereco "
					+ "(cep, cli_codigo, nome, complemento, bairro, cidade, estado, numero) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?, ?)");
			
			st.setString(1, obj.getCep());
			st.setInt(2, obj.getCliente().getCodigo());
			st.setString(3, obj.getRua());
			st.setString(4, obj.getComplemento());
			st.setString(5, obj.getBairro());
			st.setString(6, obj.getCidade());
			st.setString(7, obj.getEstado());
			st.setString(8, obj.getNumero());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				System.out.println("Dados inseridos com sucesso!");
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
	public void update(Endereco obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE endereco "
					+ "SET cep = ?, nome = ?, complemento = ?, bairro = ?, cidade = ?, estado = ?, numero = ? "
					+ "WHERE codigo_cliente = ?");
			
			st.setString(1, obj.getCep());
			st.setString(2, obj.getRua());
			st.setString(3, obj.getComplemento());
			st.setString(4, obj.getBairro());
			st.setString(5, obj.getCidade());
			st.setString(6, obj.getEstado());
			st.setString(7, obj.getNumero());
			st.setInt(8, obj.getCliente().getCodigo());
			
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public Endereco findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Endereco> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
