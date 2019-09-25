package prax2;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/api/parser")
public class Task2Servlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String input = request.getReader().lines().collect(Collectors.joining("\n"));
        response.setContentType("application/json");
        response.getWriter().println(reverseJson(input));
    }

    private static String reverseJson(String input) {
        int index = input.indexOf("\"");
        while (index > 0) {
            int end = JsonParser.skipNameIndex(index, input) + 1;
            input = input.substring(0, index) + new StringBuffer(input.substring(index, end)).reverse()
                    + input.substring(end);
            //System.out.println(input);
            index = input.indexOf("\"", end);
        }
        return input;
    }
}