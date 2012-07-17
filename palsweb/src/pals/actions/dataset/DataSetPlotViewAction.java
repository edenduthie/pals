package pals.actions.dataset;

import pals.actions.UserAwareAction;

public class DataSetPlotViewAction extends UserAwareAction
{
	Integer filterDataSetVersionId = -1;
	Integer filterDataSetId = -1;

	public Integer getFilterDataSetVersionId() {
		return filterDataSetVersionId;
	}

	public void setFilterDataSetVersionId(Integer filterDataSetVersionId) {
		this.filterDataSetVersionId = filterDataSetVersionId;
	}

	public Integer getFilterDataSetId() {
		return filterDataSetId;
	}

	public void setFilterDataSetId(Integer filterDataSetId) {
		this.filterDataSetId = filterDataSetId;
	}
}
