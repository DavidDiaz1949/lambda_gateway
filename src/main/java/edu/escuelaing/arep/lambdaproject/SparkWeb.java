/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arep.lambdaproject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import static spark.Spark.*;
import spark.Request;
import spark.Response;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author bucar
 */
public class SparkWeb {
    public static void main(String[] args) {
        port(getPort());
        get("/cuadrado", (req, res) -> inputDataPage(req, res));
        get("/results", (req, res) -> resultsPage(req, res));
    }

    /**
     * Metodo que permite recibir los datos a los que el usuario desea sacar la
     * desviacion estandar y el promedio 
     * @param req
     * @param res
     * @return
     */
    private static String inputDataPage(Request req, Response res) {
        String pageContent
                = "<!DOCTYPE html>"
                + "<html>"
                + "<body>"
                + "<h2>Calculadora de Cuadrados</h2>"
                + "<form action=\"/results\">"
                + "  Obtener el cuadrado de: <br>"
                + "  <br>"
                + "  <input type=\"text\" name=\"value\" value=\"p.e 4\">"
                + "  <br><br>"
                + "  <input type=\"submit\" value=\"Calcular\">"
                + "</form>"
                + "</body>"
                + "</html>";
        return pageContent;
    }

    /**
     * Metodo que permite obtener los resultados de la desviacion y promedio de los datos ingresados
     * @param req
     * @param res
     * @return
     */
    private static String resultsPage(Request req, Response res) throws IOException {
        String recibida = req.queryParams("value");
        try{
            URL url = new URL("https://4odpwzx0k7.execute-api.us-east-1.amazonaws.com/tutorial?value="+
                        req.queryParams("value"));
            try {
                FileWriter fileWriter = new FileWriter("src/main/resources/result.html");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine = null;
                while ((inputLine= bufferedReader.readLine()) != null) {
                    fileWriter.write(inputLine);
                }
                fileWriter.close();
            } catch (IOException e) {
                System.err.println("Error de petición: "+ e.getMessage());
            }
        } catch (MalformedURLException e) {
            System.err.println("La URL es invalida");
        }

        String page = FileUtils.readFileToString(
                new File("src/main/resources/result.html"), StandardCharsets.UTF_8);
        return page;
    }

    /**
     * This method reads the default port as specified by the PORT variable in
     * the environment.
     *
     * Heroku provides the port automatically so you need this to run the
     * project on Heroku.
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
