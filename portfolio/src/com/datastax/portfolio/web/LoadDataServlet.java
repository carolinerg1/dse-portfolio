package com.datastax.portfolio.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.datastax.portfolio.util.*;
import com.datastax.portfolio.model.*;

/**
 * Servlet implementation class LoadDataServlet
 */
@WebServlet("/LoadDataServlet")
public class LoadDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Database db;
	   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadDataServlet() {
        super();
        // TODO Auto-generated constructor stub
        
        String nodes[] = {"54.86.69.115"};
		db = new Database(nodes, "portfolio");  // nodes[], keyspace
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		final String ticker = "MSFT";
		final String fileName = "/Users/caro/data/Datastax/demo/portfolio/data/" + ticker + "HistoricalQuotes.csv";
		
		System.out.println("reading file for ticker" + ticker);
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
			
			String inputLine = null;
			Stock stock = new Stock();
			stock.setTicker(ticker);
			while(((inputLine = reader.readLine()) != null )) {
				// Split the input line.
				if(inputLine.contains(",")) {
					String[] arrLine = inputLine.split(",");
					
					stock.setDate(arrLine[0].trim());
					stock.setClose(Float.parseFloat(arrLine[1].trim()));
					stock.setVolume(Double.parseDouble(arrLine[2].trim()));
					stock.setOpen(Float.parseFloat(arrLine[3].trim()));
					stock.setHigh(Float.parseFloat(arrLine[4].trim()));
					stock.setLow(Float.parseFloat(arrLine[5].trim()));
							
					System.out.println("Stock close: " + stock.getClose() + " volume: " + stock.getVolume());
					db.insertHistoricalPrices(stock);
				}
			}
		
		
		
		response.setContentType("application/json");
        JSONObject json = new JSONObject();
        json.put("data", "loaded successfully");
     
        response.getWriter().write(json.toString());
	}

}
