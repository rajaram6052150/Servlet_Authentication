package model;

public class ForgetPassword {
    String email;
    private String resetToken;
    String expirationTime;

    public ForgetPassword(String email, String resetToken) {
        this.email = email;
        this.resetToken = resetToken;
    }

    public String getResetToken(){
        return resetToken;
    }
    public String getEmail(){
        return email;
    }
}
