package pl2.g7.iamsi.stuffngo.Listeners;

public interface LoginListener {
    void onValidateLogin(final String token, final String username);
}