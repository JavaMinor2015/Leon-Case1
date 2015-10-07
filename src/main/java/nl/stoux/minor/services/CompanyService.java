package nl.stoux.minor.services;

import nl.stoux.minor.access.StudentHandler;
import nl.stoux.minor.domain.Company;
import nl.stoux.minor.services.base.BaseService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by Stoux on 07/10/2015.
 */
@Path(Company.URL)
public class CompanyService extends BaseService {

    @GET
    @Path("/{id}")
    public Response getStudent(@PathParam("id") int companyId) {
        return get(
                StudentHandler::new,
                StudentHandler::findCompany,
                companyId
        );
    }

}
