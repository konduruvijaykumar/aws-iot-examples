/**
 * 
 */
package org.pjay.aws.iot;

import org.pjay.aws.iot.util.CommandArguments;
import org.pjay.aws.iot.util.SampleUtil;
import org.pjay.aws.iot.util.SampleUtil.KeyStorePasswordPair;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

/**
 * @author Vijay Konduru
 *
 */
public class PublishAndSubscribe {
	
    public static final String TestTopic = "sdk/test/java";
    public static final AWSIotQos TestTopicQos = AWSIotQos.QOS0;
    
    private static AWSIotMqttClient awsIotClient;

    public static void setClient(AWSIotMqttClient client) {
        awsIotClient = client;
    }

	/**
	 * @param args
	 * @throws AWSIotException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws AWSIotException, InterruptedException {
        CommandArguments arguments = CommandArguments.parse(args);
        initClient(arguments);

        awsIotClient.connect();

        AWSIotTopic topic = new TopicListener(TestTopic, TestTopicQos);
        awsIotClient.subscribe(topic, true);
        
        Thread blockingPublishThread = new Thread(new BlockingPublisher(awsIotClient));
        Thread nonBlockingPublishThread = new Thread(new NonBlockingPublisher(awsIotClient));

        blockingPublishThread.start();
        nonBlockingPublishThread.start();

        blockingPublishThread.join();
        nonBlockingPublishThread.join();
        // added to stop or terminate the program after completion
        awsIotClient.disconnect();
	}
	
    private static void initClient(CommandArguments arguments) {
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

}
