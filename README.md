# EMailProducer
Spring Boot + Java 8 + RabbitMQ Email Sender Example (with multipart attachments)
Attachments converto Base64

# Test request
Postman requestUrl POST: http://localhost:8080/sendMail/producer
Postman Body:

    {
        "to": "testUser@com.tr",
        "sender": "sendPostman@com.tr",    
        "subject":"RabbitMQ Send Mail Test Multipart attachments example",
        "body" : "This is test mail. Mail comes from rabbitMQ. Multipart attachments example.",
        "attachments": [
            {
                "fileName" : "test.csv",
                "filePath" : "C:\\Users\\username\\Desktop\\test.csv"                
            },
            {
                "fileName" : "test.pdf",
                "filePath" : "C:\\Users\\username\\Desktop\\test.pdf"                
            }
        ]
    }


