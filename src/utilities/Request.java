package utilities;

import java.io.Serializable;

/**
 * @author Team Cyan
 * Class responsible for creating requests to be sent to the server
 */
public class Request implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @author Team Cyan
	 * Possible instructions the Request may have
	 */
	public enum Instructions{
		STOP,
		ADD_SESSION,
		REMOVE_SESSION,
		LOAD_MAP,
		SAVE_MAP,
		START_REQUEST_MAP,
		STOP_REQUEST_MAP,
		DISCONNECT,
		CONSTRAIN_ClEANING_TIME,
		CONSTRAIN_START_TIME,
		CONSTRAIN_END_TIME, 
		SET_SPEED
	}
	
	private Instructions instruction;
	private Object operand;
	

	/**
	 * @param inst the instruction will be set to what this is from the enumeration
	 * @param op the operand will be set to this
	 */
	public Request(Instructions inst, Object op){
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
	 * @return The instruction associated with this object
	 */
	public Instructions getInstruction(){
		return instruction;
	}
	
}