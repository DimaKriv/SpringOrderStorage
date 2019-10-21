package prax2;


import prax2.orderdao.OrderDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("orders/form")
public class Task31Servelet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String form = request.getReader().lines().collect(Collectors.joining("\n"));
        //int id = activeDatabase.createNewId();
        String jsonString = formToJsonString(form);
      //  int id = activeDatabase.saveStringJson(jsonString);
        response.setContentType("text/plain");
        response.getWriter().print(OrderDao.getDAO().saveOrderReturningId(JsonParser.createOrder(jsonString)));
    }

    private String formToJsonString( String form) {
        String[] pairs = form.split("\n");
        String jsonString = "{";
        for (int i = 0; i < pairs.length; i++) {
            String[] pairMas = pairs[i].split("=");
            if (i > 0) { jsonString += ","; }
            jsonString += "\"" + pairMas[0].trim() +"\": \"" + pairMas[1] +"\"";
        }
        jsonString += "}";
        return jsonString;
    }

}
