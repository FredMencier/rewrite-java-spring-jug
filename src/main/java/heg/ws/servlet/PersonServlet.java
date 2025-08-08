package heg.ws.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import heg.entity.Genre;
import heg.entity.Person;
import heg.exception.UnknownPersonException;
import heg.manager.PersonManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/personservlet")
public class PersonServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(PersonServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PersonManager personManager = new PersonManager();

        String email = req.getParameter("email");
        ObjectMapper mapper = new ObjectMapper();
        if (email != null) {
            Person person = null;
            try {
                person = personManager.findPersonByMail(email);
                mapper.writeValue(resp.getWriter(), person);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().close();
            } catch (UnknownPersonException e) {
                LOG.error("Person not found: " + email, e);
                mapper.writeValue(resp.getWriter(), Collections.emptyList());
                resp.getWriter().close();
            }
        } else {
            List<Person> persons = personManager.findAllPerson();
            mapper.writeValue(resp.getWriter(), persons);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        Person person = new Person(email, name, age, Genre.MASCULIN, null);
        PersonManager personManager = new PersonManager();
        personManager.insert(person);
    }
}

