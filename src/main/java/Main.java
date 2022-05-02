import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static final String URL = "https://api.nasa.gov/planetary/apod?api_key=WXZTRnc2XguIJ9mI9Z0dp3DLm1AKK7lFTIm4Jqnc";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet(URL);
        CloseableHttpResponse response = httpClient.execute(request);
        NasaObj nasaObj = mapper.readValue(response.getEntity().getContent(), NasaObj.class);
        System.out.println(nasaObj);
        HttpGet request2 = new HttpGet(nasaObj.getUrl());
        CloseableHttpResponse response2 = httpClient.execute(request2);
        String[] arr = nasaObj.getUrl().split("/");
        String file = arr[6];
        HttpEntity entity = response2.getEntity();
        if (entity != null) {
            FileOutputStream fos = new FileOutputStream(file);
            entity.writeTo(fos);
            fos.close();
        }
    }
}
