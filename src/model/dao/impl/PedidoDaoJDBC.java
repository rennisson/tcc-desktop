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
import model.dao.PedidoDao;
import model.entities.Endereco;
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
					+ "(quantidade, produto_nome, prod_preco, status, end_codigo) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getQuantidade());
			st.setString(2, obj.getProduto().getNome());
			st.setDouble(3, obj.getPrecoTotal());
			st.setString(4, obj.getStatus());
			st.setInt(5, obj.getEndereco().getCodigo());
			
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
					+ "SET quantidade = ?, produto_nome = ?, prod_preco = ?, status = ? "
					+ "WHERE codigo = ?");
			
			st.setInt(1, obj.getQuantidade());
			st.setString(2, obj.getProduto().getNome());
			st.setDouble(3, obj.getPrecoTotal());
			st.setString(4, obj.getStatus());
			st.setInt(5, obj.getCodigo());
			
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
				throw new DbException("ID requisitado n�o existe!");
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
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pedido.*, endereco.nome as EndNome, endereco.numero as EndNumero, endereco.bairro as EndBairro "
					+ "FROM pedido INNER JOIN endereco "
					+ "ON pedido.end_codigo = endereco.codigo "
					+ "WHERE pedido.codigo = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			
			Map<Integer, Endereco> map = new HashMap<>();
			
			while (rs.next()) {
				
				Endereco endereco = map.get(rs.getInt("ped_codigo"));
				
				if(endereco == null) {
					endereco = instantiateEndereco(rs);
					map.put(rs.getInt("ped_codigo"), endereco);
				}
				Pedido obj = instantiatePedido(rs, endereco);
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
	public List<Pedido> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pedido.*, endereco.cep as EndCep, endereco.nome as EndNome, endereco.numero as EndNumero, endereco.complemento as EndComplemento, "
					+ "endereco.bairro as EndBairro, endereco.cidade as EndCidade, endereco.estado as EndEstado "
					+ "FROM pedido INNER JOIN endereco "
					+ "ON pedido.end_codigo = endereco.codigo");

			rs = st.executeQuery();

			List<Pedido> list = new ArrayList<>();
			Map<Integer, Endereco> map = new HashMap<>();

			while (rs.next()) {
				
				Endereco endereco = map.get(rs.getInt("codigo"));
				
				if (endereco == null) {
					endereco = instantiateEndereco(rs);
					map.put(rs.getInt("codigo"), endereco);
				}
				Pedido obj = instantiatePedido(rs, endereco);
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
	public List<Pedido> findByStatus(String status) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pedido.* , endereco.nome as EndNome, endereco.numero as EndNumero, endereco.bairro as EndBairro "
					+ "FROM pedido INNER JOIN endereco "
					+ "ON pedido.end_codigo = endereco.codigo "
					+ "WHERE pedido.status = ?");

			st.setString(1, status);
			rs = st.executeQuery();

			List<Pedido> list = new ArrayList<>();
			Map<Integer, Endereco> map = new HashMap<>();

			while (rs.next()) {
				
				Endereco endereco = map.get(rs.getInt("end_codigo"));
				
				if (endereco == null) {
					endereco = instantiateEndereco(rs);
					map.put(rs.getInt("end_codigo"), endereco);
				}
				Pedido obj = instantiatePedido(rs, endereco);
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
	public List<Pedido> findByCliente(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pedido.* ,endereco.cep as EndCep, endereco.nome as EndNome, endereco.numero as EndNumero, endereco.complemento as EndComplemento, "
					+ "endereco.bairro as EndBairro, endereco.cidade as EndCidade, endereco.estado as EndEstado "
					+ "FROM pedido INNER JOIN endereco "
					+ "ON pedido.end_codigo = endereco.codigo "
					+ "WHERE pedido.cliente LIKE ?");

			st.setString(1, "%" + nome + "%");
			rs = st.executeQuery();

			List<Pedido> list = new ArrayList<>();
			Map<Integer, Endereco> map = new HashMap<>();

			while (rs.next()) {
				
				Endereco endereco = map.get(rs.getInt("end_codigo"));
				
				if (endereco == null) {
					endereco = instantiateEndereco(rs);
					map.put(rs.getInt("end_codigo"), endereco);
				}
				Pedido obj = instantiatePedido(rs, endereco);
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
	
	private Pedido instantiatePedido(ResultSet rs, Endereco endereco) throws SQLException {
		Pedido obj = new Pedido();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setNome(rs.getString("produto_nome"));
		obj.setCliente(rs.getString("cliente"));
		obj.setQuantidade(rs.getInt("quantidade"));
		obj.setPrecoTotal(rs.getDouble("prod_preco"));
		obj.setStatus(rs.getString("status"));
		obj.setEndereco(endereco);
		return obj;
	}

	private Endereco instantiateEndereco(ResultSet rs) throws SQLException {
		Endereco obj = new Endereco();
		obj.setCodigo(rs.getInt("end_codigo"));
		obj.setCep(rs.getString("EndCep"));
		obj.setRua(rs.getString("EndNome"));
		obj.setComplemento(rs.getString("EndComplemento"));
		obj.setBairro(rs.getString("EndBairro"));
		obj.setCidade(rs.getString("EndCidade"));
		obj.setEstado(rs.getString("EndEstado"));
		obj.setNumero(rs.getString("EndNumero"));
		return obj;
	}
}
