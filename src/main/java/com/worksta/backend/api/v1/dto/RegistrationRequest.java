package com.worksta.backend.api.v1.dto;

public record RegistrationRequest(String username, String password, String role) {
    public boolean isWorker()   { return "WORKER".equalsIgnoreCase(role()); }
    public boolean isBusiness() { return "BUSINESS".equalsIgnoreCase(role()); }
}
