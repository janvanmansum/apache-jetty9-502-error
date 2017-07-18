package nl.knaw.dans.test;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.net.URI;

public class Client {
  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: ./post-file.sh <servlet> <file-name>");
      System.exit(1);
    }

    URI uri = new URI("http://192.168.33.32/" + args[0]);
    HttpClient http = HttpClientBuilder.create().build();
    HttpPost post = new HttpPost(uri);
    post.setEntity(new FileEntity(new File(args[1])));
    HttpResponse response = http.execute(post);

    System.out.println("Response status = " + response.getStatusLine());
  }
}
