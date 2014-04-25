package com.android.upiicsapp.app;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Hugo on 24/04/14.
 */
public class Scraper {
    private Document res;
    private Elements viewState;
    private Elements eventValidation;
    private Connection.Response response;
    private Map<String,String> algo;

    public Scraper(String URLtoLogin) {
        try{
            res = Jsoup.connect(URLtoLogin).get();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void login(String boleta,String pass){
        viewState = res.select("input[name=__VIEWSTATE");
        eventValidation = res.select("input[name=__EVENTVALIDATION]");
        try{
            response = Jsoup.connect("https://www.saes.upiicsa.ipn.mx/default.aspx")
                .data("ctl00$leftColumn$LoginViewSession$LoginSession$UserName", boleta)
                .data("ctl00$leftColumn$LoginViewSession$LoginSession$Password", pass)
                .data("ctl00$leftColumn$LoginViewSession$LoginSession$LoginButton", "Iniciar")
                .data("__VIEWSTATE", viewState.val())
                .data("__EVENTVALIDATION", eventValidation.val())
                .method(Connection.Method.POST)
                .timeout(30000)
                .execute();
            }
        catch(IOException e) {
         e.printStackTrace();
        }
    }
    public Document sraping_to(String URL){
        Document doc = new Document("");
        algo = response.cookies();
        try {
            doc = Jsoup.connect(URL)
                    .cookies(algo)
                    .timeout(30000)
                    .get();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return doc;
    }

    public static void breakSSL() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
