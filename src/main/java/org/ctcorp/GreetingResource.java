package org.ctcorp;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GreetingResource {
private final PgPool client;
	
	public GreetingResource(PgPool client) {
		this.client = client;
	}
	
//	private void initdb() {
//        client.query("DROP TABLE IF EXISTS table_post").execute()
//                .flatMap(r -> client.query("CREATE TABLE table_post (id SERIAL PRIMARY KEY, title TEXT, content TEXT)").execute())
//                .flatMap(r -> client.query("INSERT INTO table_post (title) VALUES ('Kiwi')").execute())
//                .flatMap(r -> client.query("INSERT INTO table_post (title) VALUES ('Durian')").execute())
//                .flatMap(r -> client.query("INSERT INTO table_post (content) VALUES ('Pomelo')").execute())
//                .flatMap(r -> client.query("INSERT INTO table_post (content) VALUES ('Lychee')").execute())
//                .await().indefinitely();
//    }
	
	@GET
	public Multi<Post> get() {
		return Post.findAll(client);
	}
	
	@GET
    @Path("{id}")
    public Uni<Response> getSingle(Long id) {
        return Post.findById(client, id)
                .onItem().transform(post -> post != null ? Response.ok(post) : Response.status(Status.NOT_FOUND))
                .onItem().transform(ResponseBuilder::build);
    }
	
	@POST
	public Uni<Response> create(Post post) {
		 return post.save(client)
	                .onItem().transform(id -> URI.create("/post/" + id))
	                .onItem().transform(uri -> Response.created(uri).build());
	}
	
	

//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String hello() {
//        return "Hello RESTEasy";
//    }
}