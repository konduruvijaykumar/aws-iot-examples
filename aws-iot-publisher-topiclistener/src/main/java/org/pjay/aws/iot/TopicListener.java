/**
 * 
 */
package org.pjay.aws.iot;

import org.pjay.aws.iot.entity.LightOnOff;
import org.pjay.aws.iot.entity.SensorData;
import org.pjay.aws.iot.serviceimpl.SensorDataServiceImpl;
import org.pjay.aws.iot.util.ApplicationContextUtility;
import org.pjay.aws.iot.util.IOTConstants;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Vijay Konduru
 *
 */
public class TopicListener extends AWSIotTopic {
	
	// Not working coming null as this call by other service and is out of spring context
	//@Autowired
	//SensorDataService sensorDataService;
	
	private ObjectMapper mapper = new ObjectMapper();
	private boolean wasActuatorTriggeredLast = false;
	private SensorData sensorData = null;
	private String payload = "";
	private AWSIotMqttClient awsIotClient = null;
	private AWSIotMessage publishMessage = null;

	public TopicListener(String topic, AWSIotQos qos) {
		super(topic, qos);
	}

	// override onMessage for printing messages received on topic
	@Override
	public void onMessage(AWSIotMessage message) {
		System.out.println("Message received ::\t" + message.getStringPayload());
		try {
			// This is triggered by some other callback service and is not under spring application context or spring container, hence no data is getting saved.
			// This code runs as a different thread and the values are also not going to other service, data going as null
			// Hence tried making a network call, which also did not work, sending data as null.
			// Better alternative to try is asyncronus call and send object or i am trying serialization and deserialization of object in other thread
			// IMP:: Let's try serialization and deserialization of object in other thread
			// SensorData sensorData = mapper.readValue(message.getStringPayload(), SensorData.class);
			// RestTemplate restTemplate = new RestTemplate();
			// restTemplate.postForEntity("http://localhost:8080/sensors/savesensordata", new SensorData(sensorData.getTemperature(), sensorData.getHumidity()), SensorData.class);
			// sensorDataService.saveData(sensorData);
			// sensorDataService.saveData(new SensorData(sensorData.getTemperature(), sensorData.getHumidity()));
			
			// Not Working
//			String filename = "sensordatabean.ser";
//			FileOutputStream fos = null;
//			ObjectOutputStream out = null;
//			SensorData sensorData = null;
//			
//			fos = new FileOutputStream(filename);
//			out = new ObjectOutputStream(fos);
//			sensorData = mapper.readValue(message.getStringPayload(), SensorData.class);
//			out.writeObject(sensorData);
//			out.close();
//			fos.close();
			// sensorDataService.saveDataFromTopic();
			// (new SensorDataServiceImpl()).saveDataFromTopic();
			// Not Working
			
			// Logic to trigger red light or led on raspberry pi temperature reaches beyond some threshold value
			// We might end up putting a hack in code to trigger light based on humidity, as we don't get higher temperature in lab without a heat blower
			
			sensorData = mapper.readValue(message.getStringPayload(), SensorData.class);
			if(null != sensorData){
				// We will add humidity logic as hack to test this code,instead of temperature. Which can be easily tested in lab
				if(sensorData.getHumidity() >= 88F){
					// if(sensorData.getTemperature() >= 48F){
					// if(sensorData.getHumidity() >= 88F){
					payload = mapper.writeValueAsString(new LightOnOff(true));
					publishMessage(payload);
					wasActuatorTriggeredLast = true;
				}
				else if(sensorData.getHumidity() < 88F && wasActuatorTriggeredLast){
					// else if(sensorData.getTemperature() < 48F && wasActuatorTriggeredLast){
					// else if(sensorData.getHumidity() < 88F && wasActuatorTriggeredLast){
					payload = mapper.writeValueAsString(new LightOnOff(false));
					publishMessage(payload);
					wasActuatorTriggeredLast = false;
				}
//				else{
//					// Do noting or no need of this block
//				}
			}
			
			//ApplicationContextUtility.getInstance().getApplicationContextBean().getApplicationContext().getBean("sensorDataServiceImpl", SensorDataServiceImpl.class).saveData(mapper.readValue(message.getStringPayload(), SensorData.class));
			ApplicationContextUtility.getInstance().getApplicationContextBean().getApplicationContext().getBean("sensorDataServiceImpl", SensorDataServiceImpl.class).saveData(sensorData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void publishMessage(String payload) throws AWSIotException{
		awsIotClient = ApplicationContextUtility.getInstance().getApplicationContextBean().getApplicationContext().getBean("awsIotClientInitializer", AWSIotClientInitializer.class).getAwsIotClient();
		publishMessage = new NonBlockingPublishListener(IOTConstants.TestPublisherTopic, IOTConstants.TestTopicQos, payload);
		awsIotClient.publish(publishMessage);
	}

}
