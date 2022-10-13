package reqres.models;

public class LoginResponsePojoModel {

    private String id,
            token;

    public final String getId() {
        return id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    public final String getToken() {
        return token;
    }

    public final void setToken(final String token) {
        this.token = token;
    }
}
