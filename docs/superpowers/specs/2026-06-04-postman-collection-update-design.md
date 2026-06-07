# Postman Collection Update Design

**Goal:** Ensure the `AutoMatch-Backend.postman_collection.json` is fully up-to-date with all microservice endpoints and contains correct request payloads.

**Status:** Approved

## 1. Identified Discrepancies

- **Create Booking**: The current payload in the collection is missing `clientEmail` and `professionalEmail` fields, which are mandatory in the `CreateBookingRequest` DTO.
- **Update Professional**: This endpoint (`PUT /api/v1/professionals/{id}`) is implemented in the `catalog-service` but missing from the Postman collection.

## 2. Proposed Changes

### 2.1 Update "Create Booking" Payload
- **Service:** Booking Service
- **Endpoint:** `POST /api/v1/bookings`
- **New Payload:**
```json
{
    "clientId": "{{userId}}",
    "clientEmail": "jose.silva@example.com",
    "professionalId": "{{mechanicId}}",
    "professionalEmail": "professional@example.com",
    "serviceName": "Troca de Óleo e Filtro",
    "appointmentTime": "2026-07-15T14:30:00"
}
```

### 2.2 Add "Update Professional" Request
- **Service:** Catalog Service
- **Endpoint:** `PUT /api/v1/professionals/{{mechanicId}}`
- **Folder:** "2. Catalog"
- **Method:** `PUT`
- **Headers:** `Authorization: Bearer {{token}}`
- **Payload:**
```json
{
    "firstName": "José",
    "lastName": "Mecânico",
    "specialty": "Mecânico de Motores",
    "services": ["Troca de Óleo", "Revisão Geral"],
    "active": true
}
```

## 3. Verification Plan
- Manually inspect the JSON file to ensure valid structure.
- Verify that all endpoints use `{{baseUrl}}` and include appropriate headers.
