package utilities;

import java.io.Serializable;

/**
 * @author Team Cyan
 * Class responsible for creating client server responses
 *
 */
public class Response implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7672858491226668941L;

	/**
	 * @author Team Cyan
	 * An enumeration of possible instruction types
	 *
	 */
	public enum Instructions{
		SESSIONS_LIST,//
		EXECUTING_SESSION,//
		DROPPED_SESSION,//
		ERROR,
		NEW_MAP,//		
		SAVE_MAP,//
		SESSION_TERMINATED,//
		
		NEW_SESSION,//
		COLLISIONS_DETECTED,//
		STALLED,//
		DIRECTION_CHANGED,//
		UPDATE_TILE,//
		ROBOT_MOVED,
	}
	
	private Instructions instruction;
	private Object operand;
	

	/**
	 * Constructor
	 * @param inst this will be set to the instruction of this object
	 * @param op this will be set to the operand of this object
	 */
	public Response(Instructions inst, Object op){
		this.instruction = inst;
		this.operand = op;
	}
	
	/**
	 * @return the operand Object
	 */
	public Object getOperand(){
		return operand;
	}
	
	/**
	 * @return the set instruction enumeration
	 */
	public Instructions getInstruction(){
		return instruction;
	}
	
}