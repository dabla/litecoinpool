package org.smartwallet.stratum;

import static com.google.common.base.Throwables.throwIfUnchecked;
import static javax.net.ssl.SSLContext.getInstance;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SocketFactory {
	public Socket create(boolean isTLS) throws IOException {
		if (isTLS) {
            try {
                SSLContext sc = getInstance("TLS");
                sc.init(null, new TrustManager[]{new TrustAllX509TrustManager()}, new SecureRandom());
                javax.net.SocketFactory factory = sc.getSocketFactory();
                return factory.createSocket();
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throwIfUnchecked(e);
            }
        }
		
        return new Socket();
        
	}

	static class TrustAllX509TrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] certs,
                                       String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs,
                                       String authType) {
        }
    }
}
