/**
 * 
 */
package org.pjay.aws.iot.model;

/**
 * @author Vijay Konduru
 *
 */
public class LightOnOff {

	private boolean lightOn;

	public LightOnOff() {
	}

	public LightOnOff(boolean lightOn) {
		this.lightOn = lightOn;
	}

	public boolean isLightOn() {
		return lightOn;
	}

	public void setLightOn(boolean lightOn) {
		this.lightOn = lightOn;
	}

}
