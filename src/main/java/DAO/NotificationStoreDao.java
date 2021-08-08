package DAO;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import model.Location;
import model.Notification;
import model.RequestNotification;
import model.SaveleLocation;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationStoreDao extends DAO implements NotificationStore{
    private static final String getNotificationsFor = "SELECT (notification_id,notification_status,sender_mail,receiver_mail,location_name,sess,l.location_id,chat_id, requested_price) FROM (request_notification INNER JOIN locations l) WHERE receiver_mail = ?;";
    private static final String clearAllNotificationsFor = "DELETE FROM request_notification WHERE receiver_mail = ?;";
    private static final String deleteNotification = "DELETE FROM request_notification WHERE notification_id = ?;";
    private static final String addNotification = "INSERT INTO request_notification(notification_status,location_id,sender_mail,receiver_mail,requested_price) VALUES(?,?,?,?,?) ;";
    private static final String changeNotificationStatus = "UPDATE request_notification SET notification_status = ? WHERE notification_id = ?;";
    DataSource dataSource;

    public NotificationStoreDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Notification> getNotificationsFor(String mail) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(getNotificationsFor);
            ResultSet rs = st.executeQuery();
            return createNotifications(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return null;
    }

    private List<Notification> createNotifications(ResultSet rs) throws SQLException {
        List<Notification> notificationList = new ArrayList<>();
        while(rs.next()) {
            Location location = new SaveleLocation(rs.getString("location_name"),rs.getInt("sess"),rs.getInt("chat_id"));
            Notification notification = new RequestNotification(
                    rs.getInt("notification_status"),rs.getString("sender_mail"),rs.getString("receiver_mail"),location,rs.getDouble("requested_price"));
            notificationList.add(notification);
        }
        return notificationList;
    }
    @Override
    public void clearAllNotificationsFor(String mail) {
        Connection connection = null;
        try {
            PreparedStatement st = connection.prepareStatement(clearAllNotificationsFor);
            st.setString(1,mail);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void deleteNotification(int id) {
        Connection connection = null;
        try {
            PreparedStatement st = connection.prepareStatement(deleteNotification);
            st.setInt(1,id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
    private int getID(ResultSet rs) throws SQLException {
        if(rs.next()) {
            return rs.getInt("notification_id");
        }
        return -1;
    }
    @Override
    public int addNotification(Notification n) {
        Connection connection = null;
        try {
            LocationStore locationStore = new LocationStoreDao(dataSource);
            PreparedStatement st = connection.prepareStatement(addNotification, Statement.RETURN_GENERATED_KEYS);
            int id = locationStore.getLocationId(n.getRequestedLocation().getName(),n.getRequestedLocation().getSessionNumber());
            st.setInt(1,n.getStatus());
            st.setInt(2,id);
            st.setString(3,n.getSenderMail());
            st.setString(4,n.getReceiverMail());
            st.setDouble(5,n.getPrice());
            st.executeUpdate();
            return getID(st.getGeneratedKeys());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return -1;
    }

    @Override
    public void acceptNotification(int id) {
        changeNotification(Notification.ACCEPTED,id);
    }

    @Override
    public void rejectNotification(int id) {
        changeNotification(Notification.REJECTED,id);
    }

    private void changeNotification(int status, int id) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement st = connection.prepareStatement(changeNotificationStatus);
            st.setInt(1,status);
            st.setInt(2,id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
}
