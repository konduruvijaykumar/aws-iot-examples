/**
 * 
 */
package org.pjay.aws.iot;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.pjay.aws.iot.model.SensorData;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Vijay Konduru
 *
 */
public class BlockingPublisher implements Runnable {

	private final AWSIotMqttClient awsIotClient;
	// set to false when deploying on Raspberry PI
	private boolean isSimulation = true;
	private int numberOfIterations = 20;//30

	public BlockingPublisher(AWSIotMqttClient awsIotClient) {
		this.awsIotClient = awsIotClient;
	}

	@Override
	public void run() {
		if (isSimulation) {
			publishSimulationData();
		} else {
			// Main logic for PI with python code run using runtime process
			try {
				//Process pythonProcess = Runtime.getRuntime().exec("python /home/pi/dht22/DHT22.py");
				//Process pythonProcess = Runtime.getRuntime().exec("python3 /home/pi/PROJECT-DSI/sensingDHT22.py");
				Process pythonProcess = Runtime.getRuntime().exec("python3 /home/pi/PROJECT-DSI/DHT22SensorDemo.py");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void publishSimulationData() {
		List<Float> temperatureDummyData = Arrays.asList(33F, 42F, 28F, 38F, 58F, 35F, 48F, 53F, 24F, 18F, 21F, 12F, 27F, 60F, 39F, 59F, 66F, 46F);
		List<Float> humidityDummyData = Arrays.asList(32F, 61F, 29F, 47F, 59F, 36F, 56F, 67F, 25F, 19F, 22F, 43F, 26F, 13F, 37F, 49F, 51F, 34F);
		Random random = new Random();
		ObjectMapper mapper = new ObjectMapper();
		String payload = "";
		for ( ;numberOfIterations > 0; ) {
			SensorData data = new SensorData(temperatureDummyData.get(random.nextInt(temperatureDummyData.size())), humidityDummyData.get(random.nextInt(humidityDummyData.size())));
			try {
				payload = mapper.writeValueAsString(data);
				awsIotClient.publish(PublishAndSubscribe.TestTopic, payload);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (AWSIotException e){
				System.out.println(System.currentTimeMillis() + ": publish failed for " + payload);
			}
			
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println(System.currentTimeMillis() + ": BlockingPublisher was interrupted");
                return;
            }
            numberOfIterations--;
		}
	}

}
