package edu.usc.csci572.teamten.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.usc.csci572.teamten.service.SolrService;

/**
 * Servlet implementation class FetchChart
 */
@WebServlet("/FetchChart")
public class FetchChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchChart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String chart = request.getParameter("chart");
		SolrService service = new SolrService();
		
		switch(chart) {
		case "monthVsManufacturer":
			service.getCitySales();
			sendJsonResponse(response, getContent("/Users/karanjeetsingh/git_workspace/mango/webapp/resources/sample_data/stackedAreaChart.json"));
			//sendJsonResponse(response, service.getTimeBasedTrendMonthManufacturer());
			break;
		case "cityVsSales":
			sendJsonResponse(response, service.getCitySales());
			break;
			
		default: return;		
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private String getContent(String filepath) throws IOException {
		File file = new File(filepath);
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();

		String content = new String(data, "UTF-8");
		return content;
	}
	
	private void sendJsonResponse(HttpServletResponse response, String data) throws IOException {
		//response.setContentType("text/plain");
		//response.setCharacterEncoding("UTF-8");
		response.getWriter().write(data);
	}
}
