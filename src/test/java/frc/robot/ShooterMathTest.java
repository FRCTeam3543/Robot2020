package frc.robot;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShooterMathTest
{
    static final double K = 2.0;
    ShooterMath shoorterMath = new ShooterMath(Math.PI/4, 1.5, K);

    @Test
    public void testShooterMath()
    {
        assertEquals(7.83, shoorterMath.muzzleVelocityFromDistanceAway(2.5), 0.005);
        assertEquals(8.86, shoorterMath.muzzleVelocityFromDistanceAway(2.0), 0.005);
        assertEquals(7.67, shoorterMath.muzzleVelocityFromDistanceAway(3.0), 0.005);
    }

    @Test
    public void testZeroReturnedForBadInput()
    {
        assertEquals(0.0, shoorterMath.muzzleVelocityFromDistanceAway(0.5), 0.00001);
        assertEquals(0.0, shoorterMath.muzzleVelocityFromDistanceAway(-2.5), 0.00001);
    }

    @Test
    public void testVoltageConversion()
    {
        assertEquals(7.83 * K, shoorterMath.muzzleVoltsForDistanceAway(2.5), 0.005);
    }
}