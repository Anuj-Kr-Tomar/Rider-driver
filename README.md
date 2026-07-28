#  Ride Sharing  Application

A Spring Boot based Ride Sharing backend inspired by applications like Uber. This project demonstrates how multiple independent services communicate using REST APIs and Apache Kafka while leveraging Redis for real-time driver location tracking.

---

##  Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Data Redis
- Spring Kafka
- Spring Cloud OpenFeign
- MySQL
- Redis
- Apache Kafka
- Maven
- Docker (Redis)
- Eclipse IDE

---

#  Microservices

| Service | Port | Responsibility |
|----------|------|----------------|
| Location Service | 8082 | Stores and retrieves driver locations using Redis Geospatial |
| Ride Service | 8081 | Creates rides, manages ride lifecycle, publishes Kafka events |
| Matching Service | 8084 | Consumes ride requests, finds nearby drivers, assigns drivers |

---

# 🏛️ Project Architecture

```
                    Rider App
                        │
                        ▼
               Ride Service (8081)
                        │
                Publish Event
                        │
                        ▼
                 Apache Kafka
                        │
        RideRequestedEvent Topic
                        │
                        ▼
            Matching Service (8084)
                        │
        Fetch Nearby Drivers (REST)
                        │
                        ▼
           Location Service (8082)
                        │
                  Redis GEO
                        │
                Best Driver Found
                        │
                        ▼
                 Kafka Event
                        │
                        ▼
                Ride Service
                        │
                  Ride Updated
```

---

#  Project Structure

```
Ride-Sharing-Application
│
├── location-service
│
├── ride-service
│
└── matching-service
```

---

#  Features

##  Ride Service

- Create Ride
- Start Ride
- Complete Ride
- Ride Status
- Rider Ride History
- Kafka Producer

---

##  Location Service

- Update Driver Location
- Find Nearby Drivers
- Redis Geospatial Storage
- Real-time Location Tracking

---

##  Matching Service

- Kafka Consumer
- Driver Matching
- REST Communication with Location Service
- Driver Assignment

---

#  Infrastructure

This project uses

- Redis (Running inside Docker)
- Apache Kafka
- MySQL

Redis is started using Docker while Kafka and MySQL are installed locally.

---

#  Running the Project

## Step 1

Start Redis Container

```bash
docker start redis
```

or

```bash
docker run -d --name redis -p 6379:6379 redis
```

---

## Step 2

Start Kafka

```bash
kafka-server-start.bat config\kraft\server.properties
```

---

## Step 3

Start MySQL Server

Make sure MySQL is running on

```
localhost:3306
```

---

## Step 4

Run Location Service

```
location-service
```

Port

```
8082
```

---

## Step 5

Run Ride Service

```
ride-service
```

Port

```
8083
```

---

## Step 6

Run Matching Service

```
matching-service
```

Port

```
8084
```

---

#  Workflow

### 1. Update Driver Location

```
Driver
    │
    ▼
Location Service
    │
    ▼
Redis
```

---

### 2. Rider Requests Ride

```
Ride Service
      │
      ▼
Kafka
      │
      ▼
Matching Service
      │
      ▼
Location Service
      │
      ▼
Redis
```

---

### 3. Driver Assigned

```
Matching Service
       │
       ▼
Ride Service
```

---

#  Sample APIs

## Update Driver Location

```
POST
http://localhost:8082/api/v1/locations/drivers/update
```

Example

```json
{
    "driverId":"driver1",
    "latitude":18.5204,
    "longitude":73.8567
}
```

---

## Request Ride

```
POST
http://localhost:8081/api/v1/rides/request
```

Example

```json
{
    "riderId":"rider1",
    "pickupLatitude":18.5204,
    "pickupLongitude":73.8567,
    "pickupAddress":"Pune Station",
    "dropLatitude":18.5314,
    "dropLongitude":73.8446,
    "dropAddress":"Shivaji Nagar"
}
```

---

## Get Ride

```
GET
http://localhost:8081/api/v1/rides/{rideId}
```

---

## Start Ride

```
PUT
http://localhost:8081/api/v1/rides/{rideId}/start
```

---

## Complete Ride

```
PUT
http://localhost:8081/api/v1/rides/{rideId}/complete
```

---

## Rider History

```
GET
http://localhost:8081/api/v1/rides/rider/{riderId}
```

---

#  Kafka Flow

```
Ride Requested

Ride Service
      │
      ▼
Kafka Topic
      │
      ▼
Matching Service
      │
Driver Assigned
      │
      ▼
Ride Service
```

---

#  Redis Geospatial

Driver locations are stored using Redis GEO commands.

Examples

```
GEOADD

GEOPOS

GEODIST

GEOSEARCH
```

---

#  Concepts Learned

- Microservices Architecture
- REST APIs
- Apache Kafka
- Event Driven Architecture
- Redis Geospatial
- Spring Data Redis
- Spring Data JPA
- Spring Cloud OpenFeign
- Producer & Consumer
- Inter-service Communication
- Maven
- Docker
- MySQL Integration

---

# 👨‍💻 Author

**Anuj Tomar**

Third Year B.E. Computer Engineering

Army Institute of Technology, Pune

