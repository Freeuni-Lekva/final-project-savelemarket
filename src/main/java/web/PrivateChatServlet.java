package web;

import DAO.ChatStore;
import DAO.ChatStoreDao;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class PrivateChatServlet extends GeneralServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        redirectIfNotLogged(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        redirectIfNotLogged(request,response);
        String otherMail = request.getParameter("profile-account"); // might be Attribute
        String myMail = getCurrentAccount(request).getMail();
        ChatStore chatStore = getChatStoreDao(request);
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
