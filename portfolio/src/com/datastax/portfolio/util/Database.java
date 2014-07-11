package com.datastax.portfolio.util;




import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.portfolio.model.*;




public class Database {
	private String[] nodes;
	
	private String keyspace;
	private Cluster cluster;
	private Session session;
	
	private PreparedStatement psInsertHistoricalPrices;
	
	
		
	public String[] getNodes() {
		return nodes;
	}

	public void setNodes(String[] nodes) {
		this.nodes = nodes;
	}

	public Database(String nodes[], String keyspace){
		setNodes(nodes);
		setKeyspace(keyspace);
		connect();		
	}
	
	private void connect() {
		System.out.println("Creating Cluster");
		
		Builder builder = Cluster.builder();
		builder.addContactPoints(nodes);
	
		System.out.println("Setting Options");
		
		//Connection Options
		//builder.socketOptions().setConnectTimeoutMillis(30000);
		//builder.socketOptions().setReadTimeoutMillis(10000);
		builder.withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE);
		builder.withReconnectionPolicy(new ConstantReconnectionPolicy(100));
		
		System.out.println("Building Cluster");
		
		cluster = builder.build();
		session = cluster.connect(keyspace);
		
		System.out.println("Retrieved Session");
	}
	

	public String getKeyspace() {
		return keyspace;
	}
	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}
	public Cluster getCluster() {
		return cluster;
	}
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	
	public String insertHistoricalPrices(Stock stock) {
		if (psInsertHistoricalPrices == null) {
			String sInsertHistoricalPrices = "INSERT INTO stock_historical_price (ticker, date, close, volume, open, high, low) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?);";
			psInsertHistoricalPrices = session.prepare(sInsertHistoricalPrices);
		}
		
		BoundStatement boundStatement = new BoundStatement(psInsertHistoricalPrices);
		
		boundStatement.setString("ticker", stock.getTicker());
		boundStatement.setString("date",stock.getDate());
		boundStatement.setFloat("close", stock.getClose());
		boundStatement.setDouble("volume", stock.getVolume());
		boundStatement.setFloat("open", stock.getOpen());
		boundStatement.setFloat("high", stock.getHigh());
		boundStatement.setFloat("low", stock.getLow());
		session.execute(boundStatement);
		
		return "row added";
		
	}
}