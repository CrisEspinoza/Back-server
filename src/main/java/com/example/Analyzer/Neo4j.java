package com.example.Analyzer;

import com.example.Entities.Club;
import com.example.Repositories.ClubRepository;
import com.mongodb.*;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.sql.DriverManager.println;

public class Neo4j {
    private Driver driver;
    private Session session;


    public void connect(String uri, String username, String password) {
        /*
            uriConnection = bolt://localhost
            username = neo4j
            password = root -> cambiar contraseña si usaron otra.
        */
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        this.session = driver.session();
    }

    public void disconnect() {
        session.close();
        driver.close();
    }

    public void deleteAll() {
        this.session.run("match (a)-[r]->(b) delete r");
        this.session.run("match (n) delete n");
    }

    public void  crearNodosEquipos( List<Club> equipos){

        for (Club club:equipos) {
            System.out.println("el equipo es :"+ club.getName());
            session.run("create (a:Club {name:'"+club.getName()+"'})");
        }

    }

    public void crearNodoUsuarios(){
        MongoCredential credential = MongoCredential.createCredential("TbdG7", "TBDG7", "antiHackers2.0".toCharArray());
        MongoClient mongoo = new MongoClient(new ServerAddress("128.199.185.248", 18117), Arrays.asList(credential));
        DB database = mongoo.getDB("TBDG7");
        DBCollection collection = database.getCollection("futbol");


        ArrayList<String> registro = (ArrayList<String>) collection.distinct("name");
        ArrayList<String> follows= new ArrayList<String>();
        for (String nombre :registro) {
            DBCursor cursor =collection.find(new BasicDBObject("name", nombre));
            DBObject tweet= cursor.next();
            follows.add(tweet.get("followers").toString());
            System.out.println("follow agregado");
        }
        System.out.println("Followers obtenidos");
        for(String nombre: registro){


//            if (!registro.contains(nombre)) {
//                registro.add(nombre);
                nombre=nombre.replace("'","");
                nombre=nombre.replace("/","");
                nombre=nombre.replace("\"","");
                nombre=nombre.replace("_","");
                nombre=nombre.replace("¯(ツ)¯","");
                nombre=nombre.replace("|","");
                nombre=nombre.replace("°","");
                nombre=nombre.replace("¬","");
                nombre=nombre.replace("!","");
                nombre=nombre.replace("#","");
                nombre=nombre.replace("$","");
                nombre=nombre.replace("%","");
                nombre=nombre.replace("&","");
                nombre=nombre.replace("/","");
                nombre=nombre.replace("(","");
                nombre=nombre.replace(")","");
                nombre=nombre.replace("=","");
                nombre=nombre.replace("?","");
                nombre=nombre.replace("\\","");
                nombre=nombre.replace("¡","");
                nombre=nombre.replace("¿","");
                nombre=nombre.replace("@","");
                nombre=nombre.replace("*","");
                nombre=nombre.replace("+","");
                nombre=nombre.replace("~","");
                nombre=nombre.replace("{","");
                nombre=nombre.replace("}","");
                nombre=nombre.replace("[","");
                nombre=nombre.replace("]","");
                nombre=nombre.replace(";","");
                nombre=nombre.replace(",","");
                nombre=nombre.replace(":","");
                nombre=nombre.replace(".","");
                nombre=nombre.replace("_","");
                nombre=nombre.replace("-","");
//                String seguidores= tweet.get("followers").toString();
                String seguidores ="0";
                session.run("create (a:Usuario {name:'"+nombre+"',seguidores:'"+seguidores+"'})");
//            }
        }
        mongoo.close();
    }
}
