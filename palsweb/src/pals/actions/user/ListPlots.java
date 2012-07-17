package pals.actions.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pals.actions.UserAwareAction;
import pals.entity.AnalysisType;
import pals.entity.Analysis;
import pals.entity.DataSet;
import pals.entity.DataSetVersion;
import pals.entity.Model;
import pals.entity.ModelOutput;
import pals.service.AnalysisServiceInterface;
import pals.service.UserServiceInterface;


public class ListPlots extends UserAwareAction 
{
	private Integer		filterModelOutputId = -1;
	private Integer     filterModelId = -1;

	public Integer getFilterModelOutputId() {
		return filterModelOutputId;
	}

	public void setFilterModelOutputId(Integer filterModelOutputId) {
		this.filterModelOutputId = filterModelOutputId;
	}

	public Integer getFilterModelId() {
		return filterModelId;
	}

	public void setFilterModelId(Integer filterModelId) {
		this.filterModelId = filterModelId;
	}
}
