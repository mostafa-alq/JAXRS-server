# Part 1: Service Architecture & Setup

### Question 1.1: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

By default, JAX-RS creates a brand new instance of the class for every single HTTP request. Because the object is deleted after the response is sent, standard instance variables will be reset each time. Therefore, to retain data and maintain the state across several requests, memory structures must be declared as static and act as class-level variables so the data belongs to the class itself and not the instance. Additionally, because multiple clients may send requests simultaneously, these shared static structures must be thread-safe to prevent race conditions when reading and writing simultaneously.

### Question 1.2: Why is the provision of "Hypermedia" (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

HATEOS makes an API self-documenting by dropping navigation URLs directly into the JSON responses. This allows frontend developers to directly read the "_links" object we provide, and if I ever need to change the internal API routing in the future, the client app won't break because it dynamically follows the link rather than relying on a hardcoded path.

# Part 2: Room Management

### Question 2.1: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.

Returning only IDs saves a lot of network bandwidth which can be crucial for clients on slower connections. However, it forces the client to do extra processing because if they want to display the room names, they have to send a lot of GET requests. Therefore, returning the full room object may be more efficient as despite the fact that it uses more bandwidth initially, it saves the client processing time and cuts down on total network bandwidth usage.

### Question 2.2: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

The DELETE operation is fully idempotent in my implementation. Idempotency just means that running the same request multiple times doesn't change the server state more compared to the first request. If a client deletes a room, we remove it and send a "No Content" response. If they accidentally send the same delete request again, the server would return a "Not Found" response each time the client sent the delete request. The response code changes, but the state of the server is still the same.

# Part 3: Sensor Operations & Linking

### Question 3.1: We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

The "@Consumes" annotation sets a strict format. If a client ignores this and sends an XML or plain text payload, JAX-RS handles the mismatch automatically and intercepts the bad request, returning a "Unsupported Media Type" response back to the client. This shields my code from having to parse incorrect data.

### Question 3.2: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/v1/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?

URL paths are strict and rigid and point towards specific locations of data, whereas query parameters are designed to filter or modify an existing collection. Using query parameters is a lot better as it keeps the URL routing clean and allows clients to easily stack multiple optional filters, whereas putting filters directly on the path creates rigid and messy routing trees.

# Part 4: Deep Nesting with Sub-Resources

### Question 4.1: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?

If I placed every nested route directly into the SensorResource.java class, the file would become very large and difficult to maintain and keep readable. However, the Sub-Resource locator pattern fixes this by giving the responsibility of finding the /readings path to a completely separate class, which is SensorReadingResource.java. This keeps my code organised, makes files much easier to read and test and ensures that my code can be maintainable. This also allows better scalability for the future and keeps the codebase a lot less complex compared to using one massive controller class.

# Part 5: Advanced Error Handling, Exception Mapping & Logging

### Question 5.1: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

A "404 Not Found" response usually means that the actual URL the client is trying to reach doesn't actually exist. However, if the client is sending a POST request to the correct "/sensors" URL and their JSON is formatted perfectly but the reference is not found or incorrect, returning a "402 Unprocessable Entity" response is perfect; This tells the client that they reached the correct endpoint and their JSON syntax is fine, but the reference was incorrect.

### Question 5.2: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

If the API returned internal Java stack traces to clients when something breaks, this would allow possible hackers to see how the server operates. A stack trace reveals what framework I used, what versions of the libraries I have installed are and even the internal file structure of my solver. A hacker with knowledge on how to infiltrate servers may be able to find vulnerabilities within these libraries and attack my system.

### Question 5.3: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

If I used "Logger.info()" into every endpoint, I would have a lot of repeated and unnecessary code. Additionally, if I add a new endpoint and forget to type the logger command, I would lose track of that traffic. However, by using a JAX-RS filter, I can set up the logging rules in one single place rather than across multiple places, making my code a lot more maintainable. Additionally, the filter automatically intercepts every request and response, guaranteeing that everything is logged correctly.