package nl.knaw.dans.test;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadAllServlet extends HttpServlet {
  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.getWriter().write("ReadAll Servlet OK!");
    resp.setStatus(200);
  }

  @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    FileOutputStream fos = new FileOutputStream("target/outfile");
    try {
      IOUtils.copy(req.getInputStream(), fos);
    }
    finally {
      fos.close();
    }

    resp.setStatus(201);
  }
}
