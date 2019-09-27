package prax2;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("orders/form")
public class Task31Servelet extends HttpServlet {

    private Database activeDatabase;

    @Override
    public void init() throws ServletException {
        super.init();
        activeDatabase = Database.init();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String form = request.getReader().lines().collect(Collectors.joining("\n"));
        int id = activeDatabase.createNewId();
        String jsonString = formToJsonString(id, form);
        activeDatabase.saveStringJson(id, jsonString);
        response.setContentType("text/plain");
        response.getWriter().print(id);
    }

    private String formToJsonString(int id, String form) {
        String[] pairs = form.split("\n");
        String jsonString = "{\"id\":"+ id;
        for (String pair: pairs) {
            String[] pairMas = pair.split("=");
            jsonString += ", \"" + pairMas[0].trim() +"\": \"" + pairMas[1] +"\"";
        }
        jsonString += "}";
        return jsonString;
    }
}
