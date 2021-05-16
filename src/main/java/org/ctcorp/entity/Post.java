package org.ctcorp.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

//@Entity
//@Table(name = "table_post")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@RegisterForReflection
public class Post {
	private Long id;
	private String title;
	private String content;
//	private List<Tag> tags = new ArrayList<Tag>();
	
//	@Id
//	@GeneratedValue
//	@Column(name = "id")
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	
//	@Column(name = "title")
//	public String getTitle() {
//		return title;
//	}
//	public void setTitle(String title) {
//		this.title = title;
//	}
//	
//	@Column(name = "content")
//	public String getContent() {
//		return content;
//	}
//	public void setContent(String content) {
//		this.content = content;
//	}
//	
//	@ManyToMany
//	@JoinTable(name = "table_post_tag", joinColumns = @JoinColumn(name = "id_post", referencedColumnName = "id_post"),
//    inverseJoinColumns = @JoinColumn(name = "id_tag", referencedColumnName = "id_tag"))
//	public List<Tag> getTags() {
//		return tags;
//	}
//	public void setTags(List<Tag> tag) {
//		this.tags = tag;
//	}
	
	public Post() {
		
	}
	
	public Post(Long id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}
	
//	public Content(String content) {
//		this.content = content;
//	}
//	
	public static Multi<Post> findAll(PgPool client) {
		return client.query("SELECT id, title, content FROM table_post ORDER BY title ASC").execute()
				.onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
				.onItem().transform(Post::from);
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
