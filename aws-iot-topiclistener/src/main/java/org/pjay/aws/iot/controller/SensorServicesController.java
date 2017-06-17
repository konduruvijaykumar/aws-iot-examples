/**
 * 
 */
package org.pjay.aws.iot.controller;

import org.pjay.aws.iot.AWSIotClientInitializer;
import org.pjay.aws.iot.TopicListener;
import org.pjay.aws.iot.util.IOTConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotTopic;

/**
 * @author Vijay Konduru
 *
 */
@RestController
@RequestMapping("/")
public class SensorServicesController {
	
	@Autowired
	AWSIotClientInitializer awsIotClientInitializer;
	
	@RequestMapping(method={RequestMethod.GET})
	public String welcome() {
		return "Welcome to IOT Sensor Data Webservice application";
	}
	
	@RequestMapping(value="/start/service", method={RequestMethod.GET})
	public String startService() {
        try {
        	System.out.println("awsIotClientInitializer.getAwsIotClient() :: " + awsIotClientInitializer.getAwsIotClient());
        	awsIotClientInitializer.getAwsIotClient().connect();
        	AWSIotTopic topic = new TopicListener(IOTConstants.TestTopic, IOTConstants.TestTopicQos);
			awsIotClientInitializer.getAwsIotClient().subscribe(topic, true);
		} catch (AWSIotException e) {
			e.printStackTrace();
			return "Service not started due to some internal server issue";
		}
		return "Started";
	}
	
	@RequestMapping(value="/stop/service", method={RequestMethod.GET})
	// This might not be implemented, as it is not required for now
	public String stopService() {
//		try {
//			awsIotClientInitializer.getAwsIotClient().disconnect();
//		} catch (AWSIotException e) {
//			e.printStackTrace();
//		}
		return "Stopped";
	}

}
