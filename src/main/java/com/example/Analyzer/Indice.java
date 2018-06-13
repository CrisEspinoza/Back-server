package com.example.Analyzer;

import com.example.Entities.Tweet;
import com.mongodb.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;


public class Indice{
    MongoCredential credential = MongoCredential.createCredential("TbdG7", "TBDG7", "antiHackers2.0".toCharArray());
    MongoClient mongoo = new MongoClient(new ServerAddress("128.199.185.248", 18117), Arrays.asList(credential));
    DB db = mongoo.getDB("TBDG7");
    DBCollection collection = db.getCollection("futbol");
    DBCursor cursor = collection.find();

    public void indexar() {
        try {
            Directory dir = FSDirectory.open(Paths.get("index/"));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(OpenMode.CREATE);
            //// Add new documents to an existing index: OpenMode.CREATE_OR_APPEND

            IndexWriter writer = new IndexWriter(dir, iwc);
            String ruta = "libros/";
            while (cursor.hasNext()) {

                Document doc = null;
                DBObject elemento = cursor.next();

                doc = new Document();
                // aca se deciden que elementos se quieren guardar en el indice
                // los  StringField son mas orientados a informacion
                // los textfield es lo que se  tokenizara
                doc.add(new StringField("id", elemento.get("_id").toString(), Field.Store.YES));
                doc.add(new StringField("name", elemento.get("name").toString(), Field.Store.YES));
                doc.add(new StringField("followers", elemento.get("followers").toString(), Field.Store.YES));
                doc.add(new TextField("text", elemento.get("text").toString(), Field.Store.YES));
                if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                    //System.out.println("Indexando el archivo: " + elemento.get("_id") + "con texto" + elemento.get("text"));
                    writer.addDocument(doc);
                } else {
                    writer.updateDocument(new Term("text" + elemento.get("text")), doc);
                }


            }
            writer.close();
        } catch (IOException ioe) {

        }
    }


    public  ArrayList<Tweet>  buscar(String equipo){
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        try {


            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("index/")));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();

            QueryParser parser = new QueryParser("text", analyzer);
            Query query = parser.parse(equipo);

            TopDocs results = searcher.search(query,220000);
            ScoreDoc[] hits = results.scoreDocs;
            System.out.println(hits.length);
            for(int i = 0; i < hits.length; i++) {
                Document doc = searcher.doc(hits[i].doc);
                //String id_salida = doc.get("id");
                //System.out.println(id_salida);
                String text_salida = doc.get("text");
                Tweet tw= new Tweet(doc.get("text"),doc.get("name"),doc.get("followers"));
                tweets.add(tw);
            }
            reader.close();


        }
        catch(IOException ioe) {

        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Datos encontrado para esta consulta"+tweets.size());

        return tweets;
    }
}
