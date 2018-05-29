package com.example.Analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import opennlp.tools.doccat.BagOfWordsFeatureGenerator;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class Classifier {
    private DoccatModel model;
    private DocumentCategorizerME categorizer;

    @Autowired
    private ResourceLoader resourceLoader;

    public DoccatModel getModel() {
        return model;
    }

    public void setModel(DoccatModel model) {
        this.model = model;
    }

    @PostConstruct
    public void train() {
        InputStreamFactory dataIn=null;
        try {
            FeatureGenerator[] def = { new BagOfWordsFeatureGenerator() };
            DoccatFactory factory=new DoccatFactory(def);

            Resource resource=resourceLoader.getResource("classpath:tweets.txt");
            File tweets=resource.getFile();

            dataIn=new MarkableFileInputStreamFactory(tweets);
            ObjectStream<String> lineStream=new PlainTextByLineStream(dataIn,StandardCharsets.UTF_8);
            ObjectStream<DocumentSample> sampleStream=new DocumentSampleStream(lineStream);

            TrainingParameters params = TrainingParameters.defaultParams();
            params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(0));
            params.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(100));

            this.model=DocumentCategorizerME.train("es",sampleStream,params,factory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.categorizer=new DocumentCategorizerME(this.model);
    }
    public HashMap<String,Double> classify(String tweet){
//        String[] words=tweet.replaceAll("[^A-Za-z]"," ").split(" ");
//        ArrayList<String> wordF = new ArrayList<String>();
//        for (String i: words) {
//            if(!(i.equals(""))){
//                wordF.add(i);
//                System.out.println("*******"+i);
//            }
//
//        }
//        String[] wordsAnalizar = new String[wordF.size()];
//        wordsAnalizar = wordF.toArray(wordsAnalizar);
//
//        double[] prob=categorizer.categorize(words);
//        System.out.println("*******"+words[0]);
//        HashMap<String,Double> result=new HashMap<String,Double>();
//        for(int i=0;i<categorizer.getNumberOfCategories();i++){
//            result.put(categorizer.getCategory(i),prob[i]);
//        }
//        return result;

        String[] words=tweet.replaceAll("[^A-Za-z]"," ").split(" ");
        double[] prob=categorizer.categorize(words);

        HashMap<String,Double> result=new HashMap<String,Double>();
        for(int i=0;i<categorizer.getNumberOfCategories();i++){
            result.put(categorizer.getCategory(i),prob[i]);
        }
        return result;
    }
}
