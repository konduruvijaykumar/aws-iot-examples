/**
 * 
 */
package org.pjay.aws.iot.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.pjay.aws.iot.entity.SensorData;
import org.pjay.aws.iot.repository.SensorDataRepository;
import org.pjay.aws.iot.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Vijay Konduru
 *
 */
@Service("sensorDataServiceImpl")
public class SensorDataServiceImpl implements SensorDataService {
	
	@Autowired
	SensorDataRepository sensorDataRepository;

	@Override
	public SensorData saveData(SensorData data) {
		return sensorDataRepository.save(data);
	}

	@Override
	public List<SensorData> getAllSensorData() {
		Iterable<SensorData> results = sensorDataRepository.findAll();
		if(results instanceof List){
			return (List<SensorData>)results;
		}
		List<SensorData> resultList = new ArrayList<>();
		for (SensorData sensorData : results) {
			resultList.add(sensorData);
		}
		return resultList;
	}

	@Override
	public SensorData getLatestSensorData() {
		return sensorDataRepository.findTopByOrderByDatetimeDesc();
	}

	@Override
	public SensorData getHighestTemperature() {
		return sensorDataRepository.findTopByOrderByTemperatureDesc();
	}

	@Override
	public SensorData getLowestTemperature() {
		return sensorDataRepository.findTopByOrderByTemperatureAsc();
	}

	@Override
	public SensorData getHighestHumidity() {
		return sensorDataRepository.findTopByOrderByHumidityDesc();
	}

	@Override
	public SensorData getLowestHumidity() {
		return sensorDataRepository.findTopByOrderByHumidityAsc();
	}

	// Not Required, found another way
/*	@Override
	public void saveDataFromTopic() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		SensorData sensorData = null;
		boolean isFileFound = false;
		String filename = "sensordatabean.ser";
		try {
			fis = new FileInputStream(filename);
			isFileFound = true;
			// Save logic
			sensorDataRepository.save(sensorData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			// Trying to close resources and delete file
			try {
				ois.close();
				fis.close();
				try {
					boolean fileDeleted = (new File(filename)).delete();
					System.out.println("File deleted... " + fileDeleted);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		// We are trying to delete the serialized object file
		if(isFileFound){
			boolean fileDeleted = (new File(filename)).delete();
			System.out.println("File deleted... " + fileDeleted);
		}
	}*/

}
