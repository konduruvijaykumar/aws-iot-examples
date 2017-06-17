/**
 * 
 */
package org.pjay.aws.iot;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

/**
 * @author Vijay Konduru
 *
 */
public class TopicListener extends AWSIotTopic {

	public TopicListener(String topic, AWSIotQos qos) {
		super(topic, qos);
	}

	// override onMessage for printing messages received on topic
	@Override
	public void onMessage(AWSIotMessage message) {
		System.out.println("Message received ::\t" + message.getStringPayload());
	}

}
