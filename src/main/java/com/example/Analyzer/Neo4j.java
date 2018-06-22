package com.example.Analyzer;

import com.example.Entities.Club;
import com.example.Entities.Keyword;
import com.example.Entities.Tweet;
import com.mongodb.*;
import org.neo4j.driver.v1.*;


import java.util.*;

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
        MongoCredential credential = MongoCredential.createCredential("TBDG7", "TBDG7", "Antihackers".toCharArray());
        MongoClient mongoo = new MongoClient(new ServerAddress("159.65.198.230", 18117), Arrays.asList(credential));
        DB database = mongoo.getDB("TBDG7");
        DBCollection collection = database.getCollection("futbol");

        DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", "$name")
                .append("seguidores", new BasicDBObject("$avg", "$followers")));

        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("seguidores", -1));
        DBObject limit= new BasicDBObject("$limit",1000);
        AggregationOutput output = collection.aggregate(group,sort,limit);
        int cantidad =output.hashCode();
                int i=0;
        for (DBObject result : output.results()) {
//            System.out.println(result);
            i++;
            session.run("create (a:Usuario {name:'"+limpiar(result.get("_id").toString())+"', followers:"+result.get("seguidores")+"})");
        }
//

        System.out.println("Usuarios agregados--"+i+"--"+cantidad);
        mongoo.close();
    }


    public String limpiar(String nombre){
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
        if(nombre.equals("AND Noticias")){
            nombre=nombre.replace("AND","and");
        }
        return nombre;
    }

    public void relacionarTweet( Iterable<Club>  clubs){

        Indice indice = new Indice();
       indice.indexar();
//        MongoCredential credential = MongoCredential.createCredential("TbdG7", "TBDG7", "antiHackers2.0".toCharArray());
//        MongoClient mongoo = new MongoClient(new ServerAddress("128.199.185.248", 18117), Arrays.asList(credential));
//        DB database = mongoo.getDB("TBDG7");
//        DBCollection collection = database.getCollection("futbol");
        StatementResult nodo=session.run("MATCH (a:Usuario) RETURN a.name as name ");
        ArrayList<String> registro = new ArrayList<String>();
        while(nodo.hasNext()){
            Record record = nodo.next();
            String name=  record.get("name").asString();
            registro.add(name);



        }

        System.out.println("registros listos");



        for (Club equipo: clubs) {
            int[] cantidades= new int[registro.size()];

            System.out.println("llegue aca ");

            if (equipo.getId() != 17) {
                ArrayList<Tweet> tweets;
                String busqueda = equipo.getName();
                for (Keyword apodo : equipo.getKeywords()) {
                    busqueda = busqueda + " OR " + apodo.getName_keyword();
                }

                System.out.println("%%%%% " + busqueda + "%%%%%%%");
               for (int i=0; i<registro.size();i++){
                   tweets = indice.buscarUsuario(registro.get(i),busqueda);
//                   System.out.println("%%%%% Estoy buscando tweets para "+ registro.get(i)+" y eqipo"+equipo.getName()+"%%%%%%%%%%%%%");

                   cantidades[i]=tweets.size();
               }
                System.out.println("%%%%% catidades listas $$$$");

                for (int i=0; i<registro.size();i++){
                    if(cantidades[i]>0){
                        String query = "match (a:Usuario) where a.name='" + registro.get(i) + "' "
                                + "  match (b:Club) where b.name='" + equipo.getName() + "' "
                                + "  create (a)-[r:Tweet {texto:" + cantidades[i]+ "}]->(b)";
                        session.run(query);
                    }
                }

            }
            System.out.println("equipo terminado");
        }
        System.out.println("club terminado");

    }

    public float[]  getInfluencia(String usuario,String club ){
            String query ="MATCH p=(u:Usuario)-[r:Tweet]->(c:Club) where u.name='"+limpiar(usuario)+"' and c.name='"+club+"' " +
                    "RETURN u.followers as seguidores, r.texto as cantidad";
            StatementResult nodo=session.run(query);
            float[] resultado = new float[2];
            if(nodo.hasNext()){
                Record record = nodo.next();
                float  seguidores= Float.parseFloat(String.valueOf(record.get("seguidores")));
               float cantidad= record.get("cantidad").asFloat();

                resultado[0] = seguidores;
                resultado[1]= cantidad;
                return resultado;

            }else{
                return null;
            }




    }

    private Map<String, Object> mapTriple(String key1, Object value1, String key2, Object value2,String key3, Object value3) {
        Map<String, Object> result = new HashMap<String, Object>(3);
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        return result;
    }



    public List<Map<String, Object>> getUsuariosInfluyentes(String equipo){

        List<Map<String, Object>> lista = new ArrayList<>();
        String query ="MATCH p=(u:Usuario)-[r:Tweet]->(c:Club) where  c.name='"+equipo+"' " +
                "RETURN u.name as name u.followers as seguidores, r.texto as cantidad";
        StatementResult nodo=session.run(query);

        while(nodo.hasNext())
        {
            Record record = nodo.next();
            lista.add(mapTriple("name", record.get("name").toString(), "seguidores", record.get("followers").toString(), "cantidad", record.get("cantidad").asDouble() ));
        }

        return lista;
    }
}
