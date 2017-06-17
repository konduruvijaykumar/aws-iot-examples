/**
 * 
 */
package org.pjay.aws.iot.controller;

import java.util.List;

import org.pjay.aws.iot.entity.SensorData;
import org.pjay.aws.iot.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vijay Konduru
 *
 */
@RestController
@RequestMapping("/sensors")
public class SensorDataServiceController {

	@Autowired
	SensorDataService sensorDataService;

	@RequestMapping(value = "/sensordata", method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public SensorData getSensorLatestData() {
		// List<SensorData> sensorData = Arrays.asList(new SensorData(42F, 59F),
		// new SensorData(34F, 88F),new SensorData(24F, 72F), new
		// SensorData(39F, 44F));;
		// Random random = new Random();
		// return sensorData.get(random.nextInt(sensorData.size()));
		return sensorDataService.getLatestSensorData();
	}

	@RequestMapping(value = "/allsensordata", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<SensorData> getAllSensorData() {
		return sensorDataService.getAllSensorData();
	}

	@RequestMapping(value = "/highesttemperature", produces = { MediaType.APPLICATION_JSON_VALUE })
	public SensorData getHighestTemperature() {
		return sensorDataService.getHighestTemperature();
	}

	@RequestMapping(value = "/lowesttemperature", produces = { MediaType.APPLICATION_JSON_VALUE })
	public SensorData getLowestTemperature() {
		return sensorDataService.getLowestTemperature();
	}

	@RequestMapping(value = "/highesthumidity", produces = { MediaType.APPLICATION_JSON_VALUE })
	public SensorData getHighestHumidity() {
		return sensorDataService.getHighestHumidity();
	}

	@RequestMapping(value = "/lowesthumidity", produces = { MediaType.APPLICATION_JSON_VALUE })
	public SensorData getLowestHumidity() {
		return sensorDataService.getLowestHumidity();
	}
	
	@RequestMapping(value = "/savesensordata", method = {RequestMethod.POST})
	public void saveSensorData(SensorData sensorData) {
		sensorDataService.saveData(sensorData);
	}

}
