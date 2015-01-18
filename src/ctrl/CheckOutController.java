package ctrl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import model.Cart;
import model.Order;
import model.bean.CartBean;
import model.bean.CustomerBean;
import model.bean.ItemBean;

/**
 * Servlet implementation class CheckOutController
 */
public class CheckOutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String orderFolder;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckOutController() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void init() throws ServletException {
		orderFolder = this.getServletContext().getRealPath(
				"/order");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("target", "/checkout.jspx");

		// initialization stuff
		// TODO remove checkout parameter, doesn't do anythnig
		String checkout = request.getParameter("checkout");
		Cart cm = (Cart) this.getServletContext().getAttribute("cart");
		CartBean cartBean = cm.cart;

		double subTotal = cartBean.getSubTotal();
		double tax = cm.getTax(cartBean);
		double shipping = cm.getShipping(cartBean);
		double grandTotal = cm.getGrandTotal(cartBean);

		Map<ItemBean, Integer> itemlist = cm.getCart();
		request.setAttribute("itemlist", itemlist);
		request.setAttribute("cart", cartBean);
		request.setAttribute("subTotal", subTotal);
		request.setAttribute("tax", tax);
		request.setAttribute("shipping", shipping);
		request.setAttribute("grandTotal", grandTotal);
		
		// check parameters
		if (checkout != null) {
			CustomerBean customerBean = (CustomerBean) request.getSession()
					.getAttribute("customer");
			if (customerBean == null) {
				// not logged in
				response.sendRedirect(request.getContextPath() + "/eFoods/login");
				return;
			} else {
				
				String account = customerBean.getAccount();
				int PO_number = getNewPONumber(account);
				String f = "order/" + account + "_" + PO_number + ".xml";
				String filename = this.getServletContext().getRealPath("/" + f);

				// FIXME need to stop catching exceptions
				try {
					export(PO_number, filename, cm, customerBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		request.getRequestDispatcher("/index.jspx").forward(request, response);
	}

	private int getNewPONumber(String account) {
		// TODO Auto-generated method stub
		File dir = new File(orderFolder);
		File[] dirFiles = dir.listFiles();
		int greatestPONumber = 1;
		Pattern p = Pattern.compile(account + "_(\\d)+.xml");
		for (File f : dirFiles) {
			Matcher m = p.matcher(f.getName());

			if (m.find()) {
				int i = Integer.parseInt(m.group(1));
				greatestPONumber = i > greatestPONumber ? i : greatestPONumber;
			}
		}

		return greatestPONumber + 1;
	}

	public void export(int ordernumber,
			String filename, Cart c, CustomerBean cb) throws Exception {

		String path = filename.substring(0, filename.lastIndexOf("/"));

		Order order = new Order(c, cb, ordernumber);
		JAXBContext jc = JAXBContext.newInstance(order.getClass());
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		SchemaFactory sf = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(new File(path + "/PO.xsd"));
		marshaller.setSchema(schema);

		StringWriter sw = new StringWriter();
		sw.write("<?xml version='1.0'?>\n");
		sw.write("<?xml-stylesheet type=\"text/xsl\" href=\"PO.xsl\"?>");
		sw.write("\n");
		marshaller.marshal(order, new StreamResult(sw));

		System.out.println(sw.toString()); // for debugging
		FileWriter fw = new FileWriter(filename);
		fw.write(sw.toString());
		fw.close();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}
