// src/main/java/com/example/bloodtracking/Main.java
package com.example.bloodtracking;

import com.example.bloodtracking.dao.DatabaseConnection;
import com.example.bloodtracking.dao.DonorDAO;
import com.example.bloodtracking.model.Donor;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.nio.charset.StandardCharsets; 

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        DonorDAO donorDAO = new DonorDAO();

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/"; // Serve static files at root
                staticFiles.directory = "/public"; // Static files in src/main/resources/public
                staticFiles.location = Location.CLASSPATH;
                // Ensure static files don't override API routes
                staticFiles.precompress = false;
            });
        }).start(7000);

        // Serve index.html for the root path directly
      app.get("/", ctx -> {
    byte[] bytes = Main.class.getResourceAsStream("/public/index.html").readAllBytes();
    String html = new String(bytes, StandardCharsets.UTF_8);
    ctx.html(html);
});

        // API endpoints
        app.get("/donors", ctx -> {
            ctx.json(donorDAO.getAllDonors());
        });

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

        // Optional: Handle 404 for invalid routes
        app.error(404, ctx -> {
            ctx.status(404).json(new ErrorResponse("Not found"));
        });
    }

    // Simple class for error responses
    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}