package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.SpeedController;

/**
 * Represents a fake speed controller. 
 *
 * @author Marty Kausas
 */
public class DummyOutput implements SpeedController {

    private double output;

    @Override
    public double get() {
        return output;
    }

    @Override
    public void set(double speed, byte syncGroup) {
        set(speed);
    }

    @Override
    public void set(double speed) {
        output = speed;
    }

    public void disable() {}

    @Override
    public void pidWrite(double output) {
        this.output = output;
    }
}
