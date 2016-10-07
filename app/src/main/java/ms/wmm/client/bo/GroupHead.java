package ms.wmm.client.bo;

import java.util.List;

/**
 * Created by Marcin on 19.08.2016.
 */
public class GroupHead {

    private String name;
    private String adminName;
    private List<String> users;
    private boolean open;
    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsersString(){
        String usersString="";
        for(String user:users){
            usersString+=(user+", ");
        }
       return usersString.isEmpty()?"":usersString.substring(0,usersString.length()-2);
    }
}
