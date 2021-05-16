package org.ctcorp.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ctcorp.entity.Post;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;

@Path("post")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostService {
	private final PgPool client;
	
	public PostService(PgPool client) {
		this.client = client;
	}
	
	private void initdb() {
        client.query("DROP TABLE IF EXISTS table_post").execute()
                .flatMap(r -> client.query("CREATE TABLE table_post (id SERIAL PRIMARY KEY, title TEXT NOT NULL, content TEXT NOT NULL)").execute())
                .flatMap(r -> client.query("INSERT INTO table_post (title) VALUES ('Kiwi')").execute())
                .flatMap(r -> client.query("INSERT INTO table_post (title) VALUES ('Durian')").execute())
                .flatMap(r -> client.query("INSERT INTO table_post (content) VALUES ('Pomelo')").execute())
                .flatMap(r -> client.query("INSERT INTO table_post (content) VALUES ('Lychee')").execute())
                .await().indefinitely();
    }
	
	@GET
	public Multi<Post> get() {
		return Post.findAll(client);
	}
}
