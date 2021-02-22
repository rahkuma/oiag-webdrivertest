package com.tibco.automation.oiag.common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLUtil {
	
	private String source;
	
	public HTMLUtil(String source) {
		this.source = source;
	}
	
	public HTMLUtil() {
		
	}
	
	public List<String> getItemsList(String cssQuery) {
		List<String> list = new ArrayList<>();
		Document soupObj = Jsoup.parse(source);
		Elements elements = soupObj.select(cssQuery);
		for (Element element : elements) {
			list.add(element.text());
		}
		return list;
	}
	
	public String getOneCell(String cssQuery) {
		Document soupObj = Jsoup.parse(source);
		Element element = soupObj.selectFirst(cssQuery);
		return element.ownText();
	}
	
	public String getOneLine(String cssQuery) {
		Document soupObj = Jsoup.parse(source);
		Elements elements = soupObj.select(cssQuery);
		String line = "";
		for (Element element : elements) {
			line += element.text().trim().replaceAll("\\s+", "") + "::";
		}
		
		return line.replace("::ffff:", "").toUpperCase().replace("--NULL--", "")
			.replaceAll("(::){2,}", "::").replaceAll("(::)?(\\d+/){2}\\d+:\\d+:\\d+(::)?", "::")
			.replaceAll("^(::)+", "").replaceAll("(::)+$", "");
	}
	
	public String getOneLineForSearch(String cssQuery) {
		Document soupObj = Jsoup.parse(source);
		Elements elements = soupObj.select(cssQuery);
		String line = "";
		for (Element element : elements) {
			line += element.text().trim().replace("\\", "").replace("/", "").replaceAll("\\s+", "") + "::";
		}
		
		return line.replace("Message:", "").replaceAll("(::){3,}", "::")
			.replaceAll("^(::)+", "").replaceAll("(::)+$", "");
	}
	
	public String getPageSource(String url) {
		try {
			trustAllHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
			Document soupObj = Jsoup.connect(url).get();
			return soupObj.html();
		} catch (IOException e) {
			return "";
		} catch (Exception e) {
			return "";
		}
	}
	
	private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
	
	HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            return true;
        }
    };
	
	static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
}
