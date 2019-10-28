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
import model.entities.Endereco;
import model.entities.Pedido;

public class EnderecoDaoJDBC implements EnderecoDao {
	
	private Connection conn;
	
	public EnderecoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Endereco insert(Endereco obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO endereco "
					+ "(cep, nome, complemento, bairro, cidade, estado, numero) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getCep());
			st.setString(2, obj.getRua());
			st.setString(3, obj.getComplemento());
			st.setString(4, obj.getBairro());
			st.setString(5, obj.getCidade());
			st.setString(6, obj.getEstado());
			st.setString(7, obj.getNumero());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setCodigo(id);
				}
				DB.closeResultSet(rs);
				return obj;
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
					+ "WHERE codigo = ?");
			
			st.setString(1, obj.getCep());
			st.setString(2, obj.getRua());
			st.setString(3, obj.getComplemento());
			st.setString(4, obj.getBairro());
			st.setString(5, obj.getCidade());
			st.setString(6, obj.getEstado());
			st.setString(7, obj.getNumero());
			st.setInt(8, obj.getCodigo());
			
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
	public Endereco findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT endereco.*, pedido.codigo as PedCodigo, pedido.produto_nome as PedNome, pedido.quantidade as PedQuantidade, "
							+ "pedido.prod_preco as PedPreco, pedido.status as PedStatus "
							+ "FROM endereco INNER JOIN pedido "
							+ "ON endereco.codigo = pedido.end_codigo "
							+ "WHERE endereco.codigo = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			
			Map<Integer, Pedido> map = new HashMap<>();
			
			while (rs.next()) {
				
				Pedido pedido = map.get(rs.getInt("codigo"));
				
				if(pedido == null) {
					pedido = instantiatePedido(rs);
					map.put(rs.getInt("codigo"), pedido);
				}
				Endereco obj = instantiateEndereco(rs, pedido);
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
					"SELECT endereco.*, pedido.codigo as PedCodigo, pedido.produto_nome as ProdNome, pedido.quantidade as ProdQuantidade, "
					+ "pedido.prod_preco as ProdPreco, pedido.status as ProdStatus "
					+ "FROM endereco INNER JOIN pedido "
					+ "ON endereco.codigo = pedido.end_codigo");

			rs = st.executeQuery();

			List<Endereco> list = new ArrayList<>();
			Map<Integer, Pedido> map = new HashMap<>();

			while (rs.next()) {
				
				Pedido pedido = map.get(rs.getInt("codigo"));
				
				if (pedido == null) {
					pedido = instantiatePedido(rs);
					map.put(rs.getInt("codigo"), pedido);
				}
				Endereco obj = instantiateEndereco(rs, pedido);
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
	
	private Endereco instantiateEndereco(ResultSet rs, Pedido pedido) throws SQLException {
		Endereco obj = new Endereco();
		obj.setCep(rs.getString("cep"));
		obj.setRua(rs.getString("nome"));
		obj.setComplemento(rs.getString("complemento"));
		obj.setBairro(rs.getString("bairro"));
		obj.setCidade(rs.getString("cidade"));
		obj.setEstado(rs.getString("estado"));
		obj.setNumero(rs.getString("numero"));
		//obj.setPedido(pedido);
		return obj;
	}
	
	private Pedido instantiatePedido(ResultSet rs) throws SQLException {
		Pedido obj = new Pedido();
		obj.setCodigo(rs.getInt("PedCodigo"));
		obj.setNome(rs.getString("PedNome"));
		obj.setQuantidade(rs.getInt("PedQuantidade"));
		obj.setPrecoTotal(rs.getDouble("PedPreco"));
		obj.setStatus(rs.getString("PedStatus"));
		return obj;
	}
}
