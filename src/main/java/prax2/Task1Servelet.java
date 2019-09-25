package prax2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/api/orders")
public class Task1Servelet extends HttpServlet {

    private static int id = 0;
    @Override
    public void init() throws ServletException {
        super.init();
        id = 0;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getContentType() != null  && request.getContentType().equals("application/json")) {
            response.setContentType("application/json");
            response.getWriter().print(addIdToJsonString(request.getReader().lines()
                    .collect(Collectors.joining("\n"))));
        } else {
            response.getWriter().println(request.getContextPath());
        }
    }

    private String addIdToJsonString(String jsonString) {
        if (jsonString.contains("\"id\":")) {
            jsonString = JsonParser.deleteOrderId(jsonString);
        }
        return "{\"id\":" + id++ + ", " + jsonString.trim().substring(1);
    }

/*    public static void main(String[] args) {
        System.out.println(skipNameIndex(0, "\"g\\\"ard\""));
        System.out.println();
        System.out.println("{\"id\": {\"der\":1, \"i\":[]}}".length());
        System.out.println(skipChildIndex(0, "{\"id\": {\"der\":1, \"i\":[]}}"));
        System.out.println(deleteOrderId("{\"rot\": {\"der\":1, \"i\":[]}, \"id\":" +
                " {\"der\":1, \"i\":[]}, \"dir\": {\"der\":1, \"i\":[]}}"));
    }*/
}
