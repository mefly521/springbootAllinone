package com.demo.mq;

import org.apache.activemq.ActiveMQSslConnectionFactory;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class CustomActiveMQConnectionFactory extends ActiveMQSslConnectionFactory {


	public CustomActiveMQConnectionFactory() {
		super();
	}

	@Override
	public void setTrustStore(String trustStore) throws Exception {
		super.setTrustStore(trustStore);
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	protected TrustManager[] createTrustManager() throws Exception {
		return new TrustManager[] { new X509ExtendedTrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket)
					throws CertificateException {

			}

			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket)
					throws CertificateException {

			}

			@Override
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine)
					throws CertificateException {

			}

			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine)
					throws CertificateException {

			}

			@Override
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

			}

			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
		} };
	}
}
