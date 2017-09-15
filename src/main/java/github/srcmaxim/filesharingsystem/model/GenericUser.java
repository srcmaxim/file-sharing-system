package github.srcmaxim.filesharingsystem.model;

public interface GenericUser {

    String getLogin();
    void setLogin(String login);
    String getPassword();
    void setPassword(String password);
    String getFirstName();
    void setFirstName(String firstName);
    String getLastName();
    void setLastName(String lastName);
    String getEmail();
    void setEmail(String email);
    String getPhone();
    void setPhone(String phone);

}
