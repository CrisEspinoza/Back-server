package com.example.Entities;


import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Verificador {

    public Status buscar (String id){

//        ConfigurationBuilder builder = new ConfigurationBuilder();
//        builder.setOAuthConsumerKey("lAB6e3xVkac71t6iTspvqPc0f");
//        builder.setOAuthConsumerSecret("WpHXugHsoaGzYqSkCcK7M0PXiT7AyKgfCQMT2hhtNskwjVYeGQ");
//        Configuration configuration = builder.build();
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("lAB6e3xVkac71t6iTspvqPc0f")
                .setOAuthConsumerSecret("WpHXugHsoaGzYqSkCcK7M0PXiT7AyKgfCQMT2hhtNskwjVYeGQ")
                .setOAuthAccessToken("733665170099179521-2KUWF8hPPEKpX7gKubCAANTFlenIYLw")
                .setOAuthAccessTokenSecret("1kxJB2DBk4O0Ux0s7Z6g7xaX4HoSgK1TJhzul9OY6wOqb");

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();


        //twitter.setOAuthConsumer(consumerKey, consumerSecret);
       // AccessToken accessToken = new AccessToken("733665170099179521-2KUWF8hPPEKpX7gKubCAANTFlenIYLw","1kxJB2DBk4O0Ux0s7Z6g7xaX4HoSgK1TJhzul9OY6wOqb");
        try {
            Status status = twitter.showStatus(Long.parseLong(id));
            if (status == null) { //
                return status;
            } else {
              return  status;
            }
        } catch (
                TwitterException e) {
            System.err.print("Failed to search tweets: " + e.getMessage());
            return null;
            // e.printStackTrace();
            // DON'T KNOW IF THIS IS THROWN WHEN ID IS INVALID
        }
    }

}
