//Template for Behaviour file
//This file is used by code generator
package patada;

//IMPORT SECTION 
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Queue;
import java.util.Vector;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.Message;
import teams.ucmTeam.RobotAPI;

public class patadaBehaviour extends Behaviour {
	/**
	 * Table for manage all state of the HFMS
	 */
	private Vector<State> states;
	/**
	 * Stack for manage the current state and the hierarchical child state of the current state  
	 */
	private Vector<State> stack;
	
	
	@Override
	public void configure() {
		//Call Generate the list of states from HFSM
		GenerateStates();
	}

	/**
	 * Put the code that execute on each iteration of simulation
	 */
	@Override
	public int takeStep() {
		try 
		{
			this.getClass().getMethod(stack.lastElement().actions, (Class[]) null).invoke(this, (Object[]) null);
		} 
		catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (SecurityException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
		}
		try 
		{
			int positionStack = 0;
			while(positionStack < stack.size())
			{
				State e = stack.get(positionStack);
				if (e.transitions != null) 
				{
					for (int j = 0; j < e.transitions.length; j++) 
					{
						if ((Boolean) this.getClass().getMethod(e.transitions[j].condition, (Class[]) null).invoke(this, (Object[]) null)) 
						{
							// Remove state of the stack 
							for (int k = stack.size() - 1; k >= positionStack; k--) {
								stack.remove(k);
							}
							// Put new state over the stack
							stack.add(states.get(e.transitions[j].state));
						}
					}
				}
				positionStack++;
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (SecurityException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
		}
		
		return myRobotAPI.ROBOT_OK;
	}

	@Override
	public void onInit(RobotAPI r) {
		//Initialize the stack with the first state 
		stack = new Vector<State>();
		stack.add(states.get(0));
	}

	@Override
	public void end() {	}

	@Override
	public void onRelease(RobotAPI r) { }
	
	//Create the table of State. It has been generated by code generator 
	private void GenerateStates()
	{
		states = new Vector<State>();
		State e;
		
		//State 0 : Node  1 
		e = new State();
		e.transitions = new Transition[1];
		e.transitions[0] = new Transition();
		e.transitions[0].condition = "cond_0_0";
		e.transitions[0].state = 1;
		e.actions = "acc_0";
		states.add(e);

		//State 1 : Node  2 
		e = new State();
		e.transitions = new Transition[1];
		e.transitions[0] = new Transition();
		e.transitions[0].condition = "cond_1_0";
		e.transitions[0].state = 0;
		e.actions = "acc_1";
		states.add(e);


	}
	
	//Conditions and actions that was building based in the graph of de HFMS of the tool editor
	//Conditions for State 0 : Node  1
	public Boolean cond_0_0(){
		return ((myRobotAPI.canKick() == true));
	}

	public void acc_0(){
		Iralapelota();
	}

//Conditions for State 1 : Node  2
	public Boolean cond_1_0(){
		return ((myRobotAPI.canKick() == false));
	}

	public void acc_1(){
		Patear();
	}


	
	//Code of basic behaviours that was building based in basic behaviours of de game model
		public void LeadBallToGoal() { 

		myRobotAPI.alignedToBallandGoal();
myRobotAPI.kick();

	}
 	public void BlockForward() { 

		myRobotAPI.blockForward();

	}
 	public void GoToCenter() { 

		Vec2 destino = new Vec2(0.0, 0.0);
destino.sub(myRobotAPI.getPosition());
myRobotAPI.setSteerHeading(destino.t);
myRobotAPI.setSpeed(1.0);

	}
 	public void Iralapelota() { 

		// Sacamos el �ngulo de la pelota
double angle = myRobotAPI.getBall().t;
// Cambiamos el �ngulo del robot
myRobotAPI.setSteerHeading(angle);
// Ponemos la velocidad
myRobotAPI.setSpeed(1.0);

	}
 	public void Unblock() { 

		myRobotAPI.avoidCollisions();

	}
 	public void Wait() { 

		myRobotAPI.setSpeed(0.0);

	}
 	public void CoverGoal() { 

		Vec2 dest = new Vec2(myRobotAPI.getOurGoal());
dest.add(myRobotAPI.getBall());
myRobotAPI.setSteerHeading(dest.t);
myRobotAPI.setSpeed(1.0);

	}
 	public void Patear() { 

		myRobotAPI.kick();

	}
 	public void WalkTowardsGoal() { 

		myRobotAPI.setSteerHeading(myRobotAPI.getOurGoal().t);
myRobotAPI.setSpeed(1.0);

	}
 	public void BlockGoalkeeper() { 

		myRobotAPI.blockGoalKeeper();

	}
 
	
	/**
	 * Strut to define a Transition of the HFMS
	 * @author Jorge
	 *
	 */
	
	private class Transition {
		String condition;
		int state;
	}
	/**
	 * Struct to define a State of the HFMS
	 * @author Jorge
	 *
	 */
	private class State {
		Transition[] transitions;
		String actions;
	}
	
	/**
	 * The equals method will be used to check if two behaviours are equal (doh).
	 * 
	 * When a robot is executing a behaviour in SBT and then the TM decides to change
	 * it for another, this method will be consulted prior to change. When behaviours 
	 * change they are reinitialized (the state machine goes back to the initial state).
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return obj.getClass().equals(getClass());
	}

}