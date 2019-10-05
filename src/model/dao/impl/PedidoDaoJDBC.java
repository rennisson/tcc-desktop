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
					+ "(cli_codigo, quantidade, produto_nome, prod_preco, status) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, obj.getCliente().getCodigo());
			st.setInt(2, obj.getQuantidade());
			st.setString(3, obj.getProduto().getNome());
			st.setDouble(4, obj.getPrecoTotal());
			st.setString(5, obj.getStatus());
			
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
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pedido.*,cliente.nome as CliNome, cliente.email as CliEmail, cliente.telefone as CliTel "
					+ "FROM pedido INNER JOIN cliente "
					+ "ON pedido.cliente_codigo = cliente.codigo "
					+ "WHERE pedido.codigo = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			
			Map<Integer, Cliente> map = new HashMap<>();
			
			while (rs.next()) {
				
				Cliente cliente = map.get(rs.getInt("cliente_codigo"));
				
				if(cliente == null) {
					cliente = instantiateCliente(rs);
					map.put(rs.getInt("cliente_codigo"), cliente);
				}
				Pedido obj = instantiatePedido(rs, cliente);
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
					"SELECT pedido.* , cliente.nome as CliNome, cliente.email as CliEmail, cliente.telefone as CliTel "
					+ "FROM pedido INNER JOIN cliente "
					+ "ON pedido.cli_codigo = cliente.codigo");

			rs = st.executeQuery();

			List<Pedido> list = new ArrayList<>();
			Map<Integer, Cliente> map = new HashMap<>();

			while (rs.next()) {
				
				Cliente cliente = map.get(rs.getInt("cli_codigo"));
				
				if (cliente == null) {
					cliente = instantiateCliente(rs);
					map.put(rs.getInt("cli_codigo"), cliente);
				}
				Pedido obj = instantiatePedido(rs, cliente);
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
					"SELECT pedido.* , cliente.nome as CliNome, cliente.email as CliEmail, cliente.telefone as CliTel "
					+ "FROM pedido INNER JOIN cliente "
					+ "ON pedido.cli_codigo = cliente.codigo "
					+ "WHERE pedido.status = ?");

			st.setString(1, status);
			rs = st.executeQuery();

			List<Pedido> list = new ArrayList<>();
			Map<Integer, Cliente> map = new HashMap<>();

			while (rs.next()) {
				
				Cliente cliente = map.get(rs.getInt("cli_codigo"));
				
				if (cliente == null) {
					cliente = instantiateCliente(rs);
					map.put(rs.getInt("cli_codigo"), cliente);
				}
				Pedido obj = instantiatePedido(rs, cliente);
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
					"SELECT pedido.* , cliente.nome as CliNome, cliente.email as CliEmail, cliente.telefone as CliTel "
					+ "FROM pedido INNER JOIN cliente "
					+ "ON pedido.cli_codigo = cliente.codigo "
					+ "WHERE cliente.nome LIKE ?");

			st.setString(1, "%" + nome + "%");
			rs = st.executeQuery();

			List<Pedido> list = new ArrayList<>();
			Map<Integer, Cliente> map = new HashMap<>();

			while (rs.next()) {
				
				Cliente cliente = map.get(rs.getInt("cli_codigo"));
				
				if (cliente == null) {
					cliente = instantiateCliente(rs);
					map.put(rs.getInt("cli_codigo"), cliente);
				}
				Pedido obj = instantiatePedido(rs, cliente);
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
	
	private Pedido instantiatePedido(ResultSet rs, Cliente cliente) throws SQLException {
		Pedido obj = new Pedido();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setNome(rs.getString("produto_nome"));
		obj.setQuantidade(rs.getInt("quantidade"));
		//obj.getProduto().setNome("produto_nome");
		obj.setPrecoTotal(rs.getDouble("prod_preco"));
		obj.setStatus(rs.getString("status"));
		obj.setCliente(cliente);
		return obj;
	}

	private Cliente instantiateCliente(ResultSet rs) throws SQLException {
		Cliente obj = new Cliente();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setNome(rs.getString("CliNome"));
		obj.setEmail(rs.getString("CliEmail"));
		obj.setTelefone(rs.getString("CliTel"));
		return obj;
	}
}
