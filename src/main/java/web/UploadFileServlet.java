package web;

import DAO.ChatStore;
import model.GeneralMessage;
import model.Message;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


@MultipartConfig
public class UploadFileServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private static final String imageFilePath = "src/main/webapp/Uploaded Files";
    //file format is: message_id, then a hyphen and file name.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        Part filePart = request.getPart("file");
        String fileName = getSubmittedFileName(filePart);
        ChatStore chatStore = getChatStoreDao(request);
        File uploads = new File(imageFilePath);
        Message m = new GeneralMessage(getCurrentAccount(request),"",true,getChatID(request));
        int messageID = chatStore.addMessage(m);
        String newFileName = messageID + "-" + fileName;

        File file = new File(uploads, newFileName);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath());
        }
        chatStore.updatePictureMessage(messageID,newFileName);
        response.sendRedirect("/chat?id=" + request.getSession().getAttribute("chat-pref"));
    }
    private static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName;
            }
        }
        return null;
    }
}
