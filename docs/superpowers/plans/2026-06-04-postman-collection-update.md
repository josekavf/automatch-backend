# Postman Collection Update Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Update the Postman collection to include the missing "Update Professional" endpoint and correct the "Create Booking" payload.

**Architecture:** Surgical edits to the `AutoMatch-Backend.postman_collection.json` file using the `replace` tool.

**Tech Stack:** JSON, Postman Collection Format v2.1.0.

---

### Task 1: Update "Create Booking" Payload

**Files:**
- Modify: `AutoMatch-Backend.postman_collection.json`

- [ ] **Step 1: Replace the "Create Booking" raw body**

Old string:
```json
							"raw": "{\n    \"clientId\": \"{{userId}}\",\n    \"professionalId\": \"{{mechanicId}}\",\n    \"serviceName\": \"Troca de Óleo e Filtro\",\n    \"appointmentTime\": \"2026-07-15T14:30:00\"\n}",
```

New string:
```json
							"raw": "{\n    \"clientId\": \"{{userId}}\",\n    \"clientEmail\": \"jose.silva@example.com\",\n    \"professionalId\": \"{{mechanicId}}\",\n    \"professionalEmail\": \"professional@example.com\",\n    \"serviceName\": \"Troca de Óleo e Filtro\",\n    \"appointmentTime\": \"2026-07-15T14:30:00\"\n}",
```

- [ ] **Step 2: Commit**

```bash
git add AutoMatch-Backend.postman_collection.json
git commit -m "fix: update Create Booking payload in Postman collection"
```

### Task 2: Add "Update Professional" Request

**Files:**
- Modify: `AutoMatch-Backend.postman_collection.json`

- [ ] **Step 1: Insert "Update Professional" after "Search Professionals"**

Search for:
```json
					"response": []
				}
			]
		},
		{
			"name": "3. Booking",
```

Replace with:
```json
					"response": []
				},
				{
					"name": "Update Professional",
					"request": {
						"method": "PUT",
						"header": [
							{
								"key": "Authorization",
								"value": "Bearer {{token}}",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\n    \"firstName\": \"José\",\n    \"lastName\": \"Mecânico\",\n    \"specialty\": \"Mecânico de Motores\",\n    \"services\": [\"Troca de Óleo\", \"Revisão Geral\"],\n    \"active\": true\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "{{baseUrl}}/api/v1/professionals/{{mechanicId}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"api",
								"v1",
								"professionals",
								"{{mechanicId}}"
							]
						},
						"description": "Updates professional profile details. Requires a valid JWT token."
					},
					"response": []
				}
			]
		},
		{
			"name": "3. Booking",
```

- [ ] **Step 2: Commit**

```bash
git add AutoMatch-Backend.postman_collection.json
git commit -m "feat: add Update Professional request to Postman collection"
```

### Task 3: Final Validation

- [ ] **Step 1: Verify JSON validity**

Run: `jq . AutoMatch-Backend.postman_collection.json > /dev/null`
Expected: Success (no output, exit code 0)

---
