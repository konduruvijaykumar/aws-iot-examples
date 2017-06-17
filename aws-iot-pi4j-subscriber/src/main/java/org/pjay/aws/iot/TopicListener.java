/**
 * 
 */
package org.pjay.aws.iot;

import java.io.IOException;

import org.pjay.aws.iot.model.LightOnOff;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

/**
 * @author Vijay Konduru
 *
 */
public class TopicListener extends AWSIotTopic {
	
	private GpioPinDigitalOutput pin;
	private LightOnOff lightOnOff = null;

	public TopicListener(String topic, AWSIotQos qos) {
		super(topic, qos);
	}
	
	public TopicListener(String topic, AWSIotQos qos, GpioPinDigitalOutput pin) {
		super(topic, qos);
		this.pin = pin;
		if(null != this.pin){
			this.pin.setShutdownOptions(true, PinState.LOW);
		}
	}

	// override onMessage for printing messages received on topic
	@Override
	public void onMessage(AWSIotMessage message) {
		System.out.println("Message received ::\t" + message.getStringPayload());
		ObjectMapper mapper = new ObjectMapper();
		try {
			lightOnOff = mapper.readValue(message.getStringPayload(), LightOnOff.class);
			if(null != pin && null != lightOnOff){
				if(lightOnOff.isLightOn())
					pin.high();
				else
					pin.low();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
