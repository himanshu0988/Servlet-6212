package in.ineuron.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/test" }, 
	initParams= {
			@WebInitParam(name = "url", value = "jdbc:mysql:///detail"),
			@WebInitParam(name = "user", value = "root"),
			@WebInitParam(name = "password", value = "9852451029")
	},loadOnStartup = 10)
		

public class TestApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private PreparedStatement pstmt = null;
	
	static
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("driver load sucessfully");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void init() throws ServletException 
	{
		String url=getInitParameter("url");
		String user=getInitParameter("user");
		String password=getInitParameter("password");
		
		try {
			connection=DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
			System.out.println("Connection established succesfully...");
			e.printStackTrace();
		}
	
	}

	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		
		String userid = request.getParameter("userid");
		String username = request.getParameter("username");
		String userage = request.getParameter("userage");
		
		String sqlInsertQuery = "insert into studnet(sid,sname,sage) values(?,?,?)";
		try {
			if (connection != null)
				pstmt = connection.prepareStatement(sqlInsertQuery);
			if (pstmt != null) {
				
				pstmt.setInt(1, Integer.parseInt(userid));
				pstmt.setString(2, username);
				pstmt.setInt(3, Integer.parseInt(userage));
			}
			if (pstmt != null) {
				int rowAffected = pstmt.executeUpdate();
				PrintWriter out = null;
				out = response.getWriter();
				if (rowAffected == 1) {
					out.println("<h1 style='color:green;text-align:center;'>REGISTRATION SUCCESFULL</h1>");
				} else {
					out.println(
							"<h1 style='color:red; text-align;center;'>REGISTRATION FAILED TRY AGAIN WITH THE FOLLOWING LINK</h1>");
					out.println("<a href='./reg.html'/>|REGISTRATION|</a>");
				}
				out.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	}


