package com.alontests;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/*
 MultipartConfig allows sending multipart/form-data data to server. The data parts are temporarily saved in files
 at the specified 'location' path by calling req.getParts(). Each part can be read and written to same location by
 calling part.write() or can be read and manipulated (or saved) by calling part.getInputStream().
*/
@WebServlet(name = "Multi-Part Upload",
        urlPatterns = {"/upload"},
        loadOnStartup = 1)
@MultipartConfig(location = "C:/Projects/my-projects/multipart-upload/tmp" /* path must exist in filesystem */,
        maxFileSize= 1024 * 1024 * 5, /* max size of a file (in bytes), default is unlimited */
        maxRequestSize = 1024 * 1024 * 5 * 2, /* max overall size of request (in bytes), default is unlimited */
        fileSizeThreshold = 1 /* file size below will not be saved on disk (in bytes), but in memory, default is 0*/)
public class MultiPartUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        try (PrintWriter writer = resp.getWriter()) {
            Collection<Part> parts = req.getParts();
            for (Part part : parts) {
                writer.printf("Writing part: name=%s, size=%d",part.getName(), part.getSize());
                writer.printf("\nHeaders: %s", getHeadersStr(part));
                part.write(part.getName());
            }
        }
        super.doPost(req, resp);
    }

    private String getHeadersStr(Part part) {
        StringBuilder sb = new StringBuilder();
        for (String headerName : part.getHeaderNames()) {
            sb.append(headerName)
                    .append("=")
                    .append(part.getHeaders(headerName))
                    .append(", ");
        }
        return sb.toString();
    }
}