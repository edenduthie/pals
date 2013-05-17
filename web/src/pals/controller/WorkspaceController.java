package pals.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pals.dto.WorkspaceDTO;
import pals.entity.PalsUser;
import pals.entity.Workspace;
import pals.service.LoginStatus;
import pals.service.WorkspaceService;

public class WorkspaceController 
{
	@Autowired WorkspaceService workspaceService;
	@Autowired LoginStatus loginStatus;
	
	@RequestMapping(value="/workspaces/my", method = RequestMethod.GET)
	public @ResponseBody List<WorkspaceDTO> getMyWorkspaces() 
	{
		PalsUser user = loginStatus.getUserService();
		List<WorkspaceDTO> results = new ArrayList<WorkspaceDTO>();
		if( user == null ) return results;
		List<Workspace> workspaces = workspaceService.getMyWorkspaces(user);
		for( Workspace workspace : workspaces )
		{
			results.add(new WorkspaceDTO(workspace));
		}
		return results;
	}
}
