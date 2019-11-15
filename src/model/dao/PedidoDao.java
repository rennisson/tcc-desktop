package model.dao;

import java.util.List;

import model.entities.Pedido;

public interface PedidoDao {
	
	void insert(Pedido obj);
	void update(Pedido obj);
	void deleteById(Integer id);
	List<Pedido> findById(Integer id);
	List<Pedido> findAll();
	List<Pedido> findByCliente(String nome);
	List<Pedido> findByStatus(String status);
}
