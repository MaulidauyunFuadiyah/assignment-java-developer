package org.ctcorp.connection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.pgclient.PgPool;

@ApplicationScoped
public class DBInit {
	
	private final PgPool client;
	private final boolean schemaCreate;
	
	public DBInit(PgPool client, @ConfigProperty(name = "myapp.schema.create", defaultValue = "true") boolean schemaCreate) {
        this.client = client;
        this.schemaCreate = schemaCreate;
    }

    void onStart(@Observes StartupEvent ev) {
        if (schemaCreate) {
            initdb();
        }
    }

    private void initdb() {
        client.query("DROP TABLE IF EXISTS table_post").execute()
                .flatMap(r -> client.query("CREATE TABLE table_post (id SERIAL PRIMARY KEY, title TEXT, content TEXT)").execute())
                .flatMap(r -> client.query("INSERT INTO table_post (title) VALUES ('Kiwi')").execute())
                .flatMap(r -> client.query("INSERT INTO table_post (title) VALUES ('Durian')").execute())
                .flatMap(r -> client.query("INSERT INTO table_post (content) VALUES ('Pomelo')").execute())
                .flatMap(r -> client.query("INSERT INTO table_post (content) VALUES ('Lychee')").execute())
                .await().indefinitely();
    }

}
