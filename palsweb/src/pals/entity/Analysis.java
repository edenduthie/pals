package pals.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.struts2.json.annotations.JSON;

/***
 * 
 * A matching up of an <Analysis> and an <Analysable> that a produces PDF.
 * 
 * Depending on the <Analysis,> only one of modelOutputId or dataSetVerionId will be set.
 * 
 * @author Stefan Gregory
 *
 */
@Entity
public class Analysis{

	@Id @GeneratedValue
	private Integer 		id;
	
	private Character		status;
	private Date			startTime;
	private Date			endTime;
	private boolean			visible;
	
	String			errorMessage;
	
	public static final Character	STATUS_NEW = 'n';
	public static final Character	STATUS_RUNNING = 'r';
	public static final Character	STATUS_COMPLETE = 'c';
	public static final Character	STATUS_ERROR = 'e';
	
	@ManyToOne(optional=false)
    //@JoinColumn(name="analysistype_id",referencedColumnName="id",insertable=false, updatable=false)
    private AnalysisType		analysisType;
	
	@ManyToOne(optional=false)
    //@JoinColumn(name="analysable_id",referencedColumnName="id",insertable=false, updatable=false)
    public Analysable		analysable;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer analysisRunId) {
		this.id = analysisRunId;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public AnalysisType getAnalysisType() {
		return analysisType;
	}
	public void setAnalysisType(AnalysisType analysis) {
		this.analysisType = analysis;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public long retrieveTimeTaken() {
		return (endTime.getTime() - startTime.getTime())/1000;
	}
	public boolean isComplete() {
		if( status != null )
		{
		    return status.equals(STATUS_COMPLETE);
		}
		else return false;
	}
	public boolean hasError() {
		if( status != null )
		{
		    return status.equals(STATUS_ERROR);
		}
		else return false;
	}
	public Analysable getAnalysable() {
		return analysable;
	}
	public void setAnalysable(Analysable analysable) {
		this.analysable = analysable;
	}
}
