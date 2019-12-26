package xero;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.xero.api.ApiClient;
import com.xero.api.client.IdentityApi;
import com.xero.models.identity.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Callback {

    static final String clientId = ;
    static final String clientSecret = ;

    static final String redirectURI = "http://localhost:9090/api/v1/xero/oauth-callback";
    static final String TOKEN_SERVER_URL = "https://identity.xero.com/connect/token";
    static final String AUTHORIZATION_SERVER_URL = "https://login.xero.com/identity/connect/authorize";
    static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public static void main(String[] args) throws IOException {

        // step 2) save code from query string param into below variable
        String code = ;

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
                new ClientParametersAuthentication(clientId, clientSecret), clientId, AUTHORIZATION_SERVER_URL).setScopes(scopeList).setDataStoreFactory(DATA_STORE_FACTORY).build();

        TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();

        HttpTransport httpTransport = new NetHttpTransport();
        GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(JSON_FACTORY).setClientSecrets(clientId, clientSecret).build();
        credential.setAccessToken(tokenResponse.getAccessToken());
        credential.setRefreshToken(tokenResponse.getRefreshToken());
        credential.setExpiresInSeconds(tokenResponse.getExpiresInSeconds());

        // Create requestFactory with credentials
        HttpTransport transport = new NetHttpTransport();
        HttpRequestFactory requestFactory = transport.createRequestFactory(credential);

        // Init IdentityApi client
        ApiClient defaultClient = new ApiClient("https://api.xero.com",null,null,null,requestFactory);
        IdentityApi idApi = new IdentityApi(defaultClient);
        String access_token = tokenResponse.getAccessToken();

        // step 3) "Wrong date format" exception occurs on line below:
        List<Connection> connection = idApi.getConnections(access_token);
    }
}
