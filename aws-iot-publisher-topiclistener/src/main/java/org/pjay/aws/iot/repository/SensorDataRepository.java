/**
 * 
 */
package org.pjay.aws.iot.repository;

import org.pjay.aws.iot.entity.SensorData;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Vijay Konduru
 *
 */
// http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.limit-query-result
public interface SensorDataRepository extends CrudRepository<SensorData, Long> {
	
	SensorData findTopByOrderByDatetimeDesc();
	
	SensorData findTopByOrderByTemperatureDesc();
	
	SensorData findTopByOrderByTemperatureAsc();
	
	SensorData findTopByOrderByHumidityDesc();
	
	SensorData findTopByOrderByHumidityAsc();
}
