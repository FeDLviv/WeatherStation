package work;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyAuthenticator extends Authenticator {

	public ProxyAuthenticator() {
		System.setProperty("http.proxyHost", MyProperties.getInstance()
				.getProperty("http.proxyHost"));
		System.setProperty("http.proxyPort", MyProperties.getInstance()
				.getProperty("http.proxyPort"));
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(MyProperties.getInstance()
				.getProperty("user"), MyProperties.getInstance()
				.getProperty("password").toCharArray());
	}
}