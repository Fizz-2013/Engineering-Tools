package engineerTools;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Gear
 * This is a class that represents a toothed Gear that spins on an axis. 
 * 
 * A Gear can spin with a given frequency, at a given torque.
 * A Gear has a set number of teeth.
 * A Gear can be connected to another Gear. The Gear will adjust its frequency and torque based on Gear ratios.
 * A Gear belongs to a set of gears, all connected with the same axis. Other Gears can be added onto this Gear's axis.
 * The essential point is that a Gear is part of a system. 
 * It is a very interactive Object; modifying one Gear affects all Gears in the same system.
 * 
 * Representation Invariant of the Gear Class:
 *      A Gear has positive radius
 *      A Gear has positive, integer numbers of teeth
 *      All Gears have frequency and torque, such that Gear Ratios are fulfilled.
 *          Gear Ratios:
 *              Gears on same axis share the same frequency and torque
 *              Adjacent connected Gears have the same but reversed toothSpeed, where toothSpeed = frequency*teethNumber.
 *      A Gear is a separate entity that cannot be duplicated in the system.
 *          I.e. A Gear cannot be added to itself, either adjacently or on the same axis.
 * @author bryan_luu30794
 *
 */
public class Gear {
    private int teethNumber;
    private double frequency = 0;
    private double torque = 0;
    private double radius;
    private Set<Gear> connectedGears = new HashSet<Gear>();
    private Set<Gear> axialGears = new HashSet<Gear>();
    
    /**
     * Create a gear with n number of teeth (per revolution), and radius r. 
     * Implicitly sets radius-teeth ratio to n/r.
     * @param n
     *      Number of teeth this gear has.
     * @param r
     *      Radius of gear.
     * @requires n be a positive integer greater than 0, r to be positive and non-zero.
     * @throws IllegalArgumentException if n <= 0 or if r<= 0.
     */
    public Gear(int n, double r) throws IllegalArgumentException {
        if(n <= 0){
            throw new IllegalArgumentException("ERROR: Teeth number must be a positive whole number (n>0).");
        }
        if(r <= 0){
            throw new IllegalArgumentException("ERROR: Radius must be a positive number!");
        }
        teethNumber = n;
        radius = r;
    }
    
    /**
     * Create a gear with n number of teeth (per revolution). 
     * Since r is unspecified, set radius to be equal to n.
     * @param n
     *      Number of teeth this gear has.
     * @requires n be a positive integer greater than 0.
     * @throws IllegalArgumentException if n <= 0.
     */
    public Gear(int n) throws IllegalArgumentException{
        this(n, n);
    }
    
    
    /**
     * Returns the number of teeth this gear has
     * @return teethNumber
     */
    public int getTeeth(){
        return teethNumber;
    }
    
    /**
     * Returns the frequency at which this gear is being spun.
     * @return frequency
     */
    public double getFrequency(){
        return frequency;
    }
    
    /**
     * Returns the rate of teeth that are in contact. I.e. how many teeth are passing a point at a time.
     * @return teeth speed
     */
    public double getTeethSpeed(){
        return frequency*teethNumber;
    }
    
    /**
     * Returns the torque this gear is under
     * @return torque
     */
    public double getTorque(){
        return torque;
    }
    
    /**
     * Returns the radius of the gear
     * @return radius
     */
    public double getRadius(){
        return radius;
    }
    
    /**
     * Returns the period of revolution
     * @return
     */
    public double getPeriod(){
        return 1/frequency;
    }
    
    /**
     * Returns the force at the edge of the gear
     * @return force
     */
    public double getEdgeForce(){
        return getForceAtRadialDistance(radius);
    }
    
    /**
     * Returns the force at the given radial distance from gear
     * @param r
     *      Radial distance to getForce
     * @requires r>0
     * @return force
     */
    public double getForceAtRadialDistance(double r){
        if(r <= 0){
            throw new IllegalArgumentException("ERROR: Given radius must be positive!");
        }
        return getTorque()/r;
    }
    
    /**
     * Applies a force to the edge of this gear. Creates a torque on the axis, which affects other gears.
     * @param f
     *      Force to be applied
     * @modifies force, torque, axialGears
     */
    public void applyEdgeForce(double f){
        applyForce(f, radius);
    }
    
    /**
     * Applies a force to the edge of this gear at the given radial distance. 
     * Creates a torque on the axis, which affects other gears.
     * @param f
     *      Force to be applied
     * @param r
     *      Radial distance to apply force
     * @modifies force, torque, axialGears
     */
    public void applyForce(double f, double r){
        applyTorque(f*r);
    }
    
