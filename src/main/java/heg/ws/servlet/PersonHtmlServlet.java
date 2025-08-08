package heg.ws.servlet;

import heg.entity.Person;
import heg.manager.PersonManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/personhtml")
public class PersonHtmlServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PersonManager personManager = new PersonManager();
        List<Person> persons = personManager.findAllPerson();
        PrintWriter writer = resp.getWriter();
        writer.println("<html>");
        writer.println("<head><title>Person List</title></head>");
        writer.println("<body>");
        writer.println("<h1>Person List</h1>");

        persons.forEach(person -> {
            writer.println("name: " + person.getName() + "<br>");
        });

        writer.println("</body>");
        resp.setStatus(HttpServletResponse.SC_OK);
        writer.close();
    }
}

