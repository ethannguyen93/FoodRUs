package ctrl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import model.bean.CustomerBean;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Servlet implementation class LoginController
 */
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private URL authServerURL;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginController() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			authServerURL = new URL(this.getServletContext()
					.getInitParameter("authServerURL"));
		} catch (MalformedURLException e) {
			throw new ServletException("AUTH server URL not properly configured", e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response); // if using HTTPS, entire request is still secure
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("target", "login.jspx");
		
		String username = request.getParameter("username"); // TODO right name?
		String password = request.getParameter("password");
		// FIXME not working, go to home page instead
		String redirectURL = request.getParameter("redirectURL");
		if (redirectURL == null){
			request.getHeader("referer");
		}
		request.setAttribute("redirectURL", redirectURL);
		if (password != null){
			if (!request.isSecure()){
				// this is very bad, the user could have just lost their password to a man-in-the-middle
				// use a string builder just because it's faster 
				// than builtin string concatenation
				String error = new StringBuilder("This request was not securely transmitted (over HTTPS). ")
						.append("This is very bad. ")
						.append("You may have lost your password to bad people. ")
						.append("Please reset your password, especially if you logged in with your CSE account.")
						.toString();
				request.setAttribute("loginError", error);
				request.getRequestDispatcher("index.jspx")
					.forward(request, response);
				return;
			} else if (username != null){

				CustomerBean cb = checkLogin(username, password);
				if (cb != null){ // successful login
					request.getSession().setAttribute("customer", cb);
					request.setAttribute("success", true);
					request.setAttribute("redirectURL", redirectURL);
					request.getRequestDispatcher("/index.jspx")
						.forward(request, response);
					return;
				} else { // failed login
					// redirect back to login page and give error
					request.setAttribute("loginError", "Wrong username and password. Please try again.");
					request.getRequestDispatcher("/index.jspx")
						.forward(request, response);
					return;
				}
			}
		} else {
			// FIXME change default jsp
			request.getRequestDispatcher("/index.jspx")
					.forward(request, response);
			return;
		}
	}

	private CustomerBean checkLogin(String username, String password)
			throws ServletException {
		HttpsURLConnection connection = null;

		try {
			//Create connection

			String usernamePasswordBase64 = Base64.encode((username+":"+password).getBytes());

			
			connection = (HttpsURLConnection) authServerURL.openConnection();
			connection.setRequestMethod("GET");  
			connection.setRequestProperty("Authorization", 
					"Basic " + usernamePasswordBase64);

			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			//Send request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream ());

			String urlParameters = "";
			wr.writeBytes (urlParameters);
			wr.flush ();
			wr.close ();


			//Get Response    
			int responseCode =connection.getResponseCode();
			if (responseCode == 401) { 
				return null;
			} else if (responseCode == 200) {
				
				// unmarshall the output
				InputStream is = connection.getInputStream();
				CustomerBean cb = new CustomerBean();
			    JAXBContext jc = JAXBContext.newInstance(cb.getClass());
			    Unmarshaller u = jc.createUnmarshaller();
			    cb = (CustomerBean) u.unmarshal(is);
				
				return cb;
			} else {
				throw new ServletException("AUTH server gave an unexpected response code:" + responseCode);
			}

		} catch (Exception e) {
			throw new ServletException(e);
		} finally {

			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}

}