    /**
     * Set the teethSpeed at the given speed.
     * @param v
     */
    public void setTeethSpeed(double v){
        setFrequency(v/teethNumber);
    }
    
    /**
     * Sets this specific gear in motion, at the given frequency. Spins any connected gears properly.
     * User can interpret units and sign convention in any way. 
     * However, for good usage, use Hz for frequency, and follow the Right-Hand-Rule for signs.
     * @param f
     *      frequency to spin this gear in.
     * @modifies frequency of this gear, and the frequency of any connected gears
     */
    public void setFrequency(double f){
        frequency = f;
        synchronize(this);
    }
    
    /**
     * Applies the given torque on the gear, affecting any connected gears properly.
     * @param t
     *      Torque to be applied
     * @modifies torque of this gear, and the torque of any connected gears
     */
    public void applyTorque(double t){
        torque = t;
        synchronize(this);
    }
    
    /**
     * For use with setFrequency(double f) or applyTorque(double t), recursive implementation.
     * This ensures all gears are following Gear Ratio rules.
     * Gear Ratios:
     *      Gears on same axis share the same frequency and torque
     *      Adjacent connected Gears have the same but reversed toothSpeed, where toothSpeed = frequency*teethNumber.
     */
    private void synchronize(Gear origin){
        for(Gear g : connectedGears){
            if(!(g == origin)){
                double gFrequency = -this.getTeethSpeed()/g.getTeeth();
                double gTorque = -(g.getTeeth()*torque)/teethNumber;
                g.frequency = gFrequency;
                g.torque = gTorque;
                g.synchronize(this);
                
            }
        }
        
        origin = this;
        
        for(Gear g: axialGears){
            if(!(g == origin)){
                if(!(g.getTorque() == origin.getTorque() && g.getFrequency() == origin.getFrequency())){
                    g.frequency = origin.getFrequency();
                    g.torque = origin.getTorque();
                    g.synchronize(g);
                }
                
            }
            
        }
        
    }
    
    /**
     * Connects two given gears together. The gear that has higher torque has priority, and the lower
     * torque gear synchronizes with the higher torque gear.
     * @param a
     *      Gear to be connected.
     * @param b
     *      Gear to be connected.
     * @modifies a, b
     *      Both gears are now connected, and frequencies and torques are dependent.
     */
    public static void connectGears(Gear a, Gear b){
        if(b.getTorque() > a.getTorque()){
            b.connectGear(a);
        }
        else{
            a.connectGear(b);
        }
    }
    
    /**
     * Connects the given gear to this gear, with this gear having frequency priority (even if it is not moving).
     * @param b
     *      Gear to be connected
     * @modifies connectedGears, b
     *      Both gears are now connected, and frequencies and torques are dependent.
     */
    public void connectGear(Gear b){
        if(!this.sharesAxisWith(b)){
            connectedGears.add(b);
            b.connectedGears.add(this);
            setFrequency(frequency);
        }
        else{
            System.out.println("ERROR: Gear shares axis! Must be a separate gear.");
        }
    }
    
    /**
     * Adds the given gear onto the same axis. Synchronizes the torque and spin frequency.
     * @param a
     *      Gear to add onto axis
     * @modifies axialGears
     */
    public void addOnToAxis(Gear a){
        if(!this.isConnectedTo(a)){
            axialGears.add(a);
            a.axialGears.add(this);
            synchronize(this);
        }
        else{
            System.out.println("ERROR: Gear is already connected to system! Must be an independent gear");
        }
        
    }
    
    /**
     * Returns the toString, constructed from the fields
     */
    @Override
    public String toString(){
        return ("Teeth="+ teethNumber + ", Frequency="+frequency+", Torque="+ torque);
    }
    
    /**
     * Returns whether this gear is connected to Gear a
     * @param a
     *      Gear to check for
     * @return
     */
    public boolean isConnectedTo(Gear a){
        return connectedGears.contains(a);
    }
    
    /**
     * Returns whether this gear shares the same axis as Gear a
     * @param a
     *      Gear to check for
     * @return
     */
    public boolean sharesAxisWith(Gear a){
        return axialGears.contains(a);
    }
    
    public void createAxis(Set<Gear> gears){
        Gear a = null;
        for(Gear b : gears){
            if(a != null){
                a.addOnToAxis(b);
            }
            else{
                a = b;
            }
        }
    }

}
