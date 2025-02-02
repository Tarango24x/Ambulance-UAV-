package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.services.GoogleKeyInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.ClientLoginHelper;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.IClientLoginHelper;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.OAuth2Helper;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.shaded.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.shaded.apache.http.auth.AUTH;
import org.shaded.apache.http.client.utils.URLEncodedUtils;
import org.shaded.apache.http.protocol.HTTP;

@DesignerComponent(category = ComponentCategory.INTERNAL, description = "<p>A non-visible component that communicates with Google Fusion Tables. Fusion Tables let you store, share, query and visualize data tables; this component lets you query, create, and modify these tables.</p> <p><font color=red><b>NOTE:</b>&nbsp;Google shutdown the Fusion Tables service on December 3, 2019. This component no longer functions.</font></p> <p>This component uses the <a href=\"https://developers.google.com/fusiontables/docs/v2/getting_started\" target=\"_blank\">Fusion Tables API V2.0</a>. <p>Applications using Fusion Tables must authentication to Google's servers. There are two ways this can be done. The first way uses an API Key which you the developer obtain (see below). With this approach end-users must also login to access a Fusion Table. The second approach is to use a Service Account. With this approach you create credentials and a special \"Service Account Email Address\" which you obtain from the <a href=\"https://code.google.com/apis/console/\" target=\"_blank\">Google APIs Console</a>. You then tell the Fusion Table Control the name of the Service Account Email address and upload the secret key as an asset to your application and set the KeyFile property to point at this file. Finally you check the \"UseServiceAuthentication\" checkbox in the designer. When using a Service Account, end-users do not need to login to use Fusion Tables, your service account authenticates all access.</p> <p>To get an API key, follow these instructions.</p> <ol><li>Go to your <a href=\"https://code.google.com/apis/console/\" target=\"_blank\">Google APIs Console</a> and login if necessary.</li><li>Select the <i>Services</i> item from the menu on the left.</li><li>Choose the <i>Fusiontables</i> service from the list provided and turn it on.</li><li>Go back to the main menu and select the <i>API Access</i> item. </li></ol><p>Your API Key will be near the bottom of that pane in the section called \"Simple API Access\".You will have to provide that key as the value for the <i>ApiKey</i> property in your Fusiontables app.</p><p>Once you have an API key, set the value of the <i>Query</i> property to a valid Fusiontables SQL query and call <i>SendQuery</i> to execute the query.  App Inventor will send the query to the Fusion Tables server and the <i>GotResult</i> block will fire when a result is returned from the server.Query results will be returned in CSV format, and can be converted to list format using the \"list from csv table\" or \"list from csv row\" blocks.</p><p>Note that you do not need to worry about UTF-encoding the query. But you do need to make sure the query follows the syntax described in <a href=\"https://developers.google.com/fusiontables/docs/v2/getting_started\" target=\"_blank\">the reference manual</a>, which means that things like capitalization for names of columns matters, and that single quotes must be used around column names if there are spaces in them.</p>", iconName = "images/fusiontables.png", nonVisible = true, version = 4)
@UsesLibraries(libraries = "fusiontables.jar,google-api-client-beta.jar,google-api-client-android2-beta.jar,google-http-client-beta.jar,google-http-client-android2-beta.jar,google-http-client-android3-beta.jar,google-oauth-client-beta.jar,guava-14.0.1.jar,gson-2.1.jar")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET,android.permission.ACCOUNT_MANAGER,android.permission.MANAGE_ACCOUNTS,android.permission.GET_ACCOUNTS,android.permission.USE_CREDENTIALS,android.permission.WRITE_EXTERNAL_STORAGE,android.permission.READ_EXTERNAL_STORAGE")
public class FusiontablesControl extends AndroidNonvisibleComponent implements Component {
    public static final String APP_NAME = "App Inventor";
    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
    public static final String AUTH_TOKEN_TYPE_FUSIONTABLES = "oauth2:https://www.googleapis.com/auth/fusiontables";
    private static final String DEFAULT_QUERY = "show tables";
    private static final String DIALOG_TEXT = "Choose an account to access FusionTables";
    public static final String FUSIONTABLES_POST = "https://www.googleapis.com/fusiontables/v2/tables";
    private static final String FUSIONTABLE_SERVICE = "fusiontables";
    private static final String FUSION_QUERY_URL = "http://www.google.com/fusiontables/v2/query";
    private static final String LOG_TAG = "FUSION";
    private static final int SERVER_TIMEOUT_MS = 30000;
    /* access modifiers changed from: private */
    public final Activity activity;
    private String apiKey;
    /* access modifiers changed from: private */
    public String authTokenType = AUTH_TOKEN_TYPE_FUSIONTABLES;
    /* access modifiers changed from: private */
    public File cachedServiceCredentials = null;
    /* access modifiers changed from: private */
    public final ComponentContainer container;
    /* access modifiers changed from: private */
    public String errorMessage;
    /* access modifiers changed from: private */
    public boolean isServiceAuth = false;
    /* access modifiers changed from: private */
    public String keyPath = "";
    private String loadingDialogMessage = "Please wait loading...";
    private String query;
    /* access modifiers changed from: private */
    public String queryResultStr;
    /* access modifiers changed from: private */
    public final IClientLoginHelper requestHelper;
    /* access modifiers changed from: private */
    public String scope = "https://www.googleapis.com/auth/fusiontables";
    /* access modifiers changed from: private */
    public String serviceAccountEmail = "";
    private boolean showLoadingDialog = true;
    /* access modifiers changed from: private */
    public String standardErrorMessage = "Error on Fusion Tables query";

