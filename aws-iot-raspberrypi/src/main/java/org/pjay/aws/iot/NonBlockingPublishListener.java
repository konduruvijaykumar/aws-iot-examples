/**
 * 
 */
package org.pjay.aws.iot;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;

/**
 * @author Vijay Konduru
 *
 */
public class NonBlockingPublishListener extends AWSIotMessage {

	public NonBlockingPublishListener(String topic, AWSIotQos qos, String payload) {
		super(topic, qos, payload);
	}

	@Override
	public void onSuccess() {
		System.out.println(System.currentTimeMillis() + " Successfully sent message ::\t" + getStringPayload());
	}

	@Override
	public void onFailure() {
		System.out.println(System.currentTimeMillis() + " Message publish failed for ::\t" + getStringPayload());
	}

	@Override
	public void onTimeout() {
		System.out.println(System.currentTimeMillis() + " Message publish timedout for ::\t" + getStringPayload());
	}

}
