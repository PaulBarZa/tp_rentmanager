package com.epf.rentmanager.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.dao.utils.DaoOutils;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {

	private DaoOutils daoOutils;

	@Autowired
	public ClientDao(DaoOutils daoOutils) {
		this.daoOutils = daoOutils;
	}

	private static final String COUNT_CLIENTS = "SELECT COUNT(id) AS count FROM Client;";
	private static final String FIND_CLIENT_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String UPDATE_CLIENT_QUERY = "UPDATE Client SET nom = ?, prenom = ?, email = ?, naissance = ? WHERE id = ?;";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";

	/**
	 * Count numbers of client in Client table
	 * 
	 * @return number of clients
	 * @throws DaoException
	 */
	public int count() throws DaoException {
		try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(COUNT_CLIENTS, false);
				ResultSet resultSet = preparedStatement.executeQuery();) {

			if (resultSet.next()) {
				return resultSet.getInt("count");

			} else {
				throw new DaoException("Failed to count clients");
			}

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Find a client in Client table think his id
	 * 
	 * @param id
	 * @return An optional client corresponding with the id
	 * @throws DaoException
	 */
	public Optional<Client> findById(long id) throws DaoException {
		try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(FIND_CLIENT_QUERY, false);) {

			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {

				Client client = resultToClient(resultSet);
				resultSet.close();
				return Optional.of(client);

			} else {
				resultSet.close();
				return Optional.empty();
			}

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Return all clients from Client table
	 * 
	 * @return list of clients
	 * @throws DaoException
	 */
	public List<Client> findAll() throws DaoException {
		List<Client> clients = new ArrayList<>();
		try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(FIND_CLIENTS_QUERY, false);
				ResultSet resultSet = preparedStatement.executeQuery();) {

			while (resultSet.next()) {
				clients.add(resultToClient(resultSet));
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
		return clients;
	}

	/**
	 * Create a client in Client table
	 * 
	 * @param client
	 * @return client key
	 * @throws DaoException
	 */
	public int create(Client client) throws DaoException {
		try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(CREATE_CLIENT_QUERY, true);) {

			PreparedStatement completedPreparedStatement = completePreparedStatement(preparedStatement, client);
			completedPreparedStatement.executeUpdate();
			ResultSet resultSet = completedPreparedStatement.getGeneratedKeys();

			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new DaoException("Failed to get client keys during creation");
			}

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Update a client in Client table
	 * 
	 * @param client
	 * @return client key
	 * @throws DaoException
	 */
	public int update(Client client) throws DaoException {
		try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(UPDATE_CLIENT_QUERY, false);) {

			PreparedStatement completedPreparedStatement = completePreparedStatement(preparedStatement, client);
			completedPreparedStatement.setLong(5, client.getId());
			int statementStatus = completedPreparedStatement.executeUpdate();

			if (statementStatus != 1) {
				throw new DaoException("This client doesn't exists");
			}
			return statementStatus;

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Delete a client in Client table
	 * 
	 * @param client
	 * @return statement status
	 * @throws DaoException
	 */
	public int delete(Client client) throws DaoException {
		try (PreparedStatement preparedStatement = daoOutils.initPreparedStatement(DELETE_CLIENT_QUERY, false);) {

			preparedStatement.setInt(1, client.getId());
			int statementStatus = preparedStatement.executeUpdate();

			if (statementStatus != 1) {
				throw new DaoException("This client doesn't exists");
			}
			return statementStatus;

		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Complete a PreparedStatement with client data
	 * 
	 * @param preparedStatement
	 * @param client
	 * @return PreparedStatement completed
	 * @throws SQLException
	 */
	public PreparedStatement completePreparedStatement(PreparedStatement preparedStatement, Client client)
			throws SQLException {
		preparedStatement.setString(1, client.getNom());
		preparedStatement.setString(2, client.getPrenom());
		preparedStatement.setString(3, client.getEmail());
		preparedStatement.setDate(4, Date.valueOf(client.getNaissance()));
		return preparedStatement;
	}

	/**
	 * Return a client from a resultSet
	 * 
	 * @param resultSet
	 * @return client corresponding to the result
	 * @throws SQLException
	 */
	public Client resultToClient(ResultSet resultSet) throws SQLException {
		return new Client(resultSet.getInt("id"), resultSet.getString("nom"), resultSet.getString("prenom"),
				resultSet.getString("email"), resultSet.getDate("naissance").toLocalDate());
	}

}
