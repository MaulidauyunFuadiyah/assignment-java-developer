package org.ctcorp.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@RegisterForReflection
public class Post {
	public Long id;
	public String title;
	public String content;
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static Multi<Post> getPost() {
		return post;
	}

	public static void setPost(Multi<Post> post) {
		Post.post = post;
	}

	public static Multi<Post> post;
	
	public Post() {
		
	}
	
	public Post(Long id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}
	

	public static Multi<Post> findAll(PgPool client) {
		System.out.println("start-------------------------------------------------");
		try {
			post = client.query("SELECT id, title, content FROM table_post ORDER BY title ASC").execute()
					.onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
					.onItem().transform(Post::from);
			System.out.println("post -----------------------------------------------" + post.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return post;
//		return client.query("SELECT id, title, content FROM table_post ORDER BY title ASC").execute()
//				.onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
//				.onItem().transform(Post::from);
	}
	
	public static Uni<Post> findById(PgPool client, Long id) {
        return client.preparedQuery("SELECT id, title, content FROM table_post WHERE id = $1").execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Uni<Long> save(PgPool client) {
        return client.preparedQuery("INSERT INTO table_post (title) VALUES ($1), (content) VALUES ($2) RETURNING id").execute(Tuple.of(title, content))
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public Uni<Boolean> update(PgPool client) {
        return client.preparedQuery("UPDATE table_post SET title = $1 WHERE id = $2").execute(Tuple.of(title, id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static Uni<Boolean> delete(PgPool client, Long id) {
        return client.preparedQuery("DELETE FROM table_post WHERE id = $1").execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    private static Post from(Row row) {
        return new Post(row.getLong("id"),row.getString("title"), row.getString("content"));
    }
}
