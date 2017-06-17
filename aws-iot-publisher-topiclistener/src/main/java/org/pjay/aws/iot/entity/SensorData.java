/**
 * 
 */
package org.pjay.aws.iot.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Component;

/**
 * @author Vijay Konduru
 *
 */
@Entity
@Table(name = "SENSOR_DATA")
@Component
public class SensorData implements Serializable {

	private static final long serialVersionUID = 5474292069150094029L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private float temperature;
	private float humidity;
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetime = new Date();// Default to current date

	public SensorData() {
	}

	public SensorData(float temperature, float humidity, Date datetime) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.datetime = datetime;
	}

	public SensorData(float temperature, float humidity) {
		this.temperature = temperature;
		this.humidity = humidity;
	}

	public SensorData(Long id, float temperature, float humidity, Date datetime) {
		this.id = id;
		this.temperature = temperature;
		this.humidity = humidity;
		this.datetime = datetime;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		if (null != datetime) {
			this.datetime = datetime;
		} else {
			this.datetime = new Date();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
