package engineerTools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

public class Tester {

    @Test (expected = IllegalArgumentException.class)
    public void testGear(){
        Gear a = new Gear(30);
        Gear b = new Gear(60);
        
        Gear.connectGears(a, b);
        a.setFrequency(1.0);
        a.applyTorque(10.0);
        
        assertTrue(a.getFrequency() == 1);
        assertTrue(a.getTorque() == 10);
        assertTrue(b.getFrequency() == -0.5);
        assertTrue(b.getTorque() == -20);
        
        a.applyTorque(30);
        
        Gear c = new Gear(6);
        b.addOnToAxis(c);
        
        
        assertTrue(a.getFrequency() == 1);
        assertTrue(a.getTorque() == 30);
        assertTrue(b.getFrequency() == -0.5);
        assertTrue(b.getTorque() == -60);
        assertTrue(c.getFrequency() == -0.5);
        assertTrue(c.getTorque() == -60);
        
        c.applyTorque(10);
        
        
        assertTrue(a.getFrequency() == 1);
        assertTrue(a.getTorque() == -5);
        assertTrue(b.getFrequency() == -0.5);
        assertTrue(b.getTorque() == 10);
        assertTrue(c.getFrequency() == -0.5);
        assertTrue(c.getTorque() == 10);
        
        Gear d = new Gear(600);
        d.connectGear(c);
        
        
        assertTrue(a.getFrequency() == 0);
        assertTrue(a.getTorque() == 0);
        assertTrue(b.getFrequency() == 0);
        assertTrue(b.getTorque() == 0);
        assertTrue(c.getFrequency() == 0);
        assertTrue(c.getTorque() == 0);
        assertTrue(d.getFrequency() == 0);
        assertTrue(d.getTorque() == 0);
        
        
        d.setFrequency(1);
        d.applyTorque(1000);
        
        
        assertTrue(a.getFrequency() == 200);
        assertTrue(a.getTorque() == 5);
        assertTrue(b.getFrequency() == -100);
        assertTrue(b.getTorque() == -10);
        assertTrue(c.getFrequency() == -100);
        assertTrue(c.getTorque() == -10);
        assertTrue(d.getFrequency() == 1);
        assertTrue(d.getTorque() == 1000);
        
        //Should throw error
        Gear k = new Gear(-3);

    }
    
    @Test
    public void testGearAxis(){
        Gear a = new Gear(10);
        Gear b = new Gear(20);
        Gear c = new Gear(30);
        Gear d = new Gear(40);
        
        a.addOnToAxis(b);
        b.addOnToAxis(c);
        a.setFrequency(10);
        System.out.println(a.sharesAxisWith(c));
    }
    
    @Test
    public void testBlah(){
        Gear b = new Gear(120);
        Gear c = new Gear(360);
        Gear d = new Gear(300);
        Gear e = new Gear(240);
        Gear.connectGears(b, c);
        c.addOnToAxis(d);
        Gear.connectGears(d, e);
        
        b.applyTorque(900);
        System.out.println(e.getTorque());
    }
    
    
    @Test
    public void testPendulum(){
        Gear a = new Gear(80, 0.03);
        Gear b = new Gear(18);
        Gear c = new Gear(60);
        Gear d = new Gear(10);
        Gear e = new Gear(60);
        Gear f = new Gear(10);
        Gear g = new Gear(60);
        Gear h = new Gear(6);
        Gear i = new Gear(30);
        
        Gear.connectGears(a, b);
        b.addOnToAxis(c);
        Gear.connectGears(c, d);
        d.addOnToAxis(e);
        Gear.connectGears(e, f);
        f.addOnToAxis(g);
        Gear.connectGears(g, h);
        h.addOnToAxis(i);
        
        i.setTeethSpeed(0.5);
        a.applyEdgeForce(16.709);
        System.out.println(a.getPeriod());
        System.out.println(i.getTorque());
        
        
    }

}
