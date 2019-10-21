package prax2;

import prax2.orderdao.OrderDao;
import prax2.orderpojo.Order;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/orders")
public class Task1Servelet extends HttpServlet {

    private static final String APP_JSON = "application/json";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getContentType() != null  && request.getContentType().equals("application/json")) {
            response.setContentType(APP_JSON);
            String strJson = request.getReader().lines().collect(Collectors.joining("\n"));
            Order order = JsonParser.createOrder(strJson);
            response.getWriter().println(JsonParser.createJson(OrderDao.getDAO().saveOrderAndReturnOrder(order)));
        } else {
            response.getWriter().println(request.getContextPath());
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameterMap().isEmpty()) {
            response.setContentType(APP_JSON);
            List<Order> orders = OrderDao.getDAO().getAllOrders();
            String str = JsonParser.createJson(orders);
            response.getWriter().print(str);
        } else {
            String stringID = request.getParameter("id");
            if (stringID.matches("\\d+")) {
                Order order = OrderDao.getDAO().getOrder(Long.parseLong(stringID));
                if (order == null)  {
                    response.getWriter().println("Order not exist");
                }
                else {
                    response.setContentType(APP_JSON);
                    response.getWriter().println(JsonParser.createJson(order));
                }
            } else {
                response.getWriter().println("Id not specified");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringID = request.getParameter("id");
        if (stringID.matches("\\d+")) {
            OrderDao.getDAO().deleteOrder(Long.parseLong(stringID));
        } else {
            response.getWriter().println("Id not specified");
        }
    }
}
