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
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * @author Vijay Konduru
 *
 */
public class Pi4JLightOnOffSubscriber {
	
    public static final String TestTopic = "raspberrypi/pi4j/subscribe";
    public static final AWSIotQos TestTopicQos = AWSIotQos.QOS0;
    
    private static AWSIotMqttClient awsIotClient;
    
	// create gpio controller
	final static GpioController gpio = GpioFactory.getInstance();
	// provision gpio pin #01 as an output pin and turn on
	final static GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "LED", PinState.LOW);


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

        //AWSIotTopic topic = new TopicListener(TestTopic, TestTopicQos);
        AWSIotTopic topic = new TopicListener(TestTopic, TestTopicQos, pin);
        awsIotClient.subscribe(topic, true);
        // added to stop or terminate the program after completion
        // We cannot disconnect as the application terminates and no use of running subscriber, to fetch data
        // Or we can have a work around by adding sleep for the main thread or start a new thread with delay, join to main thread 
        //awsIotClient.disconnect();
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
