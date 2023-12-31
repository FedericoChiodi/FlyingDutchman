package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.*;
import com.ingsw.flyingdutchman.services.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class MySQLJDBCDAOFactory extends DAOFactory {

    private Map factoryParameters;
    private Connection connection;

    public MySQLJDBCDAOFactory(Map factoryParameters){this.factoryParameters = factoryParameters;}

    @Override
    public void beginTransaction() {
        try{
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);
        }
        catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {
        try {
            this.connection.rollback();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOMySQLJDBCImpl(connection);
    }

    @Override
    public ProductDAO getProductDAO() {
        return new ProductDAOMySQLJDBCImpl(connection);
    }

    @Override
    public AuctionDAO getAuctionDAO() {
        return new AuctionDAOMySQLJDBCImpl(connection);
    }

    @Override
    public CategoryDAO getCategoryDAO() {
        return new CategoryDAOMySQLJDBCImpl(connection);
    }

    @Override
    public OrderDAO getOrderDAO() {
        return new OrderDAOMySQLJDBCImpl(connection);
    }

    @Override
    public ThresholdDAO getThresholdDAO() {
        return new ThresholdDAOMySQLJDBCImpl(connection);
    }
}
