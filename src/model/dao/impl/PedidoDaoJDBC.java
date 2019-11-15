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
import model.entities.Produto;

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
					+ "(quantidade, produto_nome, prod_preco, status, end_codigo, cliente, telefone) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getQuantidade());
			st.setString(2, obj.getProduto().getNome());
			st.setDouble(3, obj.getPrecoTotal());
			st.setString(4, obj.getStatus());
			st.setInt(5, obj.getEndereco().getCodigo());
			st.setString(6, obj.getCliente());
			st.setString(7, obj.getTelefone());
			
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
					+ "SET quantidade = ?, produto_nome = ?, prod_preco = ?, status = ?, cliente = ?, telefone = ? "
					+ "WHERE codigo = ?");
			
			st.setInt(1, obj.getQuantidade());
			st.setString(2, obj.getProduto().getNome());
			st.setDouble(3, obj.getPrecoTotal());
			st.setString(4, obj.getStatus());
			st.setString(5, obj.getCliente());
			st.setString(6, obj.getTelefone());
			st.setInt(7, obj.getCodigo());
			
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
	public List<Pedido> findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pedido.* ,endereco.cep as EndCep, endereco.nome as EndNome, endereco.numero as EndNumero, endereco.complemento as EndComplemento, "
					+ "endereco.bairro as EndBairro, endereco.cidade as EndCidade, endereco.estado as EndEstado "
					+ "FROM pedido INNER JOIN endereco "
					+ "ON pedido.end_codigo = endereco.codigo "
					+ "WHERE pedido.codigo LIKE ?");

			st.setString(1, "%" + id + "%");
			rs = st.executeQuery();
			
			List<Pedido> list = new ArrayList<Pedido>();
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
	public List<Pedido> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pedido.*, endereco.cep as EndCep, endereco.nome as EndNome, endereco.numero as EndNumero, endereco.complemento as EndComplemento, "
					+ "endereco.bairro as EndBairro, endereco.cidade as EndCidade, endereco.estado as EndEstado, produto.nome as ProdNome, produto.itens as ProdItens "
					+ "FROM pedido INNER JOIN endereco "
					+ "ON pedido.end_codigo = endereco.codigo "
					+ "INNER JOIN produto "
					+ "ON pedido.produto_nome = produto.nome");

			rs = st.executeQuery();

			List<Pedido> list = new ArrayList<>();
			Map<Integer, Endereco> map = new HashMap<>();
			Map<String, Produto> map2 = new HashMap<>();

			while (rs.next()) {
				
				Endereco endereco = map.get(rs.getInt("codigo"));
				Produto produto = map2.get(rs.getString("produto_nome"));
				
				if (endereco == null) {
					endereco = instantiateEndereco(rs);
					map.put(rs.getInt("codigo"), endereco);
				}
				
				if (produto == null) {
					produto = instantiateProduto(rs);
					map2.put(rs.getString("produto_nome"), produto);
				}
				Pedido obj = instantiatePedido(rs, endereco, produto);
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
					"SELECT pedido.* , endereco.cep as EndCep, endereco.nome as EndNome, endereco.numero as EndNumero, endereco.complemento as EndComplemento, "
					+ "endereco.bairro as EndBairro, endereco.cidade as EndCidade, endereco.estado as EndEstado "
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
		obj.setTelefone(rs.getString("telefone"));
		obj.setQuantidade(rs.getInt("quantidade"));
		obj.setPrecoTotal(rs.getDouble("prod_preco"));
		obj.setStatus(rs.getString("status"));
		obj.setEndereco(endereco);
		return obj;
	}
	
	private Pedido instantiatePedido(ResultSet rs, Endereco endereco, Produto produto) throws SQLException {
		Pedido obj = new Pedido();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setNome(rs.getString("produto_nome"));
		obj.setCliente(rs.getString("cliente"));
		obj.setTelefone(rs.getString("telefone"));
		obj.setQuantidade(rs.getInt("quantidade"));
		obj.setPrecoTotal(rs.getDouble("prod_preco"));
		obj.setStatus(rs.getString("status"));
		obj.setEndereco(endereco);
		obj.setProduto(produto);
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
	
	private Produto instantiateProduto(ResultSet rs) throws SQLException {
		Produto obj = new Produto();
		obj.setNome(rs.getString("produto_nome"));
		return obj;
	}
}
