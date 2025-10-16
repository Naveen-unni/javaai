package com.example.bloodtracking;

import com.example.bloodtracking.dao.DatabaseConnection;
import com.example.bloodtracking.dao.DonorDAO;
import com.example.bloodtracking.model.Donor;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.nio.charset.StandardCharsets; 
import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        DonorDAO donorDAO = new DonorDAO(); // only define once

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/public";
                staticFiles.location = Location.CLASSPATH;
                staticFiles.precompress = false;
            });
        }).start(7000);

        // Serve index.html
        app.get("/", ctx -> {
            byte[] bytes = Main.class.getResourceAsStream("/public/index.html").readAllBytes();
            String html = new String(bytes, StandardCharsets.UTF_8);
            ctx.html(html);
        });

        // Donor APIs
        app.get("/donors", ctx -> ctx.json(donorDAO.getAllDonors()));

        app.post("/donors", ctx -> {
            Donor donor = ctx.bodyAsClass(Donor.class);
            donorDAO.addDonor(donor);
            ctx.status(201);
        });

        app.get("/donors/search", ctx -> {
            String bloodGroup = ctx.queryParam("bloodGroup");
            String location = ctx.queryParam("location");
            ctx.json(donorDAO.searchDonors(bloodGroup, location));
        });

        // AI Analysis Endpoint - only ONE lambda, no redeclaration
        app.get("/analyze", ctx -> {
            // use the existing donorDAO
            var donors = donorDAO.getAllDonors();

            var gson = new Gson();
            String donorJson = gson.toJson(java.util.Map.of("donors", donors));

            try {
                java.net.URI uri = java.net.URI.create("http://localhost:5000/analyze");
                java.net.URL url = uri.toURL();

                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                try (java.io.OutputStream os = conn.getOutputStream()) {
                    os.write(donorJson.getBytes());
                }

                java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(conn.getInputStream())
                );
                String response = br.lines().collect(java.util.stream.Collectors.joining());
                ctx.result(response);

            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("{\"error\":\"AI service not available\"}");
            }
        });

        // 404 handler
        app.error(404, ctx -> ctx.status(404).json(new ErrorResponse("Not found")));
    }

    private static class ErrorResponse {
        private final String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}
