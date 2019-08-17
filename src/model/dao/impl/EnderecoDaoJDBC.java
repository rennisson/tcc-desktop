package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.EnderecoDao;
import model.entities.Cliente;
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
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"DELETE FROM endereco "
					+ "WHERE cli_codigo = ?");
			
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
	public Endereco findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT endereco.*,cliente.nome as CliNome, cliente.email as CliEmail, cliente.telefone as CliTel "
					+ "FROM endereco INNER JOIN cliente "
					+ "ON endereco.cli_codigo = cliente.codigo "
					+ "WHERE endereco.cli_codigo = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			
			Map<Integer, Cliente> map = new HashMap<>();
			
			while (rs.next()) {
				
				Cliente cliente = map.get(rs.getInt("cli_codigo"));
				
				if(cliente == null) {
					cliente = instantiateCliente(rs);
					map.put(rs.getInt("cli_codigo"), cliente);
				}
				Endereco obj = instantiateEndereco(rs, cliente);
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
	public List<Endereco> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT endereco.*, cliente.nome as CliNome, cliente.email as CliEmail, cliente.telefone as CliTel "
					+ "FROM endereco INNER JOIN cliente "
					+ "ON endereco.cli_codigo = cliente.codigo ");

			rs = st.executeQuery();

			List<Endereco> list = new ArrayList<>();
			Map<Integer, Cliente> map = new HashMap<>();

			while (rs.next()) {
				
				Cliente cliente = map.get(rs.getInt("cli_codigo"));
				
				if (cliente == null) {
					cliente = instantiateCliente(rs);
					map.put(rs.getInt("cli_codigo"), cliente);
				}
				Endereco obj = instantiateEndereco(rs, cliente);
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
	
	private Endereco instantiateEndereco(ResultSet rs, Cliente cliente) throws SQLException {
		Endereco obj = new Endereco();
		obj.setCep(rs.getString("cep"));
		obj.setRua(rs.getString("nome"));
		obj.setComplemento(rs.getString("complemento"));
		obj.setBairro(rs.getString("bairro"));
		obj.setCidade(rs.getString("cidade"));
		obj.setEstado(rs.getString("estado"));
		obj.setNumero(rs.getString("numero"));
		obj.setCliente(cliente);
		return obj;
	}
	
	private Cliente instantiateCliente(ResultSet rs) throws SQLException {
		Cliente obj = new Cliente();
		obj.setCodigo(rs.getInt("cli_codigo"));
		obj.setNome(rs.getString("CliNome"));
		obj.setEmail(rs.getString("CliEmail"));
		obj.setTelefone(rs.getString("CliTel"));
		return obj;
	}
}