    public FusiontablesControl(ComponentContainer componentContainer) {
        super(componentContainer.$form());
        this.container = componentContainer;
        this.activity = componentContainer.$context();
        this.requestHelper = createClientLoginHelper(DIALOG_TEXT, FUSIONTABLE_SERVICE);
        this.query = DEFAULT_QUERY;
        if (SdkLevel.getLevel() < 5) {
            showNoticeAndDie("Sorry. The Fusiontables component is not compatible with this phone.", "This application must exit.", "Rats!");
        }
    }

    private void showNoticeAndDie(String message, String title, String buttonText) {
        AlertDialog alertDialog = new AlertDialog.Builder(this.activity).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setButton(buttonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FusiontablesControl.this.activity.finish();
            }
        });
        alertDialog.show();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Indicates whether a service account should be used for authentication")
    public boolean UseServiceAuthentication() {
        return this.isServiceAuth;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void UseServiceAuthentication(boolean bool) {
        this.isServiceAuth = bool;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The Service Account Email Address when service account authentication is in use.")
    public String ServiceAccountEmail() {
        return this.serviceAccountEmail;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void ServiceAccountEmail(String email) {
        this.serviceAccountEmail = email;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void ApiKey(String apiKey2) {
        this.apiKey = apiKey2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Your Google API Key. For help, click on the questionmark (?) next to the FusiontablesControl component in the Palette. ")
    public String ApiKey() {
        return this.apiKey;
    }

    @DesignerProperty(defaultValue = "show tables", editorType = "string")
    @SimpleProperty
    public void Query(String query2) {
        this.query = query2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The query to send to the Fusion Tables API. <p>For legal query formats and examples, see the <a href=\"https://developers.google.com/fusiontables/docs/v2/getting_started\" target=\"_blank\">Fusion Tables API v2.0 reference manual</a>.</p> <p>Note that you do not need to worry about UTF-encoding the query. But you do need to make sure it follows the syntax described in the reference manual, which means that things like capitalization for names of columns matters, and that single quotes need to be used around column names if there are spaces in them.</p> ")
    public String Query() {
        return this.query;
    }

    @DesignerProperty(defaultValue = "", editorType = "asset")
    @SimpleProperty
    public void KeyFile(String path) {
        if (!path.equals(this.keyPath)) {
            if (this.cachedServiceCredentials != null) {
                this.cachedServiceCredentials.delete();
                this.cachedServiceCredentials = null;
            }
            if (path == null) {
                path = "";
            }
            this.keyPath = path;
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Specifies the path of the private key file.  This key file is used to get access to the FusionTables API.")
    public String KeyFile() {
        return this.keyPath;
    }

    @SimpleFunction(description = "Send the query to the Fusiontables server.")
    public void SendQuery() {
        new QueryProcessorV2(this.activity).execute(new String[]{this.query});
    }

    @Deprecated
    @SimpleFunction(description = "DEPRECATED. This block is deprecated as of the end of 2012.  Use SendQuery.")
    public void DoQuery() {
        if (this.requestHelper != null) {
            new QueryProcessor().execute(new String[]{this.query});
            return;
        }
        this.form.dispatchErrorOccurredEvent(this, "DoQuery", 3, new Object[0]);
    }

    @SimpleEvent(description = "Indicates that the Fusion Tables query has finished processing, with a result.  The result of the query will generally be returned in CSV format, and can be converted to list format using the \"list from csv table\" or \"list from csv row\" blocks.")
    public void GotResult(String result) {
        EventDispatcher.dispatchEvent(this, "GotResult", result);
    }

    @SimpleFunction(description = "Forget end-users login credentials. Has no effect on service authentication")
    public void ForgetLogin() {
        OAuth2Helper.resetAccountCredential(this.activity);
    }

    @SimpleFunction(description = "Inserts a row into the specified fusion table. The tableId field is the id of thefusion table. The columns is a comma-separated list of the columns to insert values into. The values field specifies what values to insert into each column.")
    public void InsertRow(String tableId, String columns, String values) {
        this.query = "INSERT INTO " + tableId + " (" + columns + ") VALUES (" + values + ")";
        new QueryProcessorV2(this.activity).execute(new String[]{this.query});
    }

    @SimpleFunction(description = "Gets all the rows from a specified fusion table. The tableId field is the id of therequired fusion table. The columns field is a comma-separeted list of the columns to retrieve.")
    public void GetRows(String tableId, String columns) {
        this.query = "SELECT " + columns + " FROM " + tableId;
        new QueryProcessorV2(this.activity).execute(new String[]{this.query});
    }

    @SimpleFunction(description = "Gets all the rows from a fusion table that meet certain conditions. The tableId field isthe id of the required fusion table. The columns field is a comma-separeted list of the columns toretrieve. The conditions field specifies what rows to retrieve from the table, for example the rows in whicha particular column value is not null.")
    public void GetRowsWithConditions(String tableId, String columns, String conditions) {
        this.query = "SELECT " + columns + " FROM " + tableId + " WHERE " + conditions;
        new QueryProcessorV2(this.activity).execute(new String[]{this.query});
    }

    @DesignerProperty(defaultValue = "Please wait loading...", editorType = "string")
    @SimpleProperty
    public void LoadingDialogMessage(String loadingDialogMessage2) {
        this.loadingDialogMessage = loadingDialogMessage2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Set the loading message for the dialog.")
    public String LoadingDialogMessage() {
        return this.loadingDialogMessage;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void ShowLoadingDialog(boolean showLoadingDialog2) {
        this.showLoadingDialog = showLoadingDialog2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether or not to show the loading dialog")
    public boolean ShowLoadingDialog() {
        return this.showLoadingDialog;
    }

    private IClientLoginHelper createClientLoginHelper(String accountPrompt, String service) {
        if (SdkLevel.getLevel() < 5) {
            return null;
        }
        HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), SERVER_TIMEOUT_MS);
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), SERVER_TIMEOUT_MS);
        return new ClientLoginHelper(this.activity, service, accountPrompt, httpClient);
    }

    /* access modifiers changed from: private */
    public HttpUriRequest genFusiontablesQuery(String query2) throws IOException {
        HttpPost request = new HttpPost(FUSION_QUERY_URL);
        ArrayList<BasicNameValuePair> pair = new ArrayList<>(1);
        pair.add(new BasicNameValuePair("sql", query2));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pair, HTTP.UTF_8);
        entity.setContentType(URLEncodedUtils.CONTENT_TYPE);
        request.setEntity(entity);
        return request;
    }

    private class QueryProcessor extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;

        private QueryProcessor() {
            this.progress = null;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progress = ProgressDialog.show(FusiontablesControl.this.activity, "Fusiontables", "processing query...", true);
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... params) {
            try {
                HttpUriRequest request = FusiontablesControl.this.genFusiontablesQuery(params[0]);
                Log.d(FusiontablesControl.LOG_TAG, "Fetching: " + params[0]);
                HttpResponse response = FusiontablesControl.this.requestHelper.execute(request);
                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                response.getEntity().writeTo(outstream);
                Log.d(FusiontablesControl.LOG_TAG, "Response: " + response.getStatusLine().toString());
                return outstream.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            this.progress.dismiss();
            FusiontablesControl.this.GotResult(result);
        }
    }

    public com.google.api.client.http.HttpResponse sendQuery(String query2, String authToken) {
        this.errorMessage = this.standardErrorMessage;
        Log.i(LOG_TAG, "executing " + query2);
        try {
            Fusiontables.Query.Sql sql = new Fusiontables.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), new GoogleCredential()).setApplicationName("App Inventor Fusiontables/v2.0").setJsonHttpRequestInitializer(new GoogleKeyInitializer(ApiKey())).build().query().sql(query2);
            sql.put("alt", "csv");
            sql.setOauthToken(authToken);
            return sql.executeUnparsed();
        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            this.errorMessage = e.getMessage();
            Log.e(LOG_TAG, "JsonResponseException");
            Log.e(LOG_TAG, "e.getMessage() is " + e.getMessage());
            Log.e(LOG_TAG, "response is " + null);
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            this.errorMessage = e2.getMessage();
            Log.e(LOG_TAG, "IOException");
            Log.e(LOG_TAG, "e.getMessage() is " + e2.getMessage());
            Log.e(LOG_TAG, "response is " + null);
            return null;
        }
    }

    public static String httpResponseToString(com.google.api.client.http.HttpResponse response) {
        if (response == null) {
            return "";
        }
        if (response.getStatusCode() != 200) {
            return response.getStatusCode() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + response.getStatusMessage();
        }
        try {
            return parseResponse(response.getContent());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String httpApacheResponseToString(HttpResponse response) {
        if (response == null) {
            return "";
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            return response.getStatusLine().getStatusCode() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + response.getStatusLine().getReasonPhrase();
        }
        try {
            return parseResponse(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String parseResponse(InputStream input) {
        String resultStr = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line != null) {
                    sb.append(line + "\n");
                } else {
                    resultStr = sb.toString();
                    Log.i(LOG_TAG, "resultStr = " + resultStr);
                    br.close();
                    return resultStr;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return resultStr;
        }
    }

    public void handleOAuthError(String msg) {
        Log.i(LOG_TAG, "handleOAuthError: " + msg);
        this.errorMessage = msg;
    }

    /* access modifiers changed from: private */
    public String parseSqlCreateQueryToJson(String query2) {
        Log.i(LOG_TAG, "parsetoJSonSqlCreate :" + query2);
        StringBuilder jsonContent = new StringBuilder();
        String query3 = query2.trim();
        String tableName = query3.substring("create table".length(), query3.indexOf(40)).trim();
        String[] columnSpecs = query3.substring(query3.indexOf(40) + 1, query3.indexOf(41)).split(",");
        jsonContent.append("{'columns':[");
        for (int k = 0; k < columnSpecs.length; k++) {
            String[] nameTypePair = columnSpecs[k].split(":");
            jsonContent.append("{'name': '" + nameTypePair[0].trim() + "', 'type': '" + nameTypePair[1].trim() + "'}");
            if (k < columnSpecs.length - 1) {
                jsonContent.append(",");
            }
        }
        jsonContent.append("],");
        jsonContent.append("'isExportable':'true',");
        jsonContent.append("'name': '" + tableName + "'");
        jsonContent.append("}");
        jsonContent.insert(0, "CREATE TABLE ");
        Log.i(LOG_TAG, "result = " + jsonContent.toString());
        return jsonContent.toString();
    }

    /* access modifiers changed from: private */
    public String doPostRequest(String query2, String authToken) {
        String jsonContent = query2.trim().substring("create table".length());
        Log.i(LOG_TAG, "Http Post content = " + jsonContent);
        HttpPost request = new HttpPost("https://www.googleapis.com/fusiontables/v2/tables?key=" + ApiKey());
        try {
            StringEntity entity = new StringEntity(jsonContent);
            entity.setContentType("application/json");
            request.addHeader(AUTH.WWW_AUTH_RESP, AUTHORIZATION_HEADER_PREFIX + authToken);
            request.setEntity(entity);
            try {
                HttpResponse response = new DefaultHttpClient().execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                if (response == null || statusCode != 200) {
                    Log.i(LOG_TAG, "Error: " + response.getStatusLine().toString());
                    this.queryResultStr = response.getStatusLine().toString();
                } else {
                    try {
                        String jsonResult = httpApacheResponseToString(response);
                        JSONObject jsonObj = new JSONObject(jsonResult);
                        if (jsonObj.has("tableId")) {
                            this.queryResultStr = "tableId," + jsonObj.get("tableId");
                        } else {
                            this.queryResultStr = jsonResult;
                        }
                        Log.i(LOG_TAG, "Response code = " + response.getStatusLine());
                        Log.i(LOG_TAG, "Query = " + query2 + "\nResultStr = " + this.queryResultStr);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        StringEntity stringEntity = entity;
                        return "Error: " + e.getMessage();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                        StringEntity stringEntity2 = entity;
                        return "Error: " + e2.getMessage();
                    }
                }
                StringEntity stringEntity3 = entity;
                return this.queryResultStr;
            } catch (ClientProtocolException e3) {
                e3.printStackTrace();
                StringEntity stringEntity4 = entity;
                return "Error: " + e3.getMessage();
            } catch (IOException e4) {
                e4.printStackTrace();
                StringEntity stringEntity5 = entity;
                return "Error: " + e4.getMessage();
            }
        } catch (UnsupportedEncodingException e5) {
            e5.printStackTrace();
            return "Error: " + e5.getMessage();
        }
    }

    private class QueryProcessorV2 extends AsyncTask<String, Void, String> {
        private static final String STAG = "FUSION_SERVICE_ACCOUNT";
        private static final String TAG = "QueryProcessorV2";
        private final Activity activity;
        private final ProgressDialog dialog;

        QueryProcessorV2(Activity activity2) {
            Log.i(TAG, "Creating AsyncFusiontablesQuery");
            this.activity = activity2;
            this.dialog = new ProgressDialog(activity2);
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            if (FusiontablesControl.this.ShowLoadingDialog()) {
                this.dialog.setMessage(FusiontablesControl.this.LoadingDialogMessage());
                this.dialog.show();
            }
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... params) {
            String query = params[0];
            Log.i(TAG, "Starting doInBackground " + query);
            if (FusiontablesControl.this.isServiceAuth) {
                return serviceAuthRequest(query);
            }
            return userAuthRequest(query);
        }

        private String userAuthRequest(String query) {
            String unused = FusiontablesControl.this.queryResultStr = "";
            String authToken = new OAuth2Helper().getRefreshedAuthToken(this.activity, FusiontablesControl.this.authTokenType);
            if (authToken == null) {
                return OAuth2Helper.getErrorMessage();
            }
            if (query.toLowerCase().contains("create table")) {
                String unused2 = FusiontablesControl.this.queryResultStr = FusiontablesControl.this.doPostRequest(FusiontablesControl.this.parseSqlCreateQueryToJson(query), authToken);
                return FusiontablesControl.this.queryResultStr;
            }
            com.google.api.client.http.HttpResponse response = FusiontablesControl.this.sendQuery(query, authToken);
            if (response != null) {
                String unused3 = FusiontablesControl.this.queryResultStr = FusiontablesControl.httpResponseToString(response);
                Log.i(TAG, "Query = " + query + "\nResultStr = " + FusiontablesControl.this.queryResultStr);
            } else {
                String unused4 = FusiontablesControl.this.queryResultStr = FusiontablesControl.this.errorMessage;
                Log.i(TAG, "Error:  " + FusiontablesControl.this.errorMessage);
            }
            return FusiontablesControl.this.queryResultStr;
        }

        private String serviceAuthRequest(String query) {
            com.google.api.client.http.HttpResponse response;
            String unused = FusiontablesControl.this.queryResultStr = "";
            String unused2 = FusiontablesControl.this.errorMessage = FusiontablesControl.this.standardErrorMessage;
            HttpTransport TRANSPORT = AndroidHttp.newCompatibleTransport();
            JsonFactory JSON_FACTORY = new GsonFactory();
            Log.i(STAG, "keyPath " + FusiontablesControl.this.keyPath);
            try {
                if (FusiontablesControl.this.cachedServiceCredentials == null) {
                    File unused3 = FusiontablesControl.this.cachedServiceCredentials = MediaUtil.copyMediaToTempFile(FusiontablesControl.this.container.$form(), FusiontablesControl.this.keyPath);
                }
                Fusiontables.Query.Sql sql = new Fusiontables.Builder(TRANSPORT, JSON_FACTORY, new GoogleCredential.Builder().setTransport(TRANSPORT).setJsonFactory(JSON_FACTORY).setServiceAccountId(FusiontablesControl.this.serviceAccountEmail).setServiceAccountScopes(new String[]{FusiontablesControl.this.scope}).setServiceAccountPrivateKeyFromP12File(FusiontablesControl.this.cachedServiceCredentials).build()).setJsonHttpRequestInitializer(new GoogleKeyInitializer(FusiontablesControl.this.ApiKey())).build().query().sql(query);
                sql.put("alt", "csv");
                response = null;
                response = sql.executeUnparsed();
            } catch (GoogleJsonResponseException e) {
                Log.i(STAG, "Got a JsonResponse exception on sql.executeUnparsed");
                String unused4 = FusiontablesControl.this.errorMessage = parseJsonResponseException(e.getMessage());
                FusiontablesControl.this.signalJsonResponseError(query, FusiontablesControl.this.errorMessage);
            } catch (Exception e2) {
                Log.i(STAG, "Got an unanticipated exception on sql.executeUnparsed");
                Log.i(STAG, "Exception class is " + e2.getClass());
                Log.i(STAG, "Exception message is " + e2.getMessage());
                Log.i(STAG, "Exception is " + e2);
                Log.i(STAG, "Point e");
                Log.i(STAG, "end of printing exception");
                String unused5 = FusiontablesControl.this.errorMessage = e2.getMessage();
                FusiontablesControl.this.signalJsonResponseError(query, FusiontablesControl.this.errorMessage);
            } catch (Throwable e3) {
                Log.i(STAG, "in Catch Throwable e");
                e3.printStackTrace();
                String unused6 = FusiontablesControl.this.queryResultStr = e3.getMessage();
            }
            if (response != null) {
                String unused7 = FusiontablesControl.this.queryResultStr = FusiontablesControl.httpResponseToString(response);
                Log.i(STAG, "Query = " + query + "\nResultStr = " + FusiontablesControl.this.queryResultStr);
            } else {
                String unused8 = FusiontablesControl.this.queryResultStr = FusiontablesControl.this.errorMessage;
                Log.i(STAG, "Error with null response:  " + FusiontablesControl.this.errorMessage);
            }
            Log.i(STAG, "executed sql query");
            Log.i(STAG, "returning queryResultStr = " + FusiontablesControl.this.queryResultStr);
            return FusiontablesControl.this.queryResultStr;
        }

        /* access modifiers changed from: package-private */
        public String parseJsonResponseException(String exceptionMessage) {
            Log.i(STAG, "parseJsonResponseException: " + exceptionMessage);
            return exceptionMessage;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            Log.i(FusiontablesControl.LOG_TAG, "Query result " + result);
            if (result == null) {
                result = FusiontablesControl.this.errorMessage;
            }
            this.dialog.dismiss();
            FusiontablesControl.this.GotResult(result);
        }
    }

    /* access modifiers changed from: package-private */
    public void signalJsonResponseError(String query2, String parsedException) {
        this.form.dispatchErrorOccurredEventDialog(this, "SendQuery", ErrorMessages.FUSION_TABLES_QUERY_ERROR, query2, parsedException);
    }
}
