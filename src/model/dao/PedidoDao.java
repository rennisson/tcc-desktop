package model.dao;

import java.util.List;

import model.entities.Cliente;
import model.entities.Pedido;

public interface PedidoDao {
	
	void insert(Pedido obj);
	void update(Pedido obj);
	void deleteById(Integer id);
	Pedido findById(Integer id);
	List<Pedido> findAll();
	List<Pedido> findByCliente(Cliente cliente);
	List<Pedido> findByStatus(String status);
}
