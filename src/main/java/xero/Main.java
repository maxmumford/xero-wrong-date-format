package xero;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    static final String clientId = ;
    static final String clientSecret = ;

    static final String redirectURI = "http://localhost:9090/api/v1/xero/oauth-callback";
    static final String TOKEN_SERVER_URL = "https://identity.xero.com/connect/token";
    static final String AUTHORIZATION_SERVER_URL = "https://login.xero.com/identity/connect/authorize";
    static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    static final String secretState = "secret" + new Random().nextInt(999999);

    public static void main(String[] args) throws IOException {
        ArrayList<String> scopeList = new ArrayList<String>();
        scopeList.add("openid");
        scopeList.add("email");
        scopeList.add("profile");
        scopeList.add("offline_access");
        scopeList.add("accounting.settings");
        scopeList.add("accounting.transactions");
        scopeList.add("accounting.contacts");
        scopeList.add("accounting.journals.read");
        scopeList.add("accounting.reports.read");
        scopeList.add("accounting.attachments");

        DataStoreFactory DATA_STORE_FACTORY = new MemoryDataStoreFactory();
        AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new GenericUrl(TOKEN_SERVER_URL),
                new ClientParametersAuthentication(clientId, clientSecret), clientId, AUTHORIZATION_SERVER_URL)
                .setScopes(scopeList)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .build();

        String url = flow.newAuthorizationUrl()
                .setClientId(clientId)
                .setScopes(scopeList)
                .setState(secretState)
                .setRedirectUri(redirectURI).build();

        // step 1) visit url in above variable, copy "code" from query string param and save it in Callback file in this proj
    }
}
