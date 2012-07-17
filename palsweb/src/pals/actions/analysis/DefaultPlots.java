package pals.actions.analysis;

import java.util.ArrayList;
import java.util.List;

import pals.actions.UserAwareAction;

public class DefaultPlots extends UserAwareAction
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
