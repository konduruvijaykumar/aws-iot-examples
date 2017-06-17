/**
 * 
 */
package org.pjay.aws.iot;

import org.pjay.aws.iot.util.ApplicationContextBean;
import org.pjay.aws.iot.util.ApplicationContextUtility;
import org.pjay.aws.iot.util.CommandArguments;
import org.pjay.aws.iot.util.SampleUtil;
import org.pjay.aws.iot.util.SampleUtil.KeyStorePasswordPair;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.iot.client.AWSIotMqttClient;

/**
 * @author Vijay Konduru
 *
 */
// @Singleton
// By default should be singleton, if not enable above annotation
@Component("awsIotClientInitializer")
public class AWSIotClientInitializer implements InitializingBean, DisposableBean {

	private AWSIotMqttClient awsIotClient;
	
	// Hack for topic listener to get spring context
	@Autowired
	ApplicationContextBean applicationContextBean;

	public AWSIotMqttClient getAwsIotClient() {
		return awsIotClient;
	}

	public void setAwsIotClient(AWSIotMqttClient awsIotClient) {
		this.awsIotClient = awsIotClient;
		System.out.println("AWSIotClientInitializer Bean awsIotClient property value from set method :: " + awsIotClient);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Initializing AWSIotClientInitializer Bean Values");
		//String[] arr = {};
		//CommandArguments arguments = CommandArguments.parse(new String[]{});
		CommandArguments arguments = CommandArguments.parse(new String[0]);
		initClient(arguments);
		
		// Hack for topic listener to get spring context
		ApplicationContextUtility.getInstance().setApplicationContextBean(applicationContextBean);
	}
	
    private void initClient(CommandArguments arguments) {
        String clientEndpoint = arguments.getNotNull("clientEndpoint", SampleUtil.getConfig("clientEndpoint"));
        String clientId = arguments.getNotNull("clientId", SampleUtil.getConfig("clientId"));

        String certificateFile = arguments.get("certificateFile", SampleUtil.getConfig("certificateFile"));
        String privateKeyFile = arguments.get("privateKeyFile", SampleUtil.getConfig("privateKeyFile"));
        if (awsIotClient == null && certificateFile != null && privateKeyFile != null) {
            String algorithm = arguments.get("keyAlgorithm", SampleUtil.getConfig("keyAlgorithm"));
            KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile, algorithm);

            awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
        }

        if (awsIotClient == null) {
            String awsAccessKeyId = arguments.get("awsAccessKeyId", SampleUtil.getConfig("awsAccessKeyId"));
            String awsSecretAccessKey = arguments.get("awsSecretAccessKey", SampleUtil.getConfig("awsSecretAccessKey"));
            String sessionToken = arguments.get("sessionToken", SampleUtil.getConfig("sessionToken"));

            if (awsAccessKeyId != null && awsSecretAccessKey != null) {
                awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey,
                        sessionToken);
            }
        }

        if (awsIotClient == null) {
            throw new IllegalArgumentException("Failed to construct client due to missing certificate or credentials.");
        }
    }

	@Override
	public void destroy() throws Exception {
		// This might throw error also, if the bean awsIotClient was not connected
		System.out.println("Destroying AWSIotClientInitializer Bean");
		awsIotClient.disconnect();
	}

}
