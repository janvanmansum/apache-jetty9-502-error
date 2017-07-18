package nl.knaw.dans.test;

import sun.net.www.http.HttpClient;

import java.net.URI;

public class Client {
  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("Usage: ./post-file.sh <file-name>");
      System.exit(1);
    }

    URI uri = new URI("http://192.168.33.32/");
    HttpClient http = HttpClient.New(uri.toURL());







  }
}
