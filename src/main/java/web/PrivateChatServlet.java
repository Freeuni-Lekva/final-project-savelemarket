package web;

import DAO.ChatStore;
import DAO.ChatStoreDao;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class PrivateChatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String otherMail = request.getParameter("profile-account"); // might be Attribute
        String myMail = (String) request.getSession().getAttribute("current-account");
        ChatStore chatStore = (ChatStoreDao) request.getServletContext().getAttribute("chat-store");
        int id = chatStore.getPrivateChatID(myMail,otherMail);
        if(id == ChatStoreDao.ID_DOESNT_EXIST){
            id = chatStore.createPrivateChat(myMail,otherMail);
        }
        if( id == ChatStoreDao.MORE_THAN_ONE_PRIVATE){
            System.out.println("-------------------------------- MORE THAN ONE PRIVATE --------------------------------");
            try {
                throw new Exception("-------------------------------- MORE THAN ONE PRIVATE --------------------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("private-chat-id",id);
        request.getRequestDispatcher("/pchat.jsp").forward(request,response); // არ ვიცი შეიძლება ეს გადავაკეთო და doGet-ში გავუშვა
        // აჯაქსს გააჩნია
    }
}
