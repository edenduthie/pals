package pals.dto;

public class ResponseDTO 
{
	public static String SUCCESS_RESPONSE = "Success";
	
    public String message;
    public boolean success;
    
    public ResponseDTO()
    {
    	message = SUCCESS_RESPONSE;
    	success = true;
    }
    
    public ResponseDTO(String message, boolean success)
    {
    	this.message = message;
    	this.success = success;
    }
    
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
