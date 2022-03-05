package dev.tarvos.xyz.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import dev.tarvos.xyz.Core;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Nyvil, 05/03/2022 at 17:07
 *
 * @author Nyvil
 * @copyright Nyvil 2022 under Apache License 2.0, unless stated otherwise
 */

@Getter
@Setter
public class DatabaseConnection {

    @Getter
    private static MongoClient client;

    private String db = Core.getCore().getDb();
    private String dbUser = Core.getCore().getDbUser();
    private String dbPwd = Core.getCore().getDbPwd();
    private String dbPort = Core.getCore().getDbPort();
    private String host = Core.getCore().getHost();


    public DatabaseConnection() {
        client = new MongoClient(new MongoClientURI(String.format("mongodb://%s:%s@%s:%s/%s?authSource=admin", dbUser, dbPwd, host, dbPort, db))); // -> authSource=admin needs to be changed if you created the account in another database
        System.out.println(client);
    }
}
