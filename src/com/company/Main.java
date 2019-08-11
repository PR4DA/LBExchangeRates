package com.company;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        String valiuta;
        String cvaliuta;
        String data;
        String cdata;
        String url;
        String xml;
        boolean quite = false;
        while (!quite) {
            menu();
            System.out.println(">> ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 0:
                    quite = true;
                    break;
                case 1:
                    System.out.print("Enter currency, example: EU >> ");
                    valiuta = scanner.nextLine();
                    System.out.print("Enter date, example: 2019-01-01 >> ");
                    data = scanner.nextLine();
                    url = "https://www.lb.lt/webservices/FxRates/FxRates.asmx?op=getFxRates";
                    xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" + "  <soap12:Body>\n" + "    <getFxRates xmlns=\"http://www.lb.lt/WebServices/FxRates\">\n" + "      <tp>" + valiuta + "</tp>\n" + "      <dt>" + data + "</dt>\n" + "    </getFxRates>\n" + "  </soap12:Body>\n" + "</soap12:Envelope>";
                    SendRequests.request(url, xml);
                    break;
                case 2:
                    System.out.print("Enter currency, example: EU >> ");
                    valiuta = scanner.nextLine();
                    System.out.print("Enter comparable currency, example: USD >> ");
                    cvaliuta = scanner.nextLine();
                    System.out.print("Enter from date, example: 2019-01-01 >> ");
                    data = scanner.nextLine();
                    System.out.print("Enter to date, example: 2019-08-11 >> ");
                    cdata = scanner.nextLine();
                    url = "https://www.lb.lt/webservices/FxRates/FxRates.asmx?op=getFxRatesForCurrency";
                    xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" + "  <soap12:Body>\n" + "    <getFxRatesForCurrency xmlns=\"http://www.lb.lt/WebServices/FxRates\">\n" + "      <tp>" + valiuta + "</tp>\n" + "      <ccy>" + cvaliuta + "</ccy>\n" + "      <dtFrom>" + data + "</dtFrom>\n" + "      <dtTo>" + cdata + "</dtTo>\n" + "    </getFxRatesForCurrency>\n" + "  </soap12:Body>\n" + "</soap12:Envelope>";
                    SendRequests.request(url, xml);
                    break;
                case 3:
                    url = "https://www.lb.lt/webservices/FxRates/FxRates.asmx?op=getCurrencyList";
                    xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" + "  <soap12:Body>\n" + "    <getCurrencyList xmlns=\"http://www.lb.lt/WebServices/FxRates\" />\n" + "  </soap12:Body>\n" + "</soap12:Envelope>";
                    SendRequests.request(url, xml);
                    break;
            }

        }
    }

    private static void menu() {
        System.out.println("0-exit\n1-getFxRates\n2-getFxRatesForCurrency\n3-getCurrencyList");
    }

    private static class SendRequests {

        private static void request(String url, String xml) throws IOException {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/soap+xml");
            connection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(xml);
            dataOutputStream.flush();
            dataOutputStream.close();
            String rStatus = connection.getResponseMessage();
            System.out.println(rStatus);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            StringBuilder response = new StringBuilder();
            while ((input = bufferedReader.readLine()) != null) {
                response.append(input);
            }

            bufferedReader.close();

            System.out.println("Response: " + response.toString());
        }

    }

}
