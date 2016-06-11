package org.artJava.protocol.http.resource;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.artJava.protocol.app.NodeMaster;
import org.artJava.protocol.http.service.NodeMasterHttpService;

@Path("executors/{executor_UID}/command/{command}")
public class HExecutorCommand {

	@PUT
	public Response put(@PathParam("executor_UID") String executorUID, @PathParam("command") String command, String config) {
		NodeMaster master = NodeMasterHttpService.getInstance().getMaster();
		switch (command) {
		case "update":
			master.updateExecutor(executorUID,config);
			break;
		default:
			return WebApplicationExceptionMapper.response(Response.Status.NOT_FOUND);
		}
		return WebApplicationExceptionMapper.response(Response.Status.OK);
	}

}
