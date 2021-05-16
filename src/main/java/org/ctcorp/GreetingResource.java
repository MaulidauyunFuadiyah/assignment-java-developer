package org.ctcorp;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.ctcorp.entity.Post;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;

@Path("/api")
public class GreetingResource {
private final PgPool client;
	
	public GreetingResource(PgPool client) {
		this.client = client;
	}
	
	@GET
	@Path("/getAllPost")
	@Produces(MediaType.APPLICATION_JSON)
	public Multi<Post> get() {
		return Post.findAll(client);
	}
	
	@GET
    @Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getSingle(Long id) {
        return Post.findById(client, id)
                .onItem().transform(post -> post != null ? Response.ok(post) : Response.status(Status.NOT_FOUND))
                .onItem().transform(ResponseBuilder::build);
    }
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Uni<Response> create(Post post) {
		 return post.save(client)
	                .onItem().transform(id -> URI.create("/post/" + id))
	                .onItem().transform(uri -> Response.created(uri).build());
	}
	
	@PUT
    @Path("{id}/{title}&{content}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> update(Long id, Post post) {
        return post.update(client)
                .onItem().transform(updated -> updated ? Status.OK : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(Long id) {
        return Post.delete(client, id)
                .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }

}