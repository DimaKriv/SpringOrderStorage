package prax2;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/api/orders")
public class Task1Servelet extends HttpServlet {

    private static final String APP_JSON = "application/json";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getContentType() != null  && request.getContentType().equals("application/json")) {
            response.setContentType(APP_JSON);
            //response.getWriter().print(
                    //addIdToJsonString(request.getReader().lines()
                    //.collect(Collectors.joining("\n"))));
            String jsonString = request.getReader().lines().collect(Collectors.joining("\n"));
            if (jsonString.contains("\"id\":")) {
                jsonString = JsonParser.deleteOrderId(jsonString);
            }
            response.getWriter().println(OrderDao.getDAO().saveOrderAndReturnOrder(jsonString));
        } else {
            response.getWriter().println(request.getContextPath());
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameterMap().isEmpty()) {
            response.setContentType(APP_JSON);
            //System.out.println("arr");
            response.getWriter().print(OrderDao.getDAO().getAllOrders());
        } else {
            String stringID = request.getParameter("id");
            if (stringID.matches("\\d+")) {
                // String output = activeDatabase.getJsonString(Integer.parseInt(stringID));
                response.setContentType(APP_JSON);
                response.getWriter().println(OrderDao.getDAO().getOrder(Long.parseLong(stringID)));
                //  if (output != null) {
                //     response.getWriter().println(output);
                // }
            } else {
                response.getWriter().println("Id not specified");
            }
        }
    }

}
