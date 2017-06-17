/**
 * 
 */
package org.pjay.aws.iot.service;

import java.util.List;

import org.pjay.aws.iot.entity.SensorData;

/**
 * @author Vijay Konduru
 *
 */
public interface SensorDataService {

	SensorData saveData(SensorData data);

	List<SensorData> getAllSensorData();

	SensorData getLatestSensorData();

	SensorData getHighestTemperature();

	SensorData getLowestTemperature();

	SensorData getHighestHumidity();

	SensorData getLowestHumidity();

	// Not Required, found another way
	// void saveDataFromTopic();

}
