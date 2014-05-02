package com.android.upiicsapp.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    Document res;
    Elements viewState;
    Elements eventValidation;
    Connection.Response response;
    Map<String,String> cookies;

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
        Document doc = null;
        cookies = response.cookies();
        try {
            doc = Jsoup.connect(URL)
                    .cookies(cookies)
                    .timeout(30000)
                    .get();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return doc;
    }
    public String[] getCalficaciones(Document doc){
        String[] tablaCal;
        int numMat=0;
        for(Element data:doc.select("table#ctl00_mainCopy_GV_Calif tr")){
            numMat++;
        }
        tablaCal = new String[7*numMat];

        tablaCal[0]="Grupo";
        tablaCal[1]="Materia";
        tablaCal[2]="1er Parcial";
        tablaCal[3]="2do Parcial";
        tablaCal[4]="3er Parcial";
        tablaCal[5]="Ext";
        tablaCal[6]="Final";
        int i=7;
        int colum=0;
        for(Element data:doc.select("table#ctl00_mainCopy_GV_Calif tr td")){
            colum++;
            if(colum==2){
                char[] sigla = new  char[5];
                int num=0;
                boolean isSigla=true;
                for(int j=0;j<data.text().length();j++){
                    if(isSigla){
                        sigla[num]=data.text().charAt(j);
                        isSigla=false;
                        num++;
                    }
                    else {
                        if (data.text().charAt(j)==' '){isSigla=true;}
                    }
                }
                tablaCal[i]= String.valueOf(sigla);
            }
            else {
                if(colum==7){colum=0;}
                tablaCal[i] = data.text();
            }
            i++;
        }
        return tablaCal;
    }

    public static boolean isOnline (Context context){
        boolean state=false;
        ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] ni= cm.getAllNetworkInfo();
        for (int i=0;i<2;i++){
            if(ni[i].getState()==NetworkInfo.State.CONNECTED){
                state = true;
            }
        }
        return state;
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
