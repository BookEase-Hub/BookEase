@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    private String description;
    // Other property details
}

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long propertyId;
    private Long userId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // Other booking details
}

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long propertyId;
    private LocalDate date;
    private Boolean isAvailable;
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long propertyId;
    private BigDecimal pricePerNight;
    // Other pricing details
}

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByLocationAndAvailabilityDate(String location, LocalDate date);
}

@Service
public class BookingService {
    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> searchProperties(String location, LocalDate date) {
        return propertyRepository.findByLocationAndAvailabilityDate(location, date);
    }

    // Methods for booking management
}

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        // Sophisticated error handling
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}// Additional imports
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// ... existing code ...

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    // Additional user details
}

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // UserDetailsService methods
}

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long propertyId;
    private Long userId;
    private String comment;
    private Double rating;
    // Other review details
}

// Payment processing service
@Service
public class PaymentService {
    public PaymentStatus processPayment(PaymentDetails paymentDetails) {
        // Integrate with payment provider
        return new PaymentStatus();
    }
}

// Enhanced search method in PropertyRepository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Existing method
    @Query("SELECT p FROM Property p WHERE p.location = :location AND p.availability.date = :date AND p.pricing.pricePerNight <= :maxPrice")
    List<Property> findByLocationAndAvailabilityDateAndMaxPrice(String location, LocalDate date, BigDecimal maxPrice);
}

// Messaging Controller
@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageDto messageDto) {
        return ResponseEntity.ok(messageService.sendMessage(messageDto));
    }
}

// GlobalExceptionHandler enhancements
@ControllerAdvice
public class GlobalExceptionHandler {
    // Existing handler

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}

// ... additional entities and services ...
// UserService.java
// ... other methods ...

// Method to authenticate a user
public UserDetails authenticateUser(String username, String password) {
    // Logic to authenticate user with Spring Security
}

// Method to authorize user actions
public boolean authorizeUserAction(UserDetails user, String action) {
    // Logic to authorize user based on roles and permissions
}
// Review.java (Entity)
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @ManyToOne
    private Property property;
   
    @ManyToOne
    private User user;
   
    private int rating;
    private String comment;
   
    // Getters and setters
}

// ReviewRepository.java
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
}

// ReviewService.java
public class ReviewService {
    // Method to add a review
    public Review addReview(User user, Property property, int rating, String comment) {
        // Logic to save review
    }
}
// PaymentService.java
public class PaymentService {
    // Method to process payment
    public PaymentResponse processPayment(PaymentRequest request) {
        // Logic to integrate with payment provider API
    }
}
// PropertyRepository.java
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom search method
    List<Property> findByAmenitiesInAndTypeAndGuestLimit(List<String> amenities, String type, int guests);
}
// MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Method to send a message
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending messages
    }
}
// GlobalExceptionHandler.java
@ControllerAdvice
public class GlobalExceptionHandler {
    // Existing exception handlers...

    // New handler for a custom exception
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        // Logic to handle custom exception
    }
}


// Image.java (Entity)
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imageUrl;
    private Long propertyId; // Owner ID
    // Other metadata fields

    // Getters and setters
}

// Property.java (Updated Entity to include relationship with Image)
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // ... other fields ...

    @OneToMany(mappedBy = "property")
    private List<Image> images;

    // Getters and setters
}

// ImageRepository.java
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPropertyId(Long propertyId);
}

// ImageService.java
@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Image saveImage(MultipartFile file, Long propertyId) {
        // Logic to save image
    }

    public List<Image> getImagesByPropertyId(Long propertyId) {
        return imageRepository.findByPropertyId(propertyId);
    }
}

// ImageController.java
@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("propertyId") Long propertyId) {
        // Validate file type and size
        // Save image and return response
    }
}

// Notification.java (Entity)
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String message;
    private Boolean read;
    // Other fields like timestamp

    // Getters and setters
}

// NotificationRepository.java
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndRead(Long userId, Boolean read);
}

// NotificationService.java
@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdAndRead(userId, false);
    }

    public Notification createNotification(Long userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setRead(false);
        return notificationRepository.save(notification);
    }
}

// WebSocketConfig.java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }
}

// ChatController.java
@Controller
public class ChatController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }
}


// FcmService.java
@Service
public class FcmService {
    private String firebaseServerKey = "YOUR_FIREBASE_SERVER_KEY";

    public void sendPushNotification(String deviceToken, String title, String body) throws Exception {
        URL url = new URL("https://fcm.googleapis.com/fcm/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestProperty("Authorization", "key=" + firebaseServerKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        JSONObject message = new JSONObject();
        message.put("to", deviceToken);
        message.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", title);
        notification.put("body", body);

        message.put("notification", notification);

        OutputStream os = conn.getOutputStream();
        os.write(message.toString().getBytes("UTF-8"));
        os.close();

        // Read FCM response
        InputStream is = conn.getInputStream();
        String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
        is.close();

        // Handle the response as needed
    }
}

// UserController.java
@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        if (!userDto.isTermsAccepted()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You must accept the terms and conditions.");
        }

        // Proceed with the sign-up process
        // ...
    }
}

// UserDto.java
public class UserDto {
    // Other user fields
    private boolean termsAccepted;

    // Getters and setters
    public boolean isTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }
}

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Transactional
    public Review addReview(Long userId, Long propertyId, int rating, String comment) {
        // Validate inputs
        if (userId == null || propertyId == null || rating < 1 || rating > 5 || comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid input parameters for creating a review.");
        }

        // Fetch related entities
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found with id: " + propertyId));

        // Create a new Review instance
        Review review = new Review();
        review.setUser(user);
        review.setProperty(property);
        review.setRating(rating);
        review.setComment(comment);

        // Save the review to the database
        try {
            return reviewRepository.save(review);
        } catch (Exception e) {
            // Log the exception and handle it accordingly
            throw new DatabaseOperationException("Failed to save the review.", e);
        }
    }
   
    // Other service methods...
}

// Custom exception class for database operation failures
public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    // Autowired repositories and other services

    @Transactional
    public Review addReview(Long userId, Long propertyId, int rating, String comment) {
        try {
            // Input validation and review saving logic
            // ...

            logger.info("Review added successfully for user: {} and property: {}", userId, propertyId);
            return savedReview;
        } catch (IllegalArgumentException e) {
            logger.error("Error adding review: Invalid input parameters.", e);
            throw e;
        } catch (EntityNotFoundException e) {
            logger.error("Error adding review: Entity not found.", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error adding review: Unexpected error.", e);
            throw new DatabaseOperationException("Failed to save the review.", e);
        }
    }

    // Other service methods...
}

// Custom exception class for database operation failures
public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
<!-- SLF4J API module -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.13</version>
</dependency>
<!-- Logback Classic Module -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.10</version>
</dependency>
 

// ... existing imports ...

// Additional imports for referral program
import java.util.Optional;

// ... existing entities ...

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String referralCode; // Unique referral code for each user
    private Long referredBy; // ID of the user who referred this user
    private Integer rewardPoints = 0; // Initialize reward points
    // Additional user details
}

// ... existing repositories ...

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByReferralCode(String referralCode);
}

// ... existing services ...

@Service
@Transactional
public class UserService implements UserDetailsService {
    // ... existing autowired repositories and methods ...

    public void processReferral(String referralCode) {
        Optional<User> referredUserOpt = userRepository.findByReferralCode(referralCode);
        if (referredUserOpt.isPresent()) {
            User referredUser = referredUserOpt.get();
            Optional<User> referrerOpt = userRepository.findById(referredUser.getReferredBy());
            if (referrerOpt.isPresent()) {
                User referrer = referrerOpt.get();
                referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Assuming each referral gives 10 points
                userRepository.save(referrer);
            }
        } else {
            throw new IllegalArgumentException("Referral code not found.");
        }
    }
}

// ... existing controllers ...

@RestController
public class ReferralController {
    @Autowired
    private UserService userService;

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestBody Map<String, String> requestBody) {
        String referralCode = requestBody.get("referralCode");
        try {
            userService.processReferral(referralCode);
            return ResponseEntity.ok("Referral successful and rewards updated.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

// ... existing exception handlers ...
// ... existing imports ...

// Additional imports for referral program
import java.util.Optional;

// ... existing entities ...

@Entity
public class User {
    // ... existing fields ...

    private String referralCode; // Unique referral code for each user
    private Long referredBy; // ID of the user who referred this user
    private Integer rewardPoints = 0; // Initialize reward points

    // ... existing methods ...
}

// ... existing repositories ...

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByReferralCode(String referralCode);
    Optional<User> findById(Long id);
}

// ... existing services ...

@Service
@Transactional
public class UserService {
    // ... existing autowired repositories and methods ...

    public void processReferral(String referralCode) {
        Optional<User> referredUserOpt = userRepository.findByReferralCode(referralCode);
        if (referredUserOpt.isPresent()) {
            User referredUser = referredUserOpt.get();
            Optional<User> referrerOpt = userRepository.findById(referredUser.getReferredBy());
            if (referrerOpt.isPresent()) {
                User referrer = referrerOpt.get();
                referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Assuming each referral gives 10 points
                userRepository.save(referrer);
            }
        } else {
            throw new IllegalArgumentException("Referral code not found.");
        }
    }
}

// ... existing controllers ...

@RestController
public class ReferralController {
    @Autowired
    private UserService userService;

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestBody Map<String, String> requestBody) {
        String referralCode = requestBody.get("referralCode");
        try {
            userService.processReferral(referralCode);
            return ResponseEntity.ok("Referral successful and rewards updated.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

// ... existing exception handlers ...



// ... existing imports ...

// Additional imports for referral program
import java.util.Optional;

// ... existing entities ...

@Entity
public class User {
    // ... existing fields ...

    private String referralCode; // Unique referral code for each user
    private Long referredBy; // ID of the user who referred this user
    private Integer rewardPoints = 0; // Initialize reward points

    // ... existing methods ...
}

@Entity
public class Review {
    // ... existing fields ...

    @ManyToOne
    private User user;

    @ManyToOne
    private Property property;

    private int rating;
    private String comment;

    // ... existing methods ...
}

// ... existing repositories ...

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByReferralCode(String referralCode);
    Optional<User> findById(Long id);
}

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
}

// ... existing services ...

@Service
@Transactional
public class UserService {
    // ... existing autowired repositories and methods ...

    public void processReferral(String referralCode) {
        Optional<User> referredUserOpt = userRepository.findByReferralCode(referralCode);
        if (referredUserOpt.isPresent()) {
            User referredUser = referredUserOpt.get();
            Optional<User> referrerOpt = userRepository.findById(referredUser.getReferredBy());
            if (referrerOpt.isPresent()) {
                User referrer = referrerOpt.get();
                referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Assuming each referral gives 10 points
                userRepository.save(referrer);
            }
        } else {
            throw new IllegalArgumentException("Referral code not found.");
        }
    }
}

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Review addReview(User user, Property property, int rating, String comment) {
        Review review = new Review();
        review.setUser(user);
        review.setProperty(property);
        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }
}

// ... existing controllers ...

@RestController
public class ReferralController {
    @Autowired
    private UserService userService;

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestBody Map<String, String> requestBody) {
        String referralCode = requestBody.get("referralCode");
        try {
            userService.processReferral(referralCode);
            return ResponseEntity.ok("Referral successful and rewards updated.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

// ... existing exception handlers ...

@ControllerAdvice
public class GlobalExceptionHandler {
    // ... existing exception handlers ...

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // New handler for a custom exception
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        // Logic to handle custom exception
    }
}


// Notification.java (Entity)
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String message;
    private Boolean read;
    // Other fields like timestamp

    // Getters and setters
}

// NotificationRepository.java
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndRead(Long userId, Boolean read);
}

// NotificationService.java
@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdAndRead(userId, false);
    }

    public Notification createNotification(Long userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setRead(false);
        return notificationRepository.save(notification);
    }
}

// WebSocketConfig.java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
}
// Notification.java (Entity)
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String message;
    private Boolean read;
    // Other fields like timestamp

    // Getters and setters
}

// NotificationRepository.java
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndRead(Long userId, Boolean read);
}

// NotificationService.java
@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdAndRead(userId, false);
    }

    public Notification createNotification(Long userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setRead(false);
        return notificationRepository.save(notification);
    }
}

// WebSocketConfig.java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
}
@Service
public class FcmService {
    private String firebaseServerKey = "YOUR_FIREBASE_SERVER_KEY";

    public void sendPushNotification(String deviceToken, String title, String body) throws Exception {
        // Implementation details...
    }
}
// service-worker.js
self.addEventListener('push', function(event) {
    // Notification display logic...
});
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // Configuration details...
}

@Controller
public class ChatController {
    // Message handling methods...
}
app.post('/refer', async (req, res) => {
    // Referral processing logic...
});
// Call connect when the page loads
connect();

// Register service worker and request permission for notifications
if ('serviceWorker' in navigator && 'PushManager' in window) {
    // Service worker registration and push subscription logic...
}

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        // Sign-up logic with terms and conditions check
    }
}

public class UserDto {
    // User data transfer object with termsAccepted field
}
app.post('/refer', async (req, res) => {
    // Referral processing logic...
});
// Service worker registration and push messaging logic
@Service
public class FcmService {
    // Method to send push notifications using FCM
}
// User.java (Entity)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String referralCode; // Unique referral code for each user
    private Long referredBy; // ID of the user who referred this user
    private Integer rewardPoints = 0; // Initialize reward points

    // Getters and setters
    public void setTermsAccepted(boolean termsAccepted) {
        // Logic to set terms accepted
    }
}

// ReviewService.java
@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PropertyRepository propertyRepository;

    @Transactional
    public Review addReview(Long userId, Long propertyId, int rating, String comment) {
        // Validate inputs and fetch related entities
        // Create and save the review
    }
}

// ReferralController.java
@RestController
@RequestMapping("/referral")
public class ReferralController {
    @Autowired
    private UserService userService;

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestBody Map<String, String> requestBody) {
        String referralCode = requestBody.get("referralCode");
        try {
            userService.processReferral(referralCode);
            return ResponseEntity.ok("Referral successful and rewards updated.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void processReferral(String referralCode) {
        // Find the user who was referred and update the referrer's reward points
    }
}
// User.java (Entity)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String referralCode; // Unique referral code for each user
    private Long referredBy; // ID of the user who referred this user
    private Integer rewardPoints = 0; // Initialize reward points
    private boolean termsAccepted; // Flag to track terms acceptance

    // Getters and setters
}

// ReviewService.java
@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PropertyRepository propertyRepository;

    @Transactional
    public Review addReview(Long userId, Long propertyId, int rating, String comment) {
        // Validate inputs and fetch related entities
        // Create and save the review
    }
   
    // Other service methods...
}

// ReferralController.java
@RestController
@RequestMapping("/referral")
public class ReferralController {
    @Autowired
    private UserService userService;

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestBody Map<String, String> requestBody) {
        String referralCode = requestBody.get("referralCode");
        try {
            userService.processReferral(referralCode);
            return ResponseEntity.ok("Referral successful and rewards updated.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void processReferral(String referralCode) {
        // Find the user who was referred and update the referrer's reward points
    }
   
    // Other service methods...
}

// ChatController.java
@Controller
public class ChatController {
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }
}

// WebSocketConfig.java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // Configuration details...
}



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UnifiedApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnifiedApplication.class, args);
    }
}

// ... other existing imports ...

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String referralCode; // Unique referral code for each user
    private Long referredBy; // ID of the user who referred this user
    private Integer rewardPoints = 0; // Initialize reward points
    // Additional user details
    // ... other fields ...

    // Getters and setters
    // ... other methods ...
}

// ... other existing entities ...

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByReferralCode(String referralCode);
    Optional<User> findById(Long id);
}

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    // ... other autowired services and methods ...

    public void processReferral(String referralCode) {
        Optional<User> referredUserOpt = userRepository.findByReferralCode(referralCode);
        if (referredUserOpt.isPresent()) {
            User referredUser = referredUserOpt.get();
            Optional<User> referrerOpt = userRepository.findById(referredUser.getReferredBy());
            if (referrerOpt.isPresent()) {
                User referrer = referrerOpt.get();
                referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Assuming each referral gives 10 points
                userRepository.save(referrer);
            }
        } else {
            throw new IllegalArgumentException("Referral code not found.");
        }
    }

    // ... other methods ...
}

@RestController
public class ReferralController {
    @Autowired
    private UserService userService;

    @PostMapping("/refer")
    public ResponseEntity<String> referUser(@RequestBody Map<String, String> requestBody) {
        String referralCode = requestBody.get("referralCode");
        try {
            userService.processReferral(referralCode);
            return ResponseEntity.ok("Referral successful and rewards updated.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
   // ... other endpoints ...
}
// UnifiedApplication.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UnifiedApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnifiedApplication.class, args);
    }
}

// User.java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String referralCode; // Unique referral code for each user
    private Long referredBy; // ID of the user who referred this user
    private Integer rewardPoints = 0; // Initialize reward points
    // Additional user details
    // ... other fields ...

    // Getters and setters
    // ... other methods ...
}

// Review.java
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private int rating;
    private String comment;
    // ... other fields ...

    // Getters and setters
    // ... other methods ...
}

// UserRepository.java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByReferralCode(String referralCode);
    Optional<User> findById(Long id);
}

// ReviewRepository.java
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
}

// UserService.java
@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    // ... other autowired services and methods ...

    public void processReferral(String referralCode) {
        // Implementation as before
    }

    // ... other methods ...
}

// ReviewService.java
@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Review addReview(User user, Property property, int rating, String comment) {
        // Implementation to add a review
    }
}

// ReferralController.java
@RestController
public class ReferralController {
    @Autowired
    private UserService userService;

    @PostMapping("/refer")
    public ResponseEntity<String> referUser(@RequestBody Map<String, String> requestBody) {
        // Implementation as before
    }

    // ... other endpoints ...
}

// GlobalExceptionHandler.java
@ControllerAdvice
public class GlobalExceptionHandler {
    // Existing exception handlers

    // New handler for a custom exception
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        // Logic to handle custom exception
    }
}

// UnifiedApplication.java
@SpringBootApplication
public class UnifiedApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnifiedApplication.class, args);
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields

    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Getters and setters
    // Other methods
}

// Review.java (Entity)
@Entity
public class Review {
    // Existing fields

    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;

    // Other fields like rating and comment
    // Getters and setters
    // Other methods
}

// Notification.java (Entity)
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String message;
    private Boolean read;
    // Other fields like timestamp

    // Getters and setters
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByReferralCode(String referralCode);
    Optional<User> findById(Long id);
}

// ReviewRepository.java
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
}

// NotificationRepository.java
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndRead(Long userId, Boolean read);
}

// UserService.java
@Service
@Transactional
public class UserService implements UserDetailsService {
    // Existing methods

    public void processReferral(String referralCode) {
        // Implementation as before
    }

    // Other methods
}

// ReviewService.java
@Service
public class ReviewService {
    // Existing methods

    public Review addReview(User user, Property property, int rating, String comment) {
        // Implementation to add a review
    }

    // Other methods
}

// NotificationService.java
@Service
public class NotificationService {
    // Existing methods

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdAndRead(userId, false);
    }

    public Notification createNotification(Long userId, String message) {
        // Implementation to create a notification
    }

    // Other methods
}

// WebSocketConfig.java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // Existing configuration

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
}


// Spring Boot Application Entry Point
@SpringBootApplication
public class IntegratedApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegratedApplication.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;
    private boolean termsAccepted;

    // Getters and setters
}

// Review Entity
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private int rating;
    private String comment;

    // Getters and setters
}

// Notification Entity
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String message;
    private Boolean read;

    // Getters and setters
}

// Repositories
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByReferralCode(String referralCode);
}

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
}

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndRead(Long userId, Boolean read);
}

// Services
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void processReferral(String referralCode) {
        // Referral processing logic
    }
}

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PropertyRepository propertyRepository;

    @Transactional
    public Review addReview(Long userId, Long propertyId, int rating, String comment) {
        // Review creation logic
    }
}

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getUserNotifications(Long userId) {
        // Notification retrieval logic
    }

    public Notification createNotification(Long userId, String message) {
        // Notification creation logic
    }
}

// Controllers
@RestController
@RequestMapping("/referral")
public class ReferralController {
    @Autowired
    private UserService userService;

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestBody Map<String, String> requestBody) {
        // Referral endpoint logic
    }
}

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        // User sign-up logic
    }
}

// DTOs
public class UserDto {
    // User data transfer object fields
}

// WebSocket Configuration for Real-Time Communication
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
}

// FCM Service for Push Notifications
@Service
public class FcmService {
    private String firebaseServerKey = "YOUR_FIREBASE_SERVER_KEY";

    public void sendPushNotification(String deviceToken, String title, String body) {
        // Push notification sending logic
    }
}
// Spring Boot Application Entry Point
@SpringBootApplication
public class IntegratedApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegratedApplication.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // Other user fields
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;
    private boolean termsAccepted;

    // Getters and setters
}

// UserRepository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void processReferral(String referralCode) {
        // Referral processing logic
    }
   
    // Other service methods...
}

// ChatController
@Controller
public class ChatController {
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }
}

// WebSocketConfig
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }
}
<!

// Main Application Entry Point
@SpringBootApplication
public class UnifiedApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnifiedApplication.class, args);
    }
}

// Entities
@Entity
public class User {
    // User fields, including referralCode and referredBy
    // ... other fields and methods ...
}

@Entity
public class Property {
    // Property fields
    // ... other fields and methods ...
}

@Entity
public class Booking {
    // Booking fields
    // ... other fields and methods ...
}

@Entity
public class Review {
    // Review fields
    // ... other fields and methods ...
}

// Repositories
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // User repository methods
}

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Property repository methods
}

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Booking repository methods
}

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Review repository methods
}

// Services
@Service
public class UserService {
    // User service methods, including referral processing
    // ... other methods ...
}

@Service
public class PropertyService {
    // Property service methods
    // ... other methods ...
}

@Service
public class BookingService {
    // Booking service methods
    // ... other methods ...
}

@Service
public class ReviewService {
    // Review service methods
    // ... other methods ...
}

// Controllers
@RestController
public class UserController {
    // User-related endpoints
    // ... other methods ...
}

@RestController
public class PropertyController {
    // Property-related endpoints
    // ... other methods ...
}

@RestController
public class BookingController {
    // Booking-related endpoints
    // ... other methods ...
}

@RestController
public class ReviewController {
    // Review-related endpoints
    // ... other methods ...
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    // Exception handlers
    // ... other methods ...
}

// Main Application Entry Point
@SpringBootApplication
public class IntegratedApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegratedApplication.class, args);
    }
}

// Entities
@Entity
public class User {
    // User fields, including referralCode and referredBy
    // ... other fields and methods ...
}

@Entity
public class Property {
    // Property fields
    // ... other fields and methods ...
}

@Entity
public class Booking {
    // Booking fields
    // ... other fields and methods ...
}

@Entity
public class Review {
    // Review fields
    // ... other fields and methods ...
}

// Repositories
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // User repository methods
}

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Property repository methods
}

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Booking repository methods
}

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Review repository methods
}

// Services
@Service
public class UserService {
    // User service methods, including referral processing
    // ... other methods ...
}

@Service
public class PropertyService {
    // Property service methods
    // ... other methods ...
}

@Service
public class BookingService {
    // Booking service methods
    // ... other methods ...
}

@Service
public class ReviewService {
    // Review service methods
    // ... other methods ...
}

// Controllers
@RestController
public class UserController {
    // User-related endpoints
    // ... other methods ...
}

@RestController
public class PropertyController {
    // Property-related endpoints
    // ... other methods ...
}

@RestController
public class BookingController {
    // Booking-related endpoints
    // ... other methods ...
}

@RestController
public class ReviewController {
    // Review-related endpoints
    // ... other methods ...
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    // Exception handlers
    // ... other methods ...
}
@SpringBootApplication
public class IntegratedApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegratedApplication.class, args);
    }
}
@Entity
public class User {
    // User fields, including referralCode and referredBy
    // ... other fields and methods ...
}

@Entity
public class Property {
    // Property fields
    // ... other fields and methods ...
}

@Entity
public class Booking {
    // Booking fields
    // ... other fields and methods ...
}

@Entity
public class Review {
    // Review fields
    // ... other fields and methods ...
}
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByReferralCode(String referralCode);
    // ... other methods ...
}

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
    // ... other methods ...
}
@Service
public class UserService {
    // User service methods, including referral processing
    // ... other methods ...
}

@Service
public class ReviewService {
    // Review service methods
    // ... other methods ...
}
@RestController
@RequestMapping("/users")
public class UserController {
    // User-related endpoints
    // ... other methods ...
}

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    // Review-related endpoints
    // ... other methods ...
}
@ControllerAdvice
public class GlobalExceptionHandler {
    // Exception handlers
    // ... other methods ...
}
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
}



// MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Inject services as needed

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending a message
        return ResponseEntity.ok("Message sent successfully");
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields
    private String email; // Add email field

    // Generate a referral code for a new user
    public static String generateReferralCode() {
        // Logic to generate a unique referral code
    }

    // Add referral information when a user is referred
    public void addReferral(Long referredById) {
        this.referredBy = referredById;
        // Additional logic if needed
    }

    // Reward the referrer with points
    public void rewardReferrer(User referrer) {
        referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Example reward
        // Additional logic if needed
    }

    // Getters and setters for new fields
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

// UserNotFoundException.java (Custom Exception)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// MessageDto.java (Data Transfer Object)
public class MessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    // Getters and setters
}

// Main Application Entry Point
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Generate a unique referral code
    @PrePersist
    protected void onCreate() {
        referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    // ... other methods ...
}

// UserRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Process a referral
    public void processReferral(String referralCode, Long referredById) {
        User referredUser = findUserByEmail(referralCode);
        referredUser.setReferredBy(referredById);
        userRepository.save(referredUser);

        // Reward the referrer
        userRepository.findById(referredById).ifPresent(referrer -> {
            referrer.setRewardPoints(referrer.getRewardPoints() + 10);
            userRepository.save(referrer);
        });
    }
}

// UserController
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set user details from DTO
        userService.saveUser(newUser);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestParam String referralCode, Principal principal) {
        User referrer = userService.findUserByEmail(principal.getName());
        userService.processReferral(referralCode, referrer.getId());
        return ResponseEntity.ok("Referral processed successfully");
    }
}

// UserDto
public class UserDto {
    private String username;
    private String email;
    private String password;
    // Getters and setters
}

// Security Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Security configuration methods
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

// WebSocket Configuration
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket configuration methods
}

// HTML, CSS, and JavaScript files
// Place your HTML, CSS, and JavaScript files in the 'src/main/resources/static' directory
@Entity
public class Availability {
    // Fields: propertyId, startDate, endDate, isAvailable, etc.
    // Getters and setters
}

@Entity
public class Pricing {
    // Fields: propertyId, basePrice, seasonalAdjustments, etc.
    // Getters and setters
}

@Entity
public class Payment {
    // Fields: userId, bookingId, amount, paymentStatus, etc.
    // Getters and setters
}

@Entity
public class UserDetails {
    // Fields: userId, firstName, lastName, address, phone, etc.
    // Getters and setters
}
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Methods for availability management
}

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Methods for pricing management
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Methods for payment management
}

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Methods for user details management
}
@Service
public class AvailabilityService {
    // Methods for managing property availability
}

@Service
public class PricingService {
    // Methods for managing property pricing
}

@Service
public class PaymentService {
    // Methods for handling payments
}

@Service
public class UserDetailsService {
    // Methods for managing user details
}
@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    // Endpoints for managing property availability
}

@RestController
@RequestMapping("/pricing")
public class PricingController {
    // Endpoints for managing property pricing
}

@RestController
@RequestMapping("/payment")
public class PaymentController {
    // Endpoints for handling payments
}

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    //

// MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Inject services as needed

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending a message
        return ResponseEntity.ok("Message sent successfully");
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields
    private String email; // Add email field

    // Generate a referral code for a new user
    public static String generateReferralCode() {
        // Logic to generate a unique referral code
    }

    // Add referral information when a user is referred
    public void addReferral(Long referredById) {
        this.referredBy = referredById;
        // Additional logic if needed
    }

    // Reward the referrer with points
    public void rewardReferrer(User referrer) {
        referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Example reward
        // Additional logic if needed
    }

    // Getters and setters for new fields
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

// UserNotFoundException.java (Custom Exception)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// MessageDto.java (Data Transfer Object)
public class MessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    // Getters and setters
}

// Main Application Entry Point
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Generate a unique referral code
    @PrePersist
    protected void onCreate() {
        referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    // ... other methods ...
}

// UserRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Process a referral
    public void processReferral(String referralCode, Long referredById) {
        User referredUser = findUserByEmail(referralCode);
        referredUser.setReferredBy(referredById);
        userRepository.save(referredUser);

        // Reward the referrer
        userRepository.findById(referredById).ifPresent(referrer -> {
            referrer.setRewardPoints(referrer.getRewardPoints() + 10);
            userRepository.save(referrer);
        });
    }
}

// UserController
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set user details from DTO
        userService.saveUser(newUser);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestParam String referralCode, Principal principal) {
        User referrer = userService.findUserByEmail(principal.getName());
        userService.processReferral(referralCode, referrer.getId());
        return ResponseEntity.ok("Referral processed successfully");
    }
}

// UserDto
public class UserDto {
    private String username;
    private String email;
    private String password;
    // Getters and setters
}

// Security Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Security configuration methods
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

// WebSocket Configuration
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket configuration methods
}

// HTML, CSS, and JavaScript files
// Place your HTML, CSS, and JavaScript files in the 'src/main/resources/static' directory
@Entity
public class Availability {
    // Fields: propertyId, startDate, endDate, isAvailable, etc.
    // Getters and setters
}

@Entity
public class Pricing {
    // Fields: propertyId, basePrice, seasonalAdjustments, etc.
    // Getters and setters
}

@Entity
public class Payment {
    // Fields: userId, bookingId, amount, paymentStatus, etc.
    // Getters and setters
}

@Entity
public class UserDetails {
    // Fields: userId, firstName, lastName, address, phone, etc.
    // Getters and setters
}
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Methods for availability management
}

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Methods for pricing management
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Methods for payment management
}

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Methods for user details management
}
@Service
public class AvailabilityService {
    // Methods for managing property availability
}

@Service
public class PricingService {
    // Methods for managing property pricing
}

@Service
public class PaymentService {
    // Methods for handling payments
}

@Service
public class UserDetailsService {
    // Methods for managing user details
}
@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    // Endpoints for managing property availability
}

@RestController
@RequestMapping("/pricing")
public class PricingController {
    // Endpoints for managing property pricing
}

@RestController
@RequestMapping("/payment")
public class PaymentController {
    // Endpoints for handling payments
}

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    //


// User Entity with Referral Information
@Entity
public class User {
    // ... existing fields ...

    // Method to generate a unique referral code for each user
    @PrePersist
    private void initializeReferralCode() {
        this.referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // ... getters and setters ...
}

// UserService with Referral Logic
@Service
public class UserService {
    // ... existing methods ...

    // Method to process a referral when a new user signs up
    public void processReferral(String referralCode) {
        User referrer = userRepository.findByReferralCode(referralCode)
            .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));
        referrer.setRewardPoints(referrer.getRewardPoints() + 10);
        userRepository.save(referrer);
    }

    // ... other methods ...
}

// UserController with Sign-up and Referral Endpoints
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    // Endpoint to handle user sign-up with referral code
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set newUser properties from userDto
        newUser = userService.saveUser(newUser);

        // If a referral code is provided, process the referral
        if (userDto.getReferralCode() != null) {
            userService.processReferral(userDto.getReferralCode());
        }

        return ResponseEntity.ok("User signed up successfully");
    }

    // ... other endpoints ...
}

// UserDto with Referral Code
public class UserDto {
    // ... existing fields ...
    private String referralCode;
    // ... getters and setters ...
}

// Referral Dashboard Controller
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private UserService userService;

    // Endpoint to retrieve referral information for the current user
    @GetMapping("/my-referrals")
    public ResponseEntity<?> getMyReferrals(Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        List<ReferralInfo> referrals = userService.getReferralsForUser(currentUser.getId());
        return ResponseEntity.ok(referrals);
    }
}

// ReferralInfo DTO
public class ReferralInfo {
    private String referredUsername;
    private LocalDate dateReferred;
    private Boolean hasBooked;
    // ... getters and setters ...
}

// Security Configuration for Referral Dashboard
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ... existing security configuration ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // ... other security configurations ...
            .authorizeRequests()
            .antMatchers("/referral-dashboard/**").authenticated()
            // ... other matchers ...
    }
}

// Exception Handling for Referral Process
@ControllerAdvice
public class GlobalExceptionHandler {
    // ... existing exception handlers ...

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
// Frontend Components for Referral Dashboard (HTML, CSS, JavaScript)
// Place these files in the 'src/main/resources/static' directory

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    // ... other property fields ...
}

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... other booking fields ...
}

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal pricePerNight;
    // ... other pricing fields ...
}
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom query methods for properties
}

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods for bookings
}

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Custom query methods for availability
}

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Custom query methods for pricing
}
@Service
public class BookingService {
    // Autowire repositories and implement methods for:
    // - Creating bookings
    // - Checking availability
    // - Calculating pricing
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto) {
        // Endpoint to create a new booking
    }

    // ... other endpoints for managing bookings ...
}
@Entity
public class User {
    // ... existing fields ...

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String profilePictureUrl;

    // ... getters and setters ...
}
@Service
public class UserService {
    // Autowire UserRepository and implement methods for:
    // - Updating user profile details
    // - Handling photo uploads
}
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    @PostMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        // Endpoint to update user profile
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // Endpoint to upload a user's photo
    }
}
public class BookingDto {
    private Long userId;
    private Long propertyId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... getters and setters ...
}

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // ... getters and setters ...
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.uploadUserPhoto(id, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }
}


// MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Inject services as needed

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending a message
        return ResponseEntity.ok("Message sent successfully");
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields
    private String email; // Add email field

    // Generate a referral code for a new user
    public static String generateReferralCode() {
        // Logic to generate a unique referral code
    }

    // Add referral information when a user is referred
    public void addReferral(Long referredById) {
        this.referredBy = referredById;
        // Additional logic if needed
    }

    // Reward the referrer with points
    public void rewardReferrer(User referrer) {
        referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Example reward
        // Additional logic if needed
    }

    // Getters and setters for new fields
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

// UserNotFoundException.java (Custom Exception)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// MessageDto.java (Data Transfer Object)
public class MessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    // Getters and setters
}

// Main Application Entry Point
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Generate a unique referral code
    @PrePersist
    protected void onCreate() {
        referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    // ... other methods ...
}

// UserRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Process a referral
    public void processReferral(String referralCode, Long referredById) {
        User referredUser = findUserByEmail(referralCode);
        referredUser.setReferredBy(referredById);
        userRepository.save(referredUser);

        // Reward the referrer
        userRepository.findById(referredById).ifPresent(referrer -> {
            referrer.setRewardPoints(referrer.getRewardPoints() + 10);
            userRepository.save(referrer);
        });
    }
}

// UserController
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set user details from DTO
        userService.saveUser(newUser);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestParam String referralCode, Principal principal) {
        User referrer = userService.findUserByEmail(principal.getName());
        userService.processReferral(referralCode, referrer.getId());
        return ResponseEntity.ok("Referral processed successfully");
    }
}

// UserDto
public class UserDto {
    private String username;
    private String email;
    private String password;
    // Getters and setters
}

// Security Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Security configuration methods
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

// WebSocket Configuration
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket configuration methods
}

// HTML, CSS, and JavaScript files
// Place your HTML, CSS, and JavaScript files in the 'src/main/resources/static' directory
@Entity
public class Availability {
    // Fields: propertyId, startDate, endDate, isAvailable, etc.
    // Getters and setters
}

@Entity
public class Pricing {
    // Fields: propertyId, basePrice, seasonalAdjustments, etc.
    // Getters and setters
}

@Entity
public class Payment {
    // Fields: userId, bookingId, amount, paymentStatus, etc.
    // Getters and setters
}

@Entity
public class UserDetails {
    // Fields: userId, firstName, lastName, address, phone, etc.
    // Getters and setters
}
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Methods for availability management
}

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Methods for pricing management
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Methods for payment management
}

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Methods for user details management
}
@Service
public class AvailabilityService {
    // Methods for managing property availability
}

@Service
public class PricingService {
    // Methods for managing property pricing
}

@Service
public class PaymentService {
    // Methods for handling payments
}

@Service
public class UserDetailsService {
    // Methods for managing user details
}
@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    // Endpoints for managing property availability
}

@RestController
@RequestMapping("/pricing")
public class PricingController {
    // Endpoints for managing property pricing
}

@RestController
@RequestMapping("/payment")
public class PaymentController {
    // Endpoints for handling payments
}

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    //


// User Entity with Referral Information
@Entity
public class User {
    // ... existing fields ...

    // Method to generate a unique referral code for each user
    @PrePersist
    private void initializeReferralCode() {
        this.referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // ... getters and setters ...
}

// UserService with Referral Logic
@Service
public class UserService {
    // ... existing methods ...

    // Method to process a referral when a new user signs up
    public void processReferral(String referralCode) {
        User referrer = userRepository.findByReferralCode(referralCode)
            .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));
        referrer.setRewardPoints(referrer.getRewardPoints() + 10);
        userRepository.save(referrer);
    }

    // ... other methods ...
}

// UserController with Sign-up and Referral Endpoints
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    // Endpoint to handle user sign-up with referral code
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set newUser properties from userDto
        newUser = userService.saveUser(newUser);

        // If a referral code is provided, process the referral
        if (userDto.getReferralCode() != null) {
            userService.processReferral(userDto.getReferralCode());
        }

        return ResponseEntity.ok("User signed up successfully");
    }

    // ... other endpoints ...
}

// UserDto with Referral Code
public class UserDto {
    // ... existing fields ...
    private String referralCode;
    // ... getters and setters ...
}

// Referral Dashboard Controller
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private UserService userService;

    // Endpoint to retrieve referral information for the current user
    @GetMapping("/my-referrals")
    public ResponseEntity<?> getMyReferrals(Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        List<ReferralInfo> referrals = userService.getReferralsForUser(currentUser.getId());
        return ResponseEntity.ok(referrals);
    }
}

// ReferralInfo DTO
public class ReferralInfo {
    private String referredUsername;
    private LocalDate dateReferred;
    private Boolean hasBooked;
    // ... getters and setters ...
}

// Security Configuration for Referral Dashboard
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ... existing security configuration ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // ... other security configurations ...
            .authorizeRequests()
            .antMatchers("/referral-dashboard/**").authenticated()
            // ... other matchers ...
    }
}

// Exception Handling for Referral Process
@ControllerAdvice
public class GlobalExceptionHandler {
    // ... existing exception handlers ...

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
// Frontend Components for Referral Dashboard (HTML, CSS, JavaScript)
// Place these files in the 'src/main/resources/static' directory

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    // ... other property fields ...
}

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... other booking fields ...
}

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal pricePerNight;
    // ... other pricing fields ...
}
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom query methods for properties
}

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods for bookings
}

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Custom query methods for availability
}

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Custom query methods for pricing
}
@Service
public class BookingService {
    // Autowire repositories and implement methods for:
    // - Creating bookings
    // - Checking availability
    // - Calculating pricing
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto) {
        // Endpoint to create a new booking
    }

    // ... other endpoints for managing bookings ...
}
@Entity
public class User {
    // ... existing fields ...

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String profilePictureUrl;

    // ... getters and setters ...
}
@Service
public class UserService {
    // Autowire UserRepository and implement methods for:
    // - Updating user profile details
    // - Handling photo uploads
}
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    @PostMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        // Endpoint to update user profile
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // Endpoint to upload a user's photo
    }
}
public class BookingDto {
    private Long userId;
    private Long propertyId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... getters and setters ...
}

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // ... getters and setters ...
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.uploadUserPhoto(id, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }
}

// MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Inject services as needed

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending a message
        return ResponseEntity.ok("Message sent successfully");
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields
    private String email; // Add email field

    // Generate a referral code for a new user
    public static String generateReferralCode() {
        // Logic to generate a unique referral code
    }

    // Add referral information when a user is referred
    public void addReferral(Long referredById) {
        this.referredBy = referredById;
        // Additional logic if needed
    }

    // Reward the referrer with points
    public void rewardReferrer(User referrer) {
        referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Example reward
        // Additional logic if needed
    }

    // Getters and setters for new fields
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

// UserNotFoundException.java (Custom Exception)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// MessageDto.java (Data Transfer Object)
public class MessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    // Getters and setters
}

// Main Application Entry Point
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Generate a unique referral code
    @PrePersist
    protected void onCreate() {
        referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    // ... other methods ...
}

// UserRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Process a referral
    public void processReferral(String referralCode, Long referredById) {
        User referredUser = findUserByEmail(referralCode);
        referredUser.setReferredBy(referredById);
        userRepository.save(referredUser);

        // Reward the referrer
        userRepository.findById(referredById).ifPresent(referrer -> {
            referrer.setRewardPoints(referrer.getRewardPoints() + 10);
            userRepository.save(referrer);
        });
    }
}

// UserController
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set user details from DTO
        userService.saveUser(newUser);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestParam String referralCode, Principal principal) {
        User referrer = userService.findUserByEmail(principal.getName());
        userService.processReferral(referralCode, referrer.getId());
        return ResponseEntity.ok("Referral processed successfully");
    }
}

// UserDto
public class UserDto {
    private String username;
    private String email;
    private String password;
    // Getters and setters
}

// Security Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Security configuration methods
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

// WebSocket Configuration
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket configuration methods
}

// HTML, CSS, and JavaScript files
// Place your HTML, CSS, and JavaScript files in the 'src/main/resources/static' directory
@Entity
public class Availability {
    // Fields: propertyId, startDate, endDate, isAvailable, etc.
    // Getters and setters
}

@Entity
public class Pricing {
    // Fields: propertyId, basePrice, seasonalAdjustments, etc.
    // Getters and setters
}

@Entity
public class Payment {
    // Fields: userId, bookingId, amount, paymentStatus, etc.
    // Getters and setters
}

@Entity
public class UserDetails {
    // Fields: userId, firstName, lastName, address, phone, etc.
    // Getters and setters
}
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Methods for availability management
}

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Methods for pricing management
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Methods for payment management
}

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Methods for user details management
}
@Service
public class AvailabilityService {
    // Methods for managing property availability
}

@Service
public class PricingService {
    // Methods for managing property pricing
}

@Service
public class PaymentService {
    // Methods for handling payments
}

@Service
public class UserDetailsService {
    // Methods for managing user details
}
@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    // Endpoints for managing property availability
}

@RestController
@RequestMapping("/pricing")
public class PricingController {
    // Endpoints for managing property pricing
}

@RestController
@RequestMapping("/payment")
public class PaymentController {
    // Endpoints for handling payments
}

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    //

// User Entity with Referral Information
@Entity
public class User {
    // ... existing fields ...

    // Method to generate a unique referral code for each user
    @PrePersist
    private void initializeReferralCode() {
        this.referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // ... getters and setters ...
}

// UserService with Referral Logic
@Service
public class UserService {
    // ... existing methods ...

    // Method to process a referral when a new user signs up
    public void processReferral(String referralCode) {
        User referrer = userRepository.findByReferralCode(referralCode)
            .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));
        referrer.setRewardPoints(referrer.getRewardPoints() + 10);
        userRepository.save(referrer);
    }

    // ... other methods ...
}

// UserController with Sign-up and Referral Endpoints
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    // Endpoint to handle user sign-up with referral code
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set newUser properties from userDto
        newUser = userService.saveUser(newUser);

        // If a referral code is provided, process the referral
        if (userDto.getReferralCode() != null) {
            userService.processReferral(userDto.getReferralCode());
        }

        return ResponseEntity.ok("User signed up successfully");
    }

    // ... other endpoints ...
}getConversationHistory

// UserDto with Referral Code
public class UserDto {
    // ... existing fields ...
    private String referralCode;
    // ... getters and setters ...
}

// Referral Dashboard Controller
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private UserService userService;

    // Endpoint to retrieve referral information for the current user
    @GetMapping("/my-referrals")
    public ResponseEntity<?> getMyReferrals(Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        List<ReferralInfo> referrals = userService.getReferralsForUser(currentUser.getId());
        return ResponseEntity.ok(referrals);
    }
}

// ReferralInfo DTO
public class ReferralInfo {
    private String referredUsername;
    private LocalDate dateReferred;
    private Boolean hasBooked;
    // ... getters and setters ...
}

// Security Configuration for Referral Dashboard
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ... existing security configuration ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // ... other security configurations ...
            .authorizeRequests()
            .antMatchers("/referral-dashboard/**").authenticated()
            // ... other matchers ...
    }
}

// Exception Handling for Referral Process
@ControllerAdvice
public class GlobalExceptionHandler {
    // ... existing exception handlers ...

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

// Frontend Components for Referral Dashboard (HTML, CSS, JavaScript)
// Place these files in the 'src/main/resources/static' directory

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    // ... other property fields ...
}

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... other booking fields ...
}

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal pricePerNight;
    // ... other pricing fields ...
}
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom query methods for properties
}

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods for bookings
}

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Custom query methods for availability
}

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Custom query methods for pricing
}
@Service
public class BookingService {
    // Autowire repositories and implement methods for:
    // - Creating bookings
    // - Checking availability
    // - Calculating pricing
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto) {
        // Endpoint to create a new booking
    }

    // ... other endpoints for managing bookings ...
}
@Entity
public class User {
    // ... existing fields ...

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String profilePictureUrl;

    // ... getters and setters ...
}
@Service
public class UserService {
    // Autowire UserRepository and implement methods for:
    // - Updating user profile details
    // - Handling photo uploads
}
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    @PostMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        // Endpoint to update user profile
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // Endpoint to upload a user's photo
    }
}
public class BookingDto {
    private Long userId;
    private Long propertyId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... getters and setters ...
}

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // ... getters and setters ...
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.uploadUserPhoto(id, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }
}
// BookingController.java
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        Booking updatedBooking = bookingService.updateBooking(id, bookingRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}

// UserProfileController.java
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody UserProfile userProfileDetails) {
        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userId, userProfileDetails);
        return ResponseEntity.ok(updatedUserProfile);
    }
}

// ReferralDashboardController.java
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private ReferralService referralService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getReferralDetails(@PathVariable Long userId) {
        ReferralDashboard referralDashboard = referralService.getReferralDashboard(userId);
        return ResponseEntity.ok(referralDashboard);
    }
}

// Frontend Components (Assuming these files are located in the 'src/main/resources/static' directory)

// UserController.java with backend validation and error handling
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        if (!userDto.isValid()) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }
        if (!userDto.isTermsAccepted()) {
            return ResponseEntity.badRequest().body("Terms and conditions not accepted");
        }
        try {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }
}

// UserDto.java with consent storage
public class UserDto {
    private String username;
    private String email;
    private String password;
    private boolean termsAccepted;

    public boolean isValid() {
        // Implement validation logic
    }

    // Getters and setters
}


// MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Inject services as needed

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending a message
        return ResponseEntity.ok("Message sent successfully");
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields
    private String email; // Add email field

    // Generate a referral code for a new user
    public static String generateReferralCode() {
        // Logic to generate a unique referral code
    }

    // Add referral information when a user is referred
    public void addReferral(Long referredById) {
        this.referredBy = referredById;
        // Additional logic if needed
    }

    // Reward the referrer with points
    public void rewardReferrer(User referrer) {
        referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Example reward
        // Additional logic if needed
    }

    // Getters and setters for new fields
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

// UserNotFoundException.java (Custom Exception)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// MessageDto.java (Data Transfer Object)
public class MessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    // Getters and setters
}

// Main Application Entry Point
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Generate a unique referral code
    @PrePersist
    protected void onCreate() {
        referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    // ... other methods ...
}

// UserRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Process a referral
    public void processReferral(String referralCode, Long referredById) {
        User referredUser = findUserByEmail(referralCode);
        referredUser.setReferredBy(referredById);
        userRepository.save(referredUser);

        // Reward the referrer
        userRepository.findById(referredById).ifPresent(referrer -> {
            referrer.setRewardPoints(referrer.getRewardPoints() + 10);
            userRepository.save(referrer);
        });
    }
}

// UserController
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set user details from DTO
        userService.saveUser(newUser);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestParam String referralCode, Principal principal) {
        User referrer = userService.findUserByEmail(principal.getName());
        userService.processReferral(referralCode, referrer.getId());
        return ResponseEntity.ok("Referral processed successfully");
    }
}

// UserDto
public class UserDto {
    private String username;
    private String email;
    private String password;
    // Getters and setters
}

// Security Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Security configuration methods
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

// WebSocket Configuration
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket configuration methods
}

// HTML, CSS, and JavaScript files
// Place your HTML, CSS, and JavaScript files in the 'src/main/resources/static' directory
@Entity
public class Availability {
    // Fields: propertyId, startDate, endDate, isAvailable, etc.
    // Getters and setters
}

@Entity
public class Pricing {
    // Fields: propertyId, basePrice, seasonalAdjustments, etc.
    // Getters and setters
}

@Entity
public class Payment {
    // Fields: userId, bookingId, amount, paymentStatus, etc.
    // Getters and setters
}

@Entity
public class UserDetails {
    // Fields: userId, firstName, lastName, address, phone, etc.
    // Getters and setters
}
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Methods for availability management
}

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Methods for pricing management
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Methods for payment management
}

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Methods for user details management
}
@Service
public class AvailabilityService {
    // Methods for managing property availability
}

@Service
public class PricingService {
    // Methods for managing property pricing
}

@Service
public class PaymentService {
    // Methods for handling payments
}

@Service
public class UserDetailsService {
    // Methods for managing user details
}
@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    // Endpoints for managing property availability
}

@RestController
@RequestMapping("/pricing")
public class PricingController {
    // Endpoints for managing property pricing
}

@RestController
@RequestMapping("/payment")
public class PaymentController {
    // Endpoints for handling payments
}

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    //


// User Entity with Referral Information
@Entity
public class User {
    // ... existing fields ...

    // Method to generate a unique referral code for each user
    @PrePersist
    private void initializeReferralCode() {
        this.referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // ... getters and setters ...
}

// UserService with Referral Logic
@Service
public class UserService {
    // ... existing methods ...

    // Method to process a referral when a new user signs up
    public void processReferral(String referralCode) {
        User referrer = userRepository.findByReferralCode(referralCode)
            .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));
        referrer.setRewardPoints(referrer.getRewardPoints() + 10);
        userRepository.save(referrer);
    }

    // ... other methods ...
}

// UserController with Sign-up and Referral Endpoints
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    // Endpoint to handle user sign-up with referral code
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set newUser properties from userDto
        newUser = userService.saveUser(newUser);

        // If a referral code is provided, process the referral
        if (userDto.getReferralCode() != null) {
            userService.processReferral(userDto.getReferralCode());
        }

        return ResponseEntity.ok("User signed up successfully");
    }

    // ... other endpoints ...
}

// UserDto with Referral Code
public class UserDto {
    // ... existing fields ...
    private String referralCode;
    // ... getters and setters ...
}

// Referral Dashboard Controller
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private UserService userService;

    // Endpoint to retrieve referral information for the current user
    @GetMapping("/my-referrals")
    public ResponseEntity<?> getMyReferrals(Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        List<ReferralInfo> referrals = userService.getReferralsForUser(currentUser.getId());
        return ResponseEntity.ok(referrals);
    }
}

// ReferralInfo DTO
public class ReferralInfo {
    private String referredUsername;
    private LocalDate dateReferred;
    private Boolean hasBooked;
    // ... getters and setters ...
}

// Security Configuration for Referral Dashboard
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ... existing security configuration ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // ... other security configurations ...
            .authorizeRequests()
            .antMatchers("/referral-dashboard/**").authenticated()
            // ... other matchers ...
    }
}

// Exception Handling for Referral Process
@ControllerAdvice
public class GlobalExceptionHandler {
    // ... existing exception handlers ...

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

// Frontend Components for Referral Dashboard (HTML, CSS, JavaScript)
// Place these files in the 'src/main/resources/static' directory

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    // ... other property fields ...
}

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... other booking fields ...
}

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal pricePerNight;
    // ... other pricing fields ...
}
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom query methods for properties
}

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods for bookings
}

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Custom query methods for availability
}

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Custom query methods for pricing
}
@Service
public class BookingService {
    // Autowire repositories and implement methods for:
    // - Creating bookings
    // - Checking availability
    // - Calculating pricing
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto) {
        // Endpoint to create a new booking
    }

    // ... other endpoints for managing bookings ...
}
@Entity
public class User {
    // ... existing fields ...

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String profilePictureUrl;

    // ... getters and setters ...
}
@Service
public class UserService {
    // Autowire UserRepository and implement methods for:
    // - Updating user profile details
    // - Handling photo uploads
}
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    @PostMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        // Endpoint to update user profile
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // Endpoint to upload a user's photo
    }
}
public class BookingDto {
    private Long userId;
    private Long propertyId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... getters and setters ...
}

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // ... getters and setters ...
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.uploadUserPhoto(id, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }
}
// BookingController.java
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        Booking updatedBooking = bookingService.updateBooking(id, bookingRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}

// UserProfileController.java
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody UserProfile userProfileDetails) {
        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userId, userProfileDetails);
        return ResponseEntity.ok(updatedUserProfile);
    }
}

// ReferralDashboardController.java
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private ReferralService referralService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getReferralDetails(@PathVariable Long userId) {
        ReferralDashboard referralDashboard = referralService.getReferralDashboard(userId);
        return ResponseEntity.ok(referralDashboard);
    }
}

// Frontend Components (Assuming these files are located in the 'src/main/resources/static' directory)

// UserController.java with backend validation and error handling
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        if (!userDto.isValid()) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }
        if (!userDto.isTermsAccepted()) {
            return ResponseEntity.badRequest().body("Terms and conditions not accepted");
        }
        try {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }
}

// UserDto.java with consent storage
public class UserDto {
    private String username;
    private String email;
    private String password;
    private boolean termsAccepted;

    public boolean isValid() {
        // Implement validation logic
    }

    // Getters and setters
}

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    private PropertyType propertyType; // Enum: APARTMENT, HOUSE, etc.
    private int maxOccupancy;
    // ... other property fields ...
}
@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
    // ... other availability fields ...
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal nightlyRate;
    private int minimumStay;
    // ... other pricing fields ...
}
@Service
public class BookingService {
    // ... existing methods ...

    public List<Property> searchAvailableProperties(LocalDate checkIn, LocalDate checkOut, String location) {
        // Implement property search logic based on availability and location
    }

    public Booking createShortTermBooking(Long userId, Long propertyId, LocalDate checkIn, LocalDate checkOut) {
        // Implement booking creation for short-term rentals
    }
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchAvailableProperties(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam String location) {
        List<Property> availableProperties = bookingService.searchAvailableProperties(checkIn, checkOut, location);
        return ResponseEntity.ok(availableProperties)
// UserController.java with backend validation and error handling
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        if (!userDto.isValid()) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }
        if (!userDto.isTermsAccepted()) {
            return ResponseEntity.badRequest().body("Terms and conditions not accepted");
        }
        try {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }
}

// UserDto.java with consent storage
public class UserDto {
    private String username;
    private String email;
    private String password;
    private boolean termsAccepted;

    public boolean isValid() {
        // Implement validation logic
    }

    // Getters and setters
}

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    private PropertyType propertyType; // Enum: APARTMENT, HOUSE, etc.
    private int maxOccupancy;
    // ... other property fields ...
}
@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
    // ... other availability fields ...
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal nightlyRate;
    private int minimumStay;
    // ... other pricing fields ...
}
@Service
public class BookingService {
    // ... existing methods ...

    public List<Property> searchAvailableProperties(LocalDate checkIn, LocalDate checkOut, String location) {
        // Implement property search logic based on availability and location
    }

    public Booking createShortTermBooking(Long userId, Long propertyId, LocalDate checkIn, LocalDate checkOut) {
        // Implement booking creation for short-term rentals
    }
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchAvailableProperties(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam String location) {
        List<Property> availableProperties = bookingService.searchAvailableProperties(checkIn, checkOut, location);
        return ResponseEntity.ok(availableProperties);
    }

    @PostMapping
    public ResponseEntity<?> createShortTermBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createShortTermBooking(
                bookingRequest.getUserId(),
                bookingRequest.getPropertyId(),
                bookingRequest.getCheckIn(),
                bookingRequest.getCheckOut());
        return ResponseEntity.ok(booking);
    }
}

// MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Inject services as needed

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending a message
        return ResponseEntity.ok("Message sent successfully");
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields
    private String email; // Add email field

    // Generate a referral code for a new user
    public static String generateReferralCode() {
        // Logic to generate a unique referral code
    }

    // Add referral information when a user is referred
    public void addReferral(Long referredById) {
        this.referredBy = referredById;
        // Additional logic if needed
    }

    // Reward the referrer with points
    public void rewardReferrer(User referrer) {
        referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Example reward
        // Additional logic if needed
    }

    // Getters and setters for new fields
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

// UserNotFoundException.java (Custom Exception)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// MessageDto.java (Data Transfer Object)
public class MessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    // Getters and setters
}

// Main Application Entry Point
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Generate a unique referral code
    @PrePersist
    protected void onCreate() {
        referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    // ... other methods ...
}

// UserRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Process a referral
    public void processReferral(String referralCode, Long referredById) {
        User referredUser = findUserByEmail(referralCode);
        referredUser.setReferredBy(referredById);
        userRepository.save(referredUser);

        // Reward the referrer
        userRepository.findById(referredById).ifPresent(referrer -> {
            referrer.setRewardPoints(referrer.getRewardPoints() + 10);
            userRepository.save(referrer);
        });
    }
}

// UserController
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set user details from DTO
        userService.saveUser(newUser);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestParam String referralCode, Principal principal) {
        User referrer = userService.findUserByEmail(principal.getName());
        userService.processReferral(referralCode, referrer.getId());
        return ResponseEntity.ok("Referral processed successfully");
    }
}

// UserDto
public class UserDto {
    private String username;
    private String email;
    private String password;
    // Getters and setters
}

// Security Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Security configuration methods
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

// WebSocket Configuration
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket configuration methods
}

// HTML, CSS, and JavaScript files
// Place your HTML, CSS, and JavaScript files in the 'src/main/resources/static' directory
@Entity
public class Availability {
    // Fields: propertyId, startDate, endDate, isAvailable, etc.
    // Getters and setters
}

@Entity
public class Pricing {
    // Fields: propertyId, basePrice, seasonalAdjustments, etc.
    // Getters and setters
}

@Entity
public class Payment {
    // Fields: userId, bookingId, amount, paymentStatus, etc.
    // Getters and setters
}

@Entity
public class UserDetails {
    // Fields: userId, firstName, lastName, address, phone, etc.
    // Getters and setters
}
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Methods for availability management
}

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Methods for pricing management
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Methods for payment management
}

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Methods for user details management
}
@Service
public class AvailabilityService {
    // Methods for managing property availability
}

@Service
public class PricingService {
    // Methods for managing property pricing
}

@Service
public class PaymentService {
    // Methods for handling payments
}

@Service
public class UserDetailsService {
    // Methods for managing user details
}
@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    // Endpoints for managing property availability
}

@RestController
@RequestMapping("/pricing")
public class PricingController {
    // Endpoints for managing property pricing
}

@RestController
@RequestMapping("/payment")
public class PaymentController {
    // Endpoints for handling payments
}

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    //

// User Entity with Referral Information
@Entity
public class User {
    // ... existing fields ...

    // Method to generate a unique referral code for each user
    @PrePersist
    private void initializeReferralCode() {
        this.referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // ... getters and setters ...
}

// UserService with Referral Logic
@Service
public class UserService {
    // ... existing methods ...

    // Method to process a referral when a new user signs up
    public void processReferral(String referralCode) {
        User referrer = userRepository.findByReferralCode(referralCode)
            .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));
        referrer.setRewardPoints(referrer.getRewardPoints() + 10);
        userRepository.save(referrer);
    }

    // ... other methods ...
}

// UserController with Sign-up and Referral Endpoints
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    // Endpoint to handle user sign-up with referral code
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set newUser properties from userDto
        newUser = userService.saveUser(newUser);

        // If a referral code is provided, process the referral
        if (userDto.getReferralCode() != null) {
            userService.processReferral(userDto.getReferralCode());
        }

        return ResponseEntity.ok("User signed up successfully");
    }

    // ... other endpoints ...
}

// UserDto with Referral Code
public class UserDto {
    // ... existing fields ...
    private String referralCode;
    // ... getters and setters ...
}

// Referral Dashboard Controller
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private UserService userService;

    // Endpoint to retrieve referral information for the current user
    @GetMapping("/my-referrals")
    public ResponseEntity<?> getMyReferrals(Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        List<ReferralInfo> referrals = userService.getReferralsForUser(currentUser.getId());
        return ResponseEntity.ok(referrals);
    }
}

// ReferralInfo DTO
public class ReferralInfo {
    private String referredUsername;
    private LocalDate dateReferred;
    private Boolean hasBooked;
    // ... getters and setters ...
}

// Security Configuration for Referral Dashboard
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ... existing security configuration ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // ... other security configurations ...
            .authorizeRequests()
            .antMatchers("/referral-dashboard/**").authenticated()
            // ... other matchers ...
    }
}

// Exception Handling for Referral Process
@ControllerAdvice
public class GlobalExceptionHandler {
    // ... existing exception handlers ...

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    // ... other property fields ...
}

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... other booking fields ...
}

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal pricePerNight;
    // ... other pricing fields ...
}
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom query methods for properties
}

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods for bookings
}

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Custom query methods for availability
}

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Custom query methods for pricing
}
@Service
public class BookingService {
    // Autowire repositories and implement methods for:
    // - Creating bookings
    // - Checking availability
    // - Calculating pricing
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto) {
        // Endpoint to create a new booking
    }

    // ... other endpoints for managing bookings ...
}
@Entity
public class User {
    // ... existing fields ...

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String profilePictureUrl;

    // ... getters and setters ...
}
@Service
public class UserService {
    // Autowire UserRepository and implement methods for:
    // - Updating user profile details
    // - Handling photo uploads
}
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    @PostMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        // Endpoint to update user profile
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // Endpoint to upload a user's photo
    }
}
public class BookingDto {
    private Long userId;
    private Long propertyId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... getters and setters ...
}

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // ... getters and setters ...
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.uploadUserPhoto(id, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }
}
// BookingController.java
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        Booking updatedBooking = bookingService.updateBooking(id, bookingRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}

// UserProfileController.java
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody UserProfile userProfileDetails) {
        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userId, userProfileDetails);
        return ResponseEntity.ok(updatedUserProfile);
    }
}

// ReferralDashboardController.java
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private ReferralService referralService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getReferralDetails(@PathVariable Long userId) {
        ReferralDashboard referralDashboard = referralService.getReferralDashboard(userId);
        return ResponseEntity.ok(referralDashboard);
    }
}



// UserController.java with backend validation and error handling
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        if (!userDto.isValid()) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }
        if (!userDto.isTermsAccepted()) {
            return ResponseEntity.badRequest().body("Terms and conditions not accepted");
        }
        try {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }
}

// UserDto.java with consent storage
public class UserDto {
    private String username;
    private String email;
    private String password;
    private boolean termsAccepted;

    public boolean isValid() {
        // Implement validation logic
    }

    // Getters and setters
}

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    private PropertyType propertyType; // Enum: APARTMENT, HOUSE, etc.
    private int maxOccupancy;
    // ... other property fields ...
}
@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
    // ... other availability fields ...
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal nightlyRate;
    private int minimumStay;
    // ... other pricing fields ...
}
@Service
public class BookingService {
    // ... existing methods ...

    public List<Property> searchAvailableProperties(LocalDate checkIn, LocalDate checkOut, String location) {
        // Implement property search logic based on availability and location
    }

    public Booking createShortTermBooking(Long userId, Long propertyId, LocalDate checkIn, LocalDate checkOut) {
        // Implement booking creation for short-term rentals
    }
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchAvailableProperties(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam String location) {
        List<Property> availableProperties = bookingService.searchAvailableProperties(checkIn, checkOut, location);
        return ResponseEntity.ok(availableProperties);
    }

    @PostMapping
    public ResponseEntity<?> createShortTermBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createShortTermBooking(
                bookingRequest.getUserId(),
                bookingRequest.getPropertyId(),
                bookingRequest.getCheckIn(),
                bookingRequest.getCheckOut());
        return ResponseEntity.ok(booking);
    }
}

@Entity
public class UserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @OneToOne
    private User user;
   
    private String photoUrl; // URL to access the photo
   
    // Getters and setters
}
@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
    Optional<UserPhoto> findByUserId(Long userId);
}
@Service
public class UserPhotoService {
    @Autowired
    private UserPhotoRepository userPhotoRepository;
   
    public String uploadPhoto(MultipartFile file, Long userId) {
        // Logic to store the photo and return the URL
    }
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserPhotoService userPhotoService;
   
    @PostMapping("/{userId}/photo")
    public ResponseEntity<?> uploadUserPhoto(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        String photoUrl = userPhotoService.uploadPhoto(file, userId);
        return ResponseEntity.ok(photoUrl);
    }
}
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    private String name;
    private String description; // Short description of the property
    private BigDecimal pricePerNight; // Pricing information
   
    // Getters and setters
}
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Additional query methods if needed
}
@Service
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;
   
    public Property createOrUpdateProperty(Property property) {
        // Logic to save or update property details
    }
}
@RestController
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;
   
    @PostMapping
    public ResponseEntity<?> createOrUpdateProperty(@RequestBody Property property) {
        Property savedProperty = propertyService.createOrUpdateProperty(property);
        return ResponseEntity.ok(savedProperty);
    }
}


// MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Inject services as needed

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending a message
        return ResponseEntity.ok("Message sent successfully");
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields
    private String email; // Add email field

    // Generate a referral code for a new user
    public static String generateReferralCode() {
        // Logic to generate a unique referral code
    }

    // Add referral information when a user is referred
    public void addReferral(Long referredById) {
        this.referredBy = referredById;
        // Additional logic if needed
    }

    // Reward the referrer with points
    public void rewardReferrer(User referrer) {
        referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Example reward
        // Additional logic if needed
    }

    // Getters and setters for new fields
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

// UserNotFoundException.java (Custom Exception)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// MessageDto.java (Data Transfer Object)
public class MessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    // Getters and setters
}

// Main Application Entry Point
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Generate a unique referral code
    @PrePersist
    protected void onCreate() {
        referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    // ... other methods ...
}

// UserRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Process a referral
    public void processReferral(String referralCode, Long referredById) {
        User referredUser = findUserByEmail(referralCode);
        referredUser.setReferredBy(referredById);
        userRepository.save(referredUser);

        // Reward the referrer
        userRepository.findById(referredById).ifPresent(referrer -> {
            referrer.setRewardPoints(referrer.getRewardPoints() + 10);
            userRepository.save(referrer);
        });
    }
}

// UserController
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set user details from DTO
        userService.saveUser(newUser);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestParam String referralCode, Principal principal) {
        User referrer = userService.findUserByEmail(principal.getName());
        userService.processReferral(referralCode, referrer.getId());
        return ResponseEntity.ok("Referral processed successfully");
    }
}

// UserDto
public class UserDto {
    private String username;
    private String email;
    private String password;
    // Getters and setters
}

// Security Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Security configuration methods
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

// WebSocket Configuration
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket configuration methods
}

// HTML, CSS, and JavaScript files
// Place your HTML, CSS, and JavaScript files in the 'src/main/resources/static' directory
@Entity
public class Availability {
    // Fields: propertyId, startDate, endDate, isAvailable, etc.
    // Getters and setters
}

@Entity
public class Pricing {
    // Fields: propertyId, basePrice, seasonalAdjustments, etc.
    // Getters and setters
}

@Entity
public class Payment {
    // Fields: userId, bookingId, amount, paymentStatus, etc.
    // Getters and setters
}

@Entity
public class UserDetails {
    // Fields: userId, firstName, lastName, address, phone, etc.
    // Getters and setters
}
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Methods for availability management
}

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Methods for pricing management
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Methods for payment management
}

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Methods for user details management
}
@Service
public class AvailabilityService {
    // Methods for managing property availability
}

@Service
public class PricingService {
    // Methods for managing property pricing
}

@Service
public class PaymentService {
    // Methods for handling payments
}

@Service
public class UserDetailsService {
    // Methods for managing user details
}
@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    // Endpoints for managing property availability
}

@RestController
@RequestMapping("/pricing")
public class PricingController {
    // Endpoints for managing property pricing
}

@RestController
@RequestMapping("/payment")
public class PaymentController {
    // Endpoints for handling payments
}

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    //

// User Entity with Referral Information
@Entity
public class User {
    // ... existing fields ...

    // Method to generate a unique referral code for each user
    @PrePersist
    private void initializeReferralCode() {
        this.referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // ... getters and setters ...
}

// UserService with Referral Logic
@Service
public class UserService {
    // ... existing methods ...

    // Method to process a referral when a new user signs up
    public void processReferral(String referralCode) {
        User referrer = userRepository.findByReferralCode(referralCode)
            .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));
        referrer.setRewardPoints(referrer.getRewardPoints() + 10);
        userRepository.save(referrer);
    }

    // ... other methods ...
}

// UserController with Sign-up and Referral Endpoints
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    // Endpoint to handle user sign-up with referral code
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set newUser properties from userDto
        newUser = userService.saveUser(newUser);

        // If a referral code is provided, process the referral
        if (userDto.getReferralCode() != null) {
            userService.processReferral(userDto.getReferralCode());
        }

        return ResponseEntity.ok("User signed up successfully");
    }

    // ... other endpoints ...
}

// UserDto with Referral Code
public class UserDto {
    // ... existing fields ...
    private String referralCode;
    // ... getters and setters ...
}

// Referral Dashboard Controller
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private UserService userService;

    // Endpoint to retrieve referral information for the current user
    @GetMapping("/my-referrals")
    public ResponseEntity<?> getMyReferrals(Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        List<ReferralInfo> referrals = userService.getReferralsForUser(currentUser.getId());
        return ResponseEntity.ok(referrals);
    }
}

// ReferralInfo DTO
public class ReferralInfo {
    private String referredUsername;
    private LocalDate dateReferred;
    private Boolean hasBooked;
    // ... getters and setters ...
}

// Security Configuration for Referral Dashboard
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ... existing security configuration ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // ... other security configurations ...
            .authorizeRequests()
            .antMatchers("/referral-dashboard/**").authenticated()
            // ... other matchers ...
    }
}

// Exception Handling for Referral Process
@ControllerAdvice
public class GlobalExceptionHandler {
    // ... existing exception handlers ...

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
 
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    // ... other property fields ...
}

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... other booking fields ...
}

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal pricePerNight;
    // ... other pricing fields ...
}
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom query methods for properties
}

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods for bookings
}

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Custom query methods for availability
}

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Custom query methods for pricing
}
@Service
public class BookingService {
    // Autowire repositories and implement methods for:
    // - Creating bookings
    // - Checking availability
    // - Calculating pricing
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto) {
        // Endpoint to create a new booking
    }

    // ... other endpoints for managing bookings ...
}
@Entity
public class User {
    // ... existing fields ...

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String profilePictureUrl;

    // ... getters and setters ...
}
@Service
public class UserService {
    // Autowire UserRepository and implement methods for:
    // - Updating user profile details
    // - Handling photo uploads
}
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    @PostMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        // Endpoint to update user profile
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // Endpoint to upload a user's photo
    }
}
public class BookingDto {
    private Long userId;
    private Long propertyId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... getters and setters ...
}

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // ... getters and setters ...
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.uploadUserPhoto(id, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }
}
// BookingController.java
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        Booking updatedBooking = bookingService.updateBooking(id, bookingRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}

// UserProfileController.java
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody UserProfile userProfileDetails) {
        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userId, userProfileDetails);
        return ResponseEntity.ok(updatedUserProfile);
    }
}

// ReferralDashboardController.java
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private ReferralService referralService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getReferralDetails(@PathVariable Long userId) {
        ReferralDashboard referralDashboard = referralService.getReferralDashboard(userId);
        return ResponseEntity.ok(referralDashboard);
    }
}

// UserController.java with backend validation and error handling
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        if (!userDto.isValid()) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }
        if (!userDto.isTermsAccepted()) {
            return ResponseEntity.badRequest().body("Terms and conditions not accepted");
        }
        try {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }
}

// UserDto.java with consent storage
public class UserDto {
    private String username;
    private String email;
    private String password;
    private boolean termsAccepted;

    public boolean isValid() {
        // Implement validation logic
    }

    // Getters and setters
}

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    private PropertyType propertyType; // Enum: APARTMENT, HOUSE, etc.
    private int maxOccupancy;
    // ... other property fields ...
}
@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
    // ... other availability fields ...
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal nightlyRate;
    private int minimumStay;
    // ... other pricing fields ...
}
@Service
public class BookingService {
    // ... existing methods ...

    public List<Property> searchAvailableProperties(LocalDate checkIn, LocalDate checkOut, String location) {
        // Implement property search logic based on availability and location
    }

    public Booking createShortTermBooking(Long userId, Long propertyId, LocalDate checkIn, LocalDate checkOut) {
        // Implement booking creation for short-term rentals
    }
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchAvailableProperties(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam String location) {
        List<Property> availableProperties = bookingService.searchAvailableProperties(checkIn, checkOut, location);
        return ResponseEntity.ok(availableProperties);
    }

    @PostMapping
    public ResponseEntity<?> createShortTermBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createShortTermBooking(
                bookingRequest.getUserId(),
                bookingRequest.getPropertyId(),
                bookingRequest.getCheckIn(),
                bookingRequest.getCheckOut());
        return ResponseEntity.ok(booking);
    }
}

@Entity
public class UserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @OneToOne
    private User user;
   
    private String photoUrl; // URL to access the photo
   
    // Getters and setters
}
@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
    Optional<UserPhoto> findByUserId(Long userId);
}
@Service
public class UserPhotoService {
    @Autowired
    private UserPhotoRepository userPhotoRepository;
   
    public String uploadPhoto(MultipartFile file, Long userId) {
        // Logic to store the photo and return the URL
    }
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserPhotoService userPhotoService;
   
    @PostMapping("/{userId}/photo")
    public ResponseEntity<?> uploadUserPhoto(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        String photoUrl = userPhotoService.uploadPhoto(file, userId);
        return ResponseEntity.ok(photoUrl);
    }
}
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    private String name;
    private String description; // Short description of the property
    private BigDecimal pricePerNight; // Pricing information
   
    // Getters and setters
}
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Additional query methods if needed
}
@Service
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;
   
    public Property createOrUpdateProperty(Property property) {
        // Logic to save or update property details
    }
}
@RestController
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;
   
    @PostMapping
    public ResponseEntity<?> createOrUpdateProperty(@RequestBody Property property) {
        Property savedProperty = propertyService.createOrUpdateProperty(property);
        return ResponseEntity.ok(savedProperty);
    }
}
// Example of a WebSocket configuration for real-time interactions
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }
}
// Example of a service for handling image management
@Service
public class ImageService {

    public String uploadImage(MultipartFile file) {
        // Logic to handle image upload
    }

    public byte[] getImage(String imageName) {
        // Logic to retrieve and return image data
    }
}
// Example of a caching strategy for performance optimization
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        // Configure and return an appropriate CacheManager
    }
}
// Example of a security configuration for secure payment processing
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/payments/**").authenticated()
            .and()
            .formLogin();
    }
}
// Example of a repository with custom search methods for scalability
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.location = :location AND p.availability = true")
    List<Property> findAvailableProperties(@Param("location") String location);
}
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}
@RestController
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    // Getters and setters

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
@Service
public class PushNotificationService {

    public void sendPushNotification(String deviceToken, String title, String body) {
        // Logic to send push notification using Firebase Cloud Messaging (FCM)
    }
}
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @ManyToOne
    private User user;
   
    @ManyToOne
    private Property property;
   
    private int rating;
    private String comment;

    // Getters and setters
}
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
}
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForProperty(Long propertyId) {
        // Logic to retrieve reviews for a property
    }
}
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.saveReview(review);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Review>> getReviewsForProperty(@PathVariable Long propertyId) {
        List<Review> reviews = reviewService.getReviewsForProperty(propertyId);
        return ResponseEntity.ok(reviews);
    }
}
@Service
public class PayPalService {

    public PaymentResponse createPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) {
        // Logic to create a payment using PayPal API
    }

    public PaymentResponse completePayment(String paymentId, String payerId) {
        // Logic to complete the payment after the user approves
    }
}
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse payment = payPalService.createPayment(
                paymentRequest.getTotal(),
                paymentRequest.getCurrency(),
                paymentRequest.getMethod(),
                paymentRequest.getIntent(),
                paymentRequest.getDescription(),
                paymentRequest.getCancelUrl(),
                paymentRequest.getSuccessUrl());
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        PaymentResponse payment = payPalService.completePayment(paymentId, payerId);
        return ResponseEntity.ok(payment);
    }
}



// MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Inject services as needed

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending a message
        return ResponseEntity.ok("Message sent successfully");
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields
    private String email; // Add email field

    // Generate a referral code for a new user
    public static String generateReferralCode() {
        // Logic to generate a unique referral code
    }

    // Add referral information when a user is referred
    public void addReferral(Long referredById) {
        this.referredBy = referredById;
        // Additional logic if needed
    }

    // Reward the referrer with points
    public void rewardReferrer(User referrer) {
        referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Example reward
        // Additional logic if needed
    }

    // Getters and setters for new fields
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

// UserNotFoundException.java (Custom Exception)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// MessageDto.java (Data Transfer Object)
public class MessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    // Getters and setters
}
// Main Application Entry Point
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Generate a unique referral code
    @PrePersist
    protected void onCreate() {
        referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    // ... other methods ...
}

// UserRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Process a referral
    public void processReferral(String referralCode, Long referredById) {
        User referredUser = findUserByEmail(referralCode);
        referredUser.setReferredBy(referredById);
        userRepository.save(referredUser);

        // Reward the referrer
        userRepository.findById(referredById).ifPresent(referrer -> {
            referrer.setRewardPoints(referrer.getRewardPoints() + 10);
            userRepository.save(referrer);
        });
    }
}

// UserController
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set user details from DTO
        userService.saveUser(newUser);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestParam String referralCode, Principal principal) {
        User referrer = userService.findUserByEmail(principal.getName());
        userService.processReferral(referralCode, referrer.getId());
        return ResponseEntity.ok("Referral processed successfully");
    }
}

// UserDto
public class UserDto {
    private String username;
    private String email;
    private String password;
    // Getters and setters
}

// Security Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Security configuration methods
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

// WebSocket Configuration
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket configuration methods
}

// HTML, CSS, and JavaScript files
// Place your HTML, CSS, and JavaScript files in the 'src/main/resources/static' directory
@Entity
public class Availability {
    // Fields: propertyId, startDate, endDate, isAvailable, etc.
    // Getters and setters
}

@Entity
public class Pricing {
    // Fields: propertyId, basePrice, seasonalAdjustments, etc.
    // Getters and setters
}

@Entity
public class Payment {
    // Fields: userId, bookingId, amount, paymentStatus, etc.
    // Getters and setters
}

@Entity
public class UserDetails {
    // Fields: userId, firstName, lastName, address, phone, etc.
    // Getters and setters
}
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Methods for availability management
}

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Methods for pricing management
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Methods for payment management
}

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Methods for user details management
}
@Service
public class AvailabilityService {
    // Methods for managing property availability
}

@Service
public class PricingService {
    // Methods for managing property pricing
}

@Service
public class PaymentService {
    // Methods for handling payments
}

@Service
public class UserDetailsService {
    // Methods for managing user details
}
@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    // Endpoints for managing property availability
}

@RestController
@RequestMapping("/pricing")
public class PricingController {
    // Endpoints for managing property pricing
}

@RestController
@RequestMapping("/payment")
public class PaymentController {
    // Endpoints for handling payments
}

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    //


// User Entity with Referral Information
@Entity
public class User {
    // ... existing fields ...

    // Method to generate a unique referral code for each user
    @PrePersist
    private void initializeReferralCode() {
        this.referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // ... getters and setters ...
}

// UserService with Referral Logic
@Service
public class UserService {
    // ... existing methods ...

    // Method to process a referral when a new user signs up
    public void processReferral(String referralCode) {
        User referrer = userRepository.findByReferralCode(referralCode)
            .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));
        referrer.setRewardPoints(referrer.getRewardPoints() + 10);
        userRepository.save(referrer);
    }

    // ... other methods ...
}

// UserController with Sign-up and Referral Endpoints
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    // Endpoint to handle user sign-up with referral code
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set newUser properties from userDto
        newUser = userService.saveUser(newUser);

        // If a referral code is provided, process the referral
        if (userDto.getReferralCode() != null) {
            userService.processReferral(userDto.getReferralCode());
        }

        return ResponseEntity.ok("User signed up successfully");
    }

    // ... other endpoints ...
}

// UserDto with Referral Code
public class UserDto {
    // ... existing fields ...
    private String referralCode;
    // ... getters and setters ...
}

// Referral Dashboard Controller
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private UserService userService;

    // Endpoint to retrieve referral information for the current user
    @GetMapping("/my-referrals")
    public ResponseEntity<?> getMyReferrals(Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        List<ReferralInfo> referrals = userService.getReferralsForUser(currentUser.getId());
        return ResponseEntity.ok(referrals);
    }
}

// ReferralInfo DTO
public class ReferralInfo {
    private String referredUsername;
    private LocalDate dateReferred;
    private Boolean hasBooked;
    // ... getters and setters ...
}

// Security Configuration for Referral Dashboard
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ... existing security configuration ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // ... other security configurations ...
            .authorizeRequests()
            .antMatchers("/referral-dashboard/**").authenticated()
            // ... other matchers ...
    }
}

// Exception Handling for Referral Process
@ControllerAdvice
public class GlobalExceptionHandler {
    // ... existing exception handlers ...

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    // ... other property fields ...
}

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... other booking fields ...
}

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal pricePerNight;
    // ... other pricing fields ...
}
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom query methods for properties
}

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods for bookings
}

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Custom query methods for availability
}

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Custom query methods for pricing
}
@Service
public class BookingService {
    // Autowire repositories and implement methods for:
    // - Creating bookings
    // - Checking availability
    // - Calculating pricing
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto) {
        // Endpoint to create a new booking
    }

    // ... other endpoints for managing bookings ...
}
@Entity
public class User {
    // ... existing fields ...

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String profilePictureUrl;

    // ... getters and setters ...
}
@Service
public class UserService {
    // Autowire UserRepository and implement methods for:
    // - Updating user profile details
    // - Handling photo uploads
}
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    @PostMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        // Endpoint to update user profile
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // Endpoint to upload a user's photo
    }
}
public class BookingDto {
    private Long userId;
    private Long propertyId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... getters and setters ...
}

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // ... getters and setters ...
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.uploadUserPhoto(id, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }
}
// BookingController.java
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        Booking updatedBooking = bookingService.updateBooking(id, bookingRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}

// UserProfileController.java
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody UserProfile userProfileDetails) {
        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userId, userProfileDetails);
        return ResponseEntity.ok(updatedUserProfile);
    }
}

// ReferralDashboardController.java
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private ReferralService referralService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getReferralDetails(@PathVariable Long userId) {
        ReferralDashboard referralDashboard = referralService.getReferralDashboard(userId);
        return ResponseEntity.ok(referralDashboard);
    }
}



// UserController.java with backend validation and error handling
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        if (!userDto.isValid()) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }
        if (!userDto.isTermsAccepted()) {
            return ResponseEntity.badRequest().body("Terms and conditions not accepted");
        }
        try {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }
}

// UserDto.java with consent storage
public class UserDto {
    private String username;
    private String email;
    private String password;
    private boolean termsAccepted;

    public boolean isValid() {
        // Implement validation logic
    }

    // Getters and setters
}

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    private PropertyType propertyType; // Enum: APARTMENT, HOUSE, etc.
    private int maxOccupancy;
    // ... other property fields ...
}
@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
    // ... other availability fields ...
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal nightlyRate;
    private int minimumStay;
    // ... other pricing fields ...
}
@Service
public class BookingService {
    // ... existing methods ...

    public List<Property> searchAvailableProperties(LocalDate checkIn, LocalDate checkOut, String location) {
        // Implement property search logic based on availability and location
    }

    public Booking createShortTermBooking(Long userId, Long propertyId, LocalDate checkIn, LocalDate checkOut) {
        // Implement booking creation for short-term rentals
    }
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchAvailableProperties(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam String location) {
        List<Property> availableProperties = bookingService.searchAvailableProperties(checkIn, checkOut, location);
        return ResponseEntity.ok(availableProperties);
    }

    @PostMapping
    public ResponseEntity<?> createShortTermBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createShortTermBooking(
                bookingRequest.getUserId(),
                bookingRequest.getPropertyId(),
                bookingRequest.getCheckIn(),
                bookingRequest.getCheckOut());
        return ResponseEntity.ok(booking);
    }
}

@Entity
public class UserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @OneToOne
    private User user;
   
    private String photoUrl; // URL to access the photo
   
    // Getters and setters
}
@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
    Optional<UserPhoto> findByUserId(Long userId);
}
@Service
public class UserPhotoService {
    @Autowired
    private UserPhotoRepository userPhotoRepository;
   
    public String uploadPhoto(MultipartFile file, Long userId) {
        // Logic to store the photo and return the URL
    }
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserPhotoService userPhotoService;
   
    @PostMapping("/{userId}/photo")
    public ResponseEntity<?> uploadUserPhoto(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        String photoUrl = userPhotoService.uploadPhoto(file, userId);
        return ResponseEntity.ok(photoUrl);
    }
}
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    private String name;
    private String description; // Short description of the property
    private BigDecimal pricePerNight; // Pricing information
   
    // Getters and setters
}
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Additional query methods if needed
}
@Service
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;
   
    public Property createOrUpdateProperty(Property property) {
        // Logic to save or update property details
    }
}
@RestController
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;
   
    @PostMapping
    public ResponseEntity<?> createOrUpdateProperty(@RequestBody Property property) {
        Property savedProperty = propertyService.createOrUpdateProperty(property);
        return ResponseEntity.ok(savedProperty);
    }
}
// Example of a WebSocket configuration for real-time interactions
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }
}
// Example of a service for handling image management
@Service
public class ImageService {

    public String uploadImage(MultipartFile file) {
        // Logic to handle image upload
    }

    public byte[] getImage(String imageName) {
        // Logic to retrieve and return image data
    }
}
// Example of a caching strategy for performance optimization
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        // Configure and return an appropriate CacheManager
    }
}
// Example of a security configuration for secure payment processing
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/payments/**").authenticated()
            .and()
            .formLogin();
    }
}
// Example of a repository with custom search methods for scalability
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.location = :location AND p.availability = true")
    List<Property> findAvailableProperties(@Param("location") String location);
}
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}
@RestController
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    // Getters and setters

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
@Service
public class PushNotificationService {

    public void sendPushNotification(String deviceToken, String title, String body) {
        // Logic to send push notification using Firebase Cloud Messaging (FCM)
    }
}
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @ManyToOne
    private User user;
   
    @ManyToOne
    private Property property;
   
    private int rating;
    private String comment;

    // Getters and setters
}
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
}
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForProperty(Long propertyId) {
        // Logic to retrieve reviews for a property
    }
}
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.saveReview(review);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Review>> getReviewsForProperty(@PathVariable Long propertyId) {
        List<Review> reviews = reviewService.getReviewsForProperty(propertyId);
        return ResponseEntity.ok(reviews);
    }
}
@Service
public class PayPalService {

    public PaymentResponse createPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) {
        // Logic to create a payment using PayPal API
    }

    public PaymentResponse completePayment(String paymentId, String payerId) {
        // Logic to complete the payment after the user approves
    }
}
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse payment = payPalService.createPayment(
                paymentRequest.getTotal(),
                paymentRequest.getCurrency(),
                paymentRequest.getMethod(),
                paymentRequest.getIntent(),
                paymentRequest.getDescription(),
                paymentRequest.getCancelUrl(),
                paymentRequest.getSuccessUrl());
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        PaymentResponse payment = payPalService.completePayment(paymentId, payerId);
        return ResponseEntity.ok(payment);
    }
}

// Security Configuration for Authentication
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login").permitAll()
            .and()
            .oauth2Login(); // For social login integration
    }
}
// Custom UserDetails service for authentication
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}

// Security configuration for OAuth and form login
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll()
            .and()
            .oauth2Login();
    }
}
// Property Controller for Listing and Managing Properties
@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.findAllProperties();
        return ResponseEntity.ok(properties);
    }

    // Additional endpoints for property management...
}
// Property controller for listing and managing properties
@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties(@RequestParam(required = false) String location) {
        List<Property> properties = propertyService.getPropertiesByLocation(location);
        return ResponseEntity.ok(properties);
    }

    // Additional CRUD endpoints...
}
// User Profile Controller for Managing User Profiles
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    // Additional endpoints for user profile management...
}
// Controller for user profile management
@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    // Additional endpoints for updating user profiles...
}
// Booking Controller for Handling the Booking Process
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    // Additional endpoints for booking management...
}
// Booking controller to handle the booking process
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    // Additional endpoints for booking management...
}

// Messaging Controller for Real-Time Communication
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
        Message message = messageService.sendMessage(messageRequest);
        return ResponseEntity.ok(message);
    }

    // Additional endpoints for messaging...
}
// Messaging controller for real-time communication between hosts and guests
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
        Message message = messageService.sendMessage(messageRequest);
        return ResponseEntity.ok(message);
    }

    // Additional endpoints for messaging...
}
// Payment Controller for Handling Payments
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(paymentResult);
    }

    // Additional endpoints for payment processing...
}
// Payment controller for handling secure payment transactions
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(paymentResult);
    }

    // Additional endpoints for payment processing...
}
// Review Controller for Property Reviews
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.createReview(reviewRequest);
        return ResponseEntity.ok(review);
    }

    // Additional endpoints for reviews...
}
// Review controller for managing property reviews and ratings
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.addReview(reviewRequest);
        return ResponseEntity.ok(review);
    }

    // Additional endpoints for reviews...
}
// PayPal service for handling transactions and applying commission fees
@Service
public class PayPalService {
    public PaymentResponse createPayment(Double amount, String currency, String paymentMethod, String intent, String description, String cancelUrl, String successUrl) {
        // Logic to create a payment with PayPal API
        // Apply a 30% commission fee on the total amount
    }

    public PaymentResponse executePayment(String paymentId, String payerId) {
        // Logic to execute the payment after approval
    }
}


// MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Inject services as needed

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending a message
        return ResponseEntity.ok("Message sent successfully");
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields
    private String email; // Add email field

    // Generate a referral code for a new user
    public static String generateReferralCode() {
        // Logic to generate a unique referral code
    }

    // Add referral information when a user is referred
    public void addReferral(Long referredById) {
        this.referredBy = referredById;
        // Additional logic if needed
    }

    // Reward the referrer with points
    public void rewardReferrer(User referrer) {
        referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Example reward
        // Additional logic if needed
    }

    // Getters and setters for new fields
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

// UserNotFoundException.java (Custom Exception)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// MessageDto.java (Data Transfer Object)
public class MessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    // Getters and setters
}
// Main Application Entry Point
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Generate a unique referral code
    @PrePersist
    protected void onCreate() {
        referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    // ... other methods ...
}

// UserRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Process a referral
    public void processReferral(String referralCode, Long referredById) {
        User referredUser = findUserByEmail(referralCode);
        referredUser.setReferredBy(referredById);
        userRepository.save(referredUser);

        // Reward the referrer
        userRepository.findById(referredById).ifPresent(referrer -> {
            referrer.setRewardPoints(referrer.getRewardPoints() + 10);
            userRepository.save(referrer);
        });
    }
}

// UserController
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set user details from DTO
        userService.saveUser(newUser);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestParam String referralCode, Principal principal) {
        User referrer = userService.findUserByEmail(principal.getName());
        userService.processReferral(referralCode, referrer.getId());
        return ResponseEntity.ok("Referral processed successfully");
    }
}

// UserDto
public class UserDto {
    private String username;
    private String email;
    private String password;
    // Getters and setters
}

// Security Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Security configuration methods
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

// WebSocket Configuration
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket configuration methods
}

// HTML, CSS, and JavaScript files
// Place your HTML, CSS, and JavaScript files in the 'src/main/resources/static' directory
@Entity
public class Availability {
    // Fields: propertyId, startDate, endDate, isAvailable, etc.
    // Getters and setters
}

@Entity
public class Pricing {
    // Fields: propertyId, basePrice, seasonalAdjustments, etc.
    // Getters and setters
}

@Entity
public class Payment {
    // Fields: userId, bookingId, amount, paymentStatus, etc.
    // Getters and setters
}

@Entity
public class UserDetails {
    // Fields: userId, firstName, lastName, address, phone, etc.
    // Getters and setters
}
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Methods for availability management
}

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Methods for pricing management
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Methods for payment management
}

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Methods for user details management
}
@Service
public class AvailabilityService {
    // Methods for managing property availability
}

@Service
public class PricingService {
    // Methods for managing property pricing
}

@Service
public class PaymentService {
    // Methods for handling payments
}

@Service
public class UserDetailsService {
    // Methods for managing user details
}
@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    // Endpoints for managing property availability
}

@RestController
@RequestMapping("/pricing")
public class PricingController {
    // Endpoints for managing property pricing
}

@RestController
@RequestMapping("/payment")
public class PaymentController {
    // Endpoints for handling payments
}

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    //

// User Entity with Referral Information
@Entity
public class User {
    // ... existing fields ...

    // Method to generate a unique referral code for each user
    @PrePersist
    private void initializeReferralCode() {
        this.referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // ... getters and setters ...
}

// UserService with Referral Logic
@Service
public class UserService {
    // ... existing methods ...

    // Method to process a referral when a new user signs up
    public void processReferral(String referralCode) {
        User referrer = userRepository.findByReferralCode(referralCode)
            .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));
        referrer.setRewardPoints(referrer.getRewardPoints() + 10);
        userRepository.save(referrer);
    }

    // ... other methods ...
}

// UserController with Sign-up and Referral Endpoints
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    // Endpoint to handle user sign-up with referral code
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set newUser properties from userDto
        newUser = userService.saveUser(newUser);

        // If a referral code is provided, process the referral
        if (userDto.getReferralCode() != null) {
            userService.processReferral(userDto.getReferralCode());
        }

        return ResponseEntity.ok("User signed up successfully");
    }

    // ... other endpoints ...
}

// UserDto with Referral Code
public class UserDto {
    // ... existing fields ...
    private String referralCode;
    // ... getters and setters ...
}

// Referral Dashboard Controller
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private UserService userService;

    // Endpoint to retrieve referral information for the current user
    @GetMapping("/my-referrals")
    public ResponseEntity<?> getMyReferrals(Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        List<ReferralInfo> referrals = userService.getReferralsForUser(currentUser.getId());
        return ResponseEntity.ok(referrals);
    }
}

// ReferralInfo DTO
public class ReferralInfo {
    private String referredUsername;
    private LocalDate dateReferred;
    private Boolean hasBooked;
    // ... getters and setters ...
}

// Security Configuration for Referral Dashboard
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ... existing security configuration ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // ... other security configurations ...
            .authorizeRequests()
            .antMatchers("/referral-dashboard/**").authenticated()
            // ... other matchers ...
    }
}

// Exception Handling for Referral Process
@ControllerAdvice
public class GlobalExceptionHandler {
    // ... existing exception handlers ...

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    // ... other property fields ...
}

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... other booking fields ...
}

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal pricePerNight;
    // ... other pricing fields ...
}
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom query methods for properties
}

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods for bookings
}

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Custom query methods for availability
}

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Custom query methods for pricing
}
@Service
public class BookingService {
    // Autowire repositories and implement methods for:
    // - Creating bookings
    // - Checking availability
    // - Calculating pricing
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto) {
        // Endpoint to create a new booking
    }

    // ... other endpoints for managing bookings ...
}
@Entity
public class User {
    // ... existing fields ...

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String profilePictureUrl;

    // ... getters and setters ...
}
@Service
public class UserService {
    // Autowire UserRepository and implement methods for:
    // - Updating user profile details
    // - Handling photo uploads
}
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    @PostMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        // Endpoint to update user profile
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // Endpoint to upload a user's photo
    }
}
public class BookingDto {
    private Long userId;
    private Long propertyId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... getters and setters ...
}

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // ... getters and setters ...
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.uploadUserPhoto(id, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }
}
// BookingController.java
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        Booking updatedBooking = bookingService.updateBooking(id, bookingRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}

// UserProfileController.java
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody UserProfile userProfileDetails) {
        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userId, userProfileDetails);
        return ResponseEntity.ok(updatedUserProfile);
    }
}

// ReferralDashboardController.java
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private ReferralService referralService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getReferralDetails(@PathVariable Long userId) {
        ReferralDashboard referralDashboard = referralService.getReferralDashboard(userId);
        return ResponseEntity.ok(referralDashboard);
    }
}
// UserController.java with backend validation and error handling
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        if (!userDto.isValid()) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }
        if (!userDto.isTermsAccepted()) {
            return ResponseEntity.badRequest().body("Terms and conditions not accepted");
        }
        try {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }
}

// UserDto.java with consent storage
public class UserDto {
    private String username;
    private String email;
    private String password;
    private boolean termsAccepted;

    public boolean isValid() {
        // Implement validation logic
    }

    // Getters and setters
}

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    private PropertyType propertyType; // Enum: APARTMENT, HOUSE, etc.
    private int maxOccupancy;
    // ... other property fields ...
}
@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
    // ... other availability fields ...
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal nightlyRate;
    private int minimumStay;
    // ... other pricing fields ...
}
@Service
public class BookingService {
    // ... existing methods ...

    public List<Property> searchAvailableProperties(LocalDate checkIn, LocalDate checkOut, String location) {
        // Implement property search logic based on availability and location
    }

    public Booking createShortTermBooking(Long userId, Long propertyId, LocalDate checkIn, LocalDate checkOut) {
        // Implement booking creation for short-term rentals
    }
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchAvailableProperties(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam String location) {
        List<Property> availableProperties = bookingService.searchAvailableProperties(checkIn, checkOut, location);
        return ResponseEntity.ok(availableProperties);
    }

    @PostMapping
    public ResponseEntity<?> createShortTermBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createShortTermBooking(
                bookingRequest.getUserId(),
                bookingRequest.getPropertyId(),
                bookingRequest.getCheckIn(),
                bookingRequest.getCheckOut());
        return ResponseEntity.ok(booking);
    }
}

@Entity
public class UserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @OneToOne
    private User user;
   
    private String photoUrl; // URL to access the photo
   
    // Getters and setters
}
@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
    Optional<UserPhoto> findByUserId(Long userId);
}
@Service
public class UserPhotoService {
    @Autowired
    private UserPhotoRepository userPhotoRepository;
   
    public String uploadPhoto(MultipartFile file, Long userId) {
        // Logic to store the photo and return the URL
    }
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserPhotoService userPhotoService;
   
    @PostMapping("/{userId}/photo")
    public ResponseEntity<?> uploadUserPhoto(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        String photoUrl = userPhotoService.uploadPhoto(file, userId);
        return ResponseEntity.ok(photoUrl);
    }
}
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    private String name;
    private String description; // Short description of the property
    private BigDecimal pricePerNight; // Pricing information
   
    // Getters and setters
}
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Additional query methods if needed
}
@Service
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;
   
    public Property createOrUpdateProperty(Property property) {
        // Logic to save or update property details
    }
}
@RestController
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;
   
    @PostMapping
    public ResponseEntity<?> createOrUpdateProperty(@RequestBody Property property) {
        Property savedProperty = propertyService.createOrUpdateProperty(property);
        return ResponseEntity.ok(savedProperty);
    }
}
// Example of a WebSocket configuration for real-time interactions
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }
}
// Example of a service for handling image management
@Service
public class ImageService {

    public String uploadImage(MultipartFile file) {
        // Logic to handle image upload
    }

    public byte[] getImage(String imageName) {
        // Logic to retrieve and return image data
    }
}
// Example of a caching strategy for performance optimization
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        // Configure and return an appropriate CacheManager
    }
}
// Example of a security configuration for secure payment processing
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/payments/**").authenticated()
            .and()
            .formLogin();
    }
}
// Example of a repository with custom search methods for scalability
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.location = :location AND p.availability = true")
    List<Property> findAvailableProperties(@Param("location") String location);
}
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}
@RestController
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    // Getters and setters

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
@Service
public class PushNotificationService {

    public void sendPushNotification(String deviceToken, String title, String body) {
        // Logic to send push notification using Firebase Cloud Messaging (FCM)
    }
}
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @ManyToOne
    private User user;
   
    @ManyToOne
    private Property property;
   
    private int rating;
    private String comment;

    // Getters and setters
}
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
}
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForProperty(Long propertyId) {
        // Logic to retrieve reviews for a property
    }
}
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.saveReview(review);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Review>> getReviewsForProperty(@PathVariable Long propertyId) {
        List<Review> reviews = reviewService.getReviewsForProperty(propertyId);
        return ResponseEntity.ok(reviews);
    }
}
@Service
public class PayPalService {

    public PaymentResponse createPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) {
        // Logic to create a payment using PayPal API
    }

    public PaymentResponse completePayment(String paymentId, String payerId) {
        // Logic to complete the payment after the user approves
    }
}
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse payment = payPalService.createPayment(
                paymentRequest.getTotal(),
                paymentRequest.getCurrency(),
                paymentRequest.getMethod(),
                paymentRequest.getIntent(),
                paymentRequest.getDescription(),
                paymentRequest.getCancelUrl(),
                paymentRequest.getSuccessUrl());
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        PaymentResponse payment = payPalService.completePayment(paymentId, payerId);
        return ResponseEntity.ok(payment);
    }
}

// Security Configuration for Authentication
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login").permitAll()
            .and()
            .oauth2Login(); // For social login integration
    }
}
// Custom UserDetails service for authentication
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}

// Security configuration for OAuth and form login
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll()
            .and()
            .oauth2Login();
    }
}
// Property Controller for Listing and Managing Properties
@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.findAllProperties();
        return ResponseEntity.ok(properties);
    }

    // Additional endpoints for property management...
}
// Property controller for listing and managing properties
@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties(@RequestParam(required = false) String location) {
        List<Property> properties = propertyService.getPropertiesByLocation(location);
        return ResponseEntity.ok(properties);
    }

    // Additional CRUD endpoints...
}
// User Profile Controller for Managing User Profiles
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    // Additional endpoints for user profile management...
}
// Controller for user profile management
@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    // Additional endpoints for updating user profiles...
}
// Booking Controller for Handling the Booking Process
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    // Additional endpoints for booking management...
}
// Booking controller to handle the booking process
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    // Additional endpoints for booking management...
}

// Messaging Controller for Real-Time Communication
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
        Message message = messageService.sendMessage(messageRequest);
        return ResponseEntity.ok(message);
    }

    // Additional endpoints for messaging...
}
// Messaging controller for real-time communication between hosts and guests
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
        Message message = messageService.sendMessage(messageRequest);
        return ResponseEntity.ok(message);
    }

    // Additional endpoints for messaging...
}
// Payment Controller for Handling Payments
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(paymentResult);
    }

    // Additional endpoints for payment processing...
}
// Payment controller for handling secure payment transactions
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(paymentResult);
    }

    // Additional endpoints for payment processing...
}
// Review Controller for Property Reviews
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.createReview(reviewRequest);
        return ResponseEntity.ok(review);
    }

    // Additional endpoints for reviews...
}
// Review controller for managing property reviews and ratings
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.addReview(reviewRequest);
        return ResponseEntity.ok(review);
    }

    // Additional endpoints for reviews...
}
// PayPal service for handling transactions and applying commission fees
@Service
public class PayPalService {
    public PaymentResponse createPayment(Double amount, String currency, String paymentMethod, String intent, String description, String cancelUrl, String successUrl) {
        // Logic to create a payment with PayPal API
        // Apply a 30% commission fee on the total amount
    }

    public PaymentResponse executePayment(String paymentId, String payerId) {
        // Logic to execute the payment after approval
    }
}

// PropertyController.java
@RestController
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> listProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso
            @GetMapping
            public ResponseEntity<List<Property>> listProperties(
                    @RequestParam(required = false) String location,
                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                    @RequestParam(required = false) Set<String> amenities,
                    @RequestParam(required = false) BigDecimal minPrice,
                    @RequestParam(required = false) BigDecimal maxPrice) {
                List<Property> properties = propertyService.searchProperties(location, startDate, endDate, amenities, minPrice, maxPrice);
                return ResponseEntity.ok(properties);
            }
        }
        // BookingController.java
        @RestController
        @RequestMapping("/bookings")
        public class BookingController {
            @Autowired
            private BookingService bookingService;
       
            @PostMapping
            public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
                Booking booking = bookingService.bookProperty(bookingRequest);
                // Send confirmation email or notification
                return ResponseEntity.ok(booking);
            }
        }
        // SecurityConfiguration.java
        @EnableWebSecurity
        public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
            // Configure authentication mechanisms, such as OAuth2 or form login
        }
       
        // UserProfileController.java
        @RestController
        @RequestMapping("/user-profile")
        public class UserProfileController {
            @Autowired
            private UserProfileService userProfileService;
       
            @GetMapping("/{userId}")
            public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
                UserProfile userProfile = userProfileService.getUserProfile(userId);
                return ResponseEntity.ok(userProfile);
            }
        }
        // WebSocket configuration for real-time messaging
        @Configuration
        @EnableWebSocketMessageBroker
        public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
            // Configure WebSocket endpoints and message broker
        }
       
        // MessageController.java for in-app messaging
        @RestController
        @RequestMapping("/messages")
        public class MessageController {
            @Autowired
            private MessageService messageService;
       
            @PostMapping
            public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
                Message message = messageService.sendMessage(messageRequest);
                return ResponseEntity.ok(message);
            }
        }
        // ReviewController.java
        @RestController
        @RequestMapping("/reviews")
        public class ReviewController {
            @Autowired
            private ReviewService reviewService;
       
            @PostMapping
            public ResponseEntity<Review> leaveReview(@RequestBody ReviewRequest reviewRequest) {
                Review review = reviewService.leaveReview(reviewRequest);
                return ResponseEntity.ok(review);
            }
        }
        // PaymentController.java
        @RestController
        @RequestMapping("/payments")
        public class PaymentController {
            @Autowired
            private PaymentService paymentService;
       
            @PostMapping("/process")
            public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest paymentRequest) {
                PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
                return ResponseEntity.ok(paymentResult);
            }
        }
        // NotificationService.java
        @Service
        public class NotificationService {
            public void sendBookingConfirmationNotification(Booking booking) {
                // Logic to send push notification for booking confirmation
            }
        }
        // HostController.java
        @RestController
        @RequestMapping("/host")
        public class HostController {
            @Autowired
            private PropertyManagementService propertyManagementService;
       
            @PostMapping("/property")
            public ResponseEntity<Property> manageProperty(@RequestBody PropertyRequest propertyRequest) {
                Property property = propertyManagementService.manageProperty(propertyRequest);
                return ResponseEntity.ok(property);
            }
        }
        // SupportController.java
        @RestController
        @RequestMapping("/support")
        public class SupportController {
            @Autowired
            private SupportService supportService;
       
            @PostMapping("/inquiry")
            public ResponseEntity<SupportTicket> createSupportInquiry(@RequestBody SupportInquiryRequest inquiryRequest) {
                SupportTicket ticket = supportService.createInquiry(inquiryRequest);
                return ResponseEntity.ok(ticket);
            }
        }
        @Service
        public class AuthenticationService {
       
            @Autowired
            private UserRepository userRepository;
            @Autowired
            private PasswordEncoder passwordEncoder;
       
            public User registerUser(SignupDto signupDto) {
                if (userRepository.existsByEmail(signupDto.getEmail())) {
                    throw new IllegalStateException("Email already in use.");
                }
       
                User newUser = new User();
                newUser.setEmail(signupDto.getEmail());
                newUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
                // Set additional fields from SignupDto
       
                return userRepository.save(newUser);
            }
       
            // Additional methods for login and authentication
        }
        @Service
        public class PropertyService {
       
            @Autowired
            private PropertyRepository propertyRepository;
       
            public List<Property> searchProperties(SearchCriteria criteria) {
                // Implement search logic based on criteria
                return propertyRepository.findPropertiesByCriteria(criteria);
            }
       
            public Property addProperty(PropertyDto propertyDto) {
                Property property = new Property();
                // Set property details from PropertyDto
                return propertyRepository.save(property);
            }
       
            // Additional methods for property management
        }
        @Service
        public class BookingService {
       
            @Autowired
            private BookingRepository bookingRepository;
            @Autowired
            private AvailabilityService availabilityService;
       
            public Booking createBooking(BookingDto bookingDto) {
                if (!availabilityService.isAvailable(bookingDto.getPropertyId(), bookingDto.getCheckIn(), bookingDto.getCheckOut())) {
                    throw new IllegalStateException("Property is not available for the selected dates.");
                }
       
                Booking booking = new Booking();
                // Set booking details from BookingDto
                return bookingRepository.save(booking);
            }
       
            // Additional methods for booking management
        }
        @Service
        public class MessagingService {
       
            @Autowired
            private MessageRepository messageRepository;
       
            public Message sendMessage(MessageDto messageDto) {
                Message message = new Message();
                // Set message details from MessageDto
                return messageRepository.save(message);
            }
       
            // Additional methods for messaging and communication
        }
        @Service
        public class ReviewService {
       
            @Autowired
            private ReviewRepository reviewRepository;
       
            public Review leaveReview(ReviewDto reviewDto) {
                Review review = new Review();
                // Set review details from ReviewDto
                return reviewRepository.save(review);
            }
       
            // Additional methods for handling reviews and ratings
        }
        @Service
        public class PaymentService {
       
            public PaymentResult processPayment(PaymentDto paymentDto) {
                // Integrate with payment gateway (e.g., Stripe, PayPal)
                // Apply business logic for payment processing
                // Return the result of the payment transaction
            }
       
            // Additional methods for payment processing
        }
        @Configuration
        @EnableWebSecurity
        public class SecurityConfig extends WebSecurityConfigurerAdapter {
       
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/api/public/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic(); // Or any other authentication mechanism
            }
        }
       
       
  // MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Inject services as needed

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending a message
        return ResponseEntity.ok("Message sent successfully");
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields
    private String email; // Add email field

    // Generate a referral code for a new user
    public static String generateReferralCode() {
        // Logic to generate a unique referral code
    }

    // Add referral information when a user is referred
    public void addReferral(Long referredById) {
        this.referredBy = referredById;
        // Additional logic if needed
    }

    // Reward the referrer with points
    public void rewardReferrer(User referrer) {
        referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Example reward
        // Additional logic if needed
    }

    // Getters and setters for new fields
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

// UserNotFoundException.java (Custom Exception)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// MessageDto.java (Data Transfer Object)
public class MessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    // Getters and setters
}

// Main Application Entry Point
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Generate a unique referral code
    @PrePersist
    protected void onCreate() {
        referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    // ... other methods ...
}

// UserRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Process a referral
    public void processReferral(String referralCode, Long referredById) {
        User referredUser = findUserByEmail(referralCode);
        referredUser.setReferredBy(referredById);
        userRepository.save(referredUser);

        // Reward the referrer
        userRepository.findById(referredById).ifPresent(referrer -> {
            referrer.setRewardPoints(referrer.getRewardPoints() + 10);
            userRepository.save(referrer);
        });
    }
}

// UserController
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set user details from DTO
        userService.saveUser(newUser);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestParam String referralCode, Principal principal) {
        User referrer = userService.findUserByEmail(principal.getName());
        userService.processReferral(referralCode, referrer.getId());
        return ResponseEntity.ok("Referral processed successfully");
    }
}

// UserDto
public class UserDto {
    private String username;
    private String email;
    private String password;
    // Getters and setters
}

// Security Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Security configuration methods
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

// WebSocket Configuration
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket configuration methods
}

// HTML, CSS, and JavaScript files
// Place your HTML, CSS, and JavaScript files in the 'src/main/resources/static' directory
@Entity
public class Availability {
    // Fields: propertyId, startDate, endDate, isAvailable, etc.
    // Getters and setters
}

@Entity
public class Pricing {
    // Fields: propertyId, basePrice, seasonalAdjustments, etc.
    // Getters and setters
}

@Entity
public class Payment {
    // Fields: userId, bookingId, amount, paymentStatus, etc.
    // Getters and setters
}

@Entity
public class UserDetails {
    // Fields: userId, firstName, lastName, address, phone, etc.
    // Getters and setters
}
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Methods for availability management
}

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Methods for pricing management
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Methods for payment management
}

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Methods for user details management
}
@Service
public class AvailabilityService {
    // Methods for managing property availability
}

@Service
public class PricingService {
    // Methods for managing property pricing
}

@Service
public class PaymentService {
    // Methods for handling payments
}

@Service
public class UserDetailsService {
    // Methods for managing user details
}
@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    // Endpoints for managing property availability
}

@RestController
@RequestMapping("/pricing")
public class PricingController {
    // Endpoints for managing property pricing
}

@RestController
@RequestMapping("/payment")
public class PaymentController {
    // Endpoints for handling payments
}

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    //


// User Entity with Referral Information
@Entity
public class User {
    // ... existing fields ...

    // Method to generate a unique referral code for each user
    @PrePersist
    private void initializeReferralCode() {
        this.referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // ... getters and setters ...
}

// UserService with Referral Logic
@Service
public class UserService {
    // ... existing methods ...

    // Method to process a referral when a new user signs up
    public void processReferral(String referralCode) {
        User referrer = userRepository.findByReferralCode(referralCode)
            .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));
        referrer.setRewardPoints(referrer.getRewardPoints() + 10);
        userRepository.save(referrer);
    }

    // ... other methods ...
}

// UserController with Sign-up and Referral Endpoints
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    // Endpoint to handle user sign-up with referral code
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set newUser properties from userDto
        newUser = userService.saveUser(newUser);

        // If a referral code is provided, process the referral
        if (userDto.getReferralCode() != null) {
            userService.processReferral(userDto.getReferralCode());
        }

        return ResponseEntity.ok("User signed up successfully");
    }

    // ... other endpoints ...
}

// UserDto with Referral Code
public class UserDto {
    // ... existing fields ...
    private String referralCode;
    // ... getters and setters ...
}

// Referral Dashboard Controller
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private UserService userService;

    // Endpoint to retrieve referral information for the current user
    @GetMapping("/my-referrals")
    public ResponseEntity<?> getMyReferrals(Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        List<ReferralInfo> referrals = userService.getReferralsForUser(currentUser.getId());
        return ResponseEntity.ok(referrals);
    }
}

// ReferralInfo DTO
public class ReferralInfo {
    private String referredUsername;
    private LocalDate dateReferred;
    private Boolean hasBooked;
    // ... getters and setters ...
}

// Security Configuration for Referral Dashboard
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ... existing security configuration ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // ... other security configurations ...
            .authorizeRequests()
            .antMatchers("/referral-dashboard/**").authenticated()
            // ... other matchers ...
    }
}

// Exception Handling for Referral Process
@ControllerAdvice
public class GlobalExceptionHandler {
    // ... existing exception handlers ...

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    // ... other property fields ...
}

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... other booking fields ...
}

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal pricePerNight;
    // ... other pricing fields ...
}
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom query methods for properties
}

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods for bookings
}

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Custom query methods for availability
}

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Custom query methods for pricing
}
@Service
public class BookingService {
    // Autowire repositories and implement methods for:
    // - Creating bookings
    // - Checking availability
    // - Calculating pricing
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto) {
        // Endpoint to create a new booking
    }

    // ... other endpoints for managing bookings ...
}
@Entity
public class User {
    // ... existing fields ...

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String profilePictureUrl;

    // ... getters and setters ...
}
@Service
public class UserService {
    // Autowire UserRepository and implement methods for:
    // - Updating user profile details
    // - Handling photo uploads
}
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    @PostMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        // Endpoint to update user profile
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // Endpoint to upload a user's photo
    }
}
public class BookingDto {
    private Long userId;
    private Long propertyId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... getters and setters ...
}

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // ... getters and setters ...
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.uploadUserPhoto(id, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }
}
// BookingController.java
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        Booking updatedBooking = bookingService.updateBooking(id, bookingRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}

// UserProfileController.java
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody UserProfile userProfileDetails) {
        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userId, userProfileDetails);
        return ResponseEntity.ok(updatedUserProfile);
    }
}

// ReferralDashboardController.java
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private ReferralService referralService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getReferralDetails(@PathVariable Long userId) {
        ReferralDashboard referralDashboard = referralService.getReferralDashboard(userId);
        return ResponseEntity.ok(referralDashboard);
    }
}



 // UserController.java with backend validation and error handling
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        if (!userDto.isValid()) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }
        if (!userDto.isTermsAccepted()) {
            return ResponseEntity.badRequest().body("Terms and conditions not accepted");
        }
        try {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }
}

// UserDto.java with consent storage
public class UserDto {
    private String username;
    private String email;
    private String password;
    private boolean termsAccepted;

    public boolean isValid() {
        // Implement validation logic
    }

    // Getters and setters
}

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    private PropertyType propertyType; // Enum: APARTMENT, HOUSE, etc.
    private int maxOccupancy;
    // ... other property fields ...
}
@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
    // ... other availability fields ...
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal nightlyRate;
    private int minimumStay;
    // ... other pricing fields ...
}
@Service
public class BookingService {
    // ... existing methods ...

    public List<Property> searchAvailableProperties(LocalDate checkIn, LocalDate checkOut, String location) {
        // Implement property search logic based on availability and location
    }

    public Booking createShortTermBooking(Long userId, Long propertyId, LocalDate checkIn, LocalDate checkOut) {
        // Implement booking creation for short-term rentals
    }
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchAvailableProperties(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam String location) {
        List<Property> availableProperties = bookingService.searchAvailableProperties(checkIn, checkOut, location);
        return ResponseEntity.ok(availableProperties);
    }

    @PostMapping
    public ResponseEntity<?> createShortTermBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createShortTermBooking(
                bookingRequest.getUserId(),
                bookingRequest.getPropertyId(),
                bookingRequest.getCheckIn(),
                bookingRequest.getCheckOut());
        return ResponseEntity.ok(booking);
    }
}

@Entity
public class UserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @OneToOne
    private User user;
   
    private String photoUrl; // URL to access the photo
   
    // Getters and setters
}
@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
    Optional<UserPhoto> findByUserId(Long userId);
}
@Service
public class UserPhotoService {
    @Autowired
    private UserPhotoRepository userPhotoRepository;
   
    public String uploadPhoto(MultipartFile file, Long userId) {
        // Logic to store the photo and return the URL
    }
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserPhotoService userPhotoService;
   
    @PostMapping("/{userId}/photo")
    public ResponseEntity<?> uploadUserPhoto(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        String photoUrl = userPhotoService.uploadPhoto(file, userId);
        return ResponseEntity.ok(photoUrl);
    }
}
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    private String name;
    private String description; // Short description of the property
    private BigDecimal pricePerNight; // Pricing information
   
    // Getters and setters
}
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Additional query methods if needed
}
@Service
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;
   
    public Property createOrUpdateProperty(Property property) {
        // Logic to save or update property details
    }
}
@RestController
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;
   
    @PostMapping
    public ResponseEntity<?> createOrUpdateProperty(@RequestBody Property property) {
        Property savedProperty = propertyService.createOrUpdateProperty(property);
        return ResponseEntity.ok(savedProperty);
    }
}
// Example of a WebSocket configuration for real-time interactions
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }
}
// Example of a service for handling image management
@Service
public class ImageService {

    public String uploadImage(MultipartFile file) {
        // Logic to handle image upload
    }

    public byte[] getImage(String imageName) {
        // Logic to retrieve and return image data
    }
}
// Example of a caching strategy for performance optimization
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        // Configure and return an appropriate CacheManager
    }
}
// Example of a security configuration for secure payment processing
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/payments/**").authenticated()
            .and()
            .formLogin();
    }
}
// Example of a repository with custom search methods for scalability
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.location = :location AND p.availability = true")
    List<Property> findAvailableProperties(@Param("location") String location);
}
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}
@RestController
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    // Getters and setters

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
@Service
public class PushNotificationService {

    public void sendPushNotification(String deviceToken, String title, String body) {
        // Logic to send push notification using Firebase Cloud Messaging (FCM)
    }
}
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @ManyToOne
    private User user;
   
    @ManyToOne
    private Property property;
   
    private int rating;
    private String comment;

    // Getters and setters
}
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
}
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForProperty(Long propertyId) {
        // Logic to retrieve reviews for a property
    }
}
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.saveReview(review);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Review>> getReviewsForProperty(@PathVariable Long propertyId) {
        List<Review> reviews = reviewService.getReviewsForProperty(propertyId);
        return ResponseEntity.ok(reviews);
    }
}
@Service
public class PayPalService {

    public PaymentResponse createPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) {
        // Logic to create a payment using PayPal API
    }

    public PaymentResponse completePayment(String paymentId, String payerId) {
        // Logic to complete the payment after the user approves
    }
}
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse payment = payPalService.createPayment(
                paymentRequest.getTotal(),
                paymentRequest.getCurrency(),
                paymentRequest.getMethod(),
                paymentRequest.getIntent(),
                paymentRequest.getDescription(),
                paymentRequest.getCancelUrl(),
                paymentRequest.getSuccessUrl());
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        PaymentResponse payment = payPalService.completePayment(paymentId, payerId);
        return ResponseEntity.ok(payment);
    }
}

// Security Configuration for Authentication
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login").permitAll()
            .and()
            .oauth2Login(); // For social login integration
    }
}
// Custom UserDetails service for authentication
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}

// Security configuration for OAuth and form login
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll()
            .and()
            .oauth2Login();
    }
}
// Property Controller for Listing and Managing Properties
@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.findAllProperties();
        return ResponseEntity.ok(properties);
    }

    // Additional endpoints for property management...
}
// Property controller for listing and managing properties
@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties(@RequestParam(required = false) String location) {
        List<Property> properties = propertyService.getPropertiesByLocation(location);
        return ResponseEntity.ok(properties);
    }

    // Additional CRUD endpoints...
}
// User Profile Controller for Managing User Profiles
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    // Additional endpoints for user profile management...
}
// Controller for user profile management
@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    // Additional endpoints for updating user profiles...
}
// Booking Controller for Handling the Booking Process
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    // Additional endpoints for booking management...
}
// Booking controller to handle the booking process
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    // Additional endpoints for booking management...
}

// Messaging Controller for Real-Time Communication
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
        Message message = messageService.sendMessage(messageRequest);
        return ResponseEntity.ok(message);
    }

    // Additional endpoints for messaging...
}
// Messaging controller for real-time communication between hosts and guests
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
        Message message = messageService.sendMessage(messageRequest);
        return ResponseEntity.ok(message);
    }

    // Additional endpoints for messaging...
}
// Payment Controller for Handling Payments
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(paymentResult);
    }

    // Additional endpoints for payment processing...
}
// Payment controller for handling secure payment transactions
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(paymentResult);
    }

    // Additional endpoints for payment processing...
}
// Review Controller for Property Reviews
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.createReview(reviewRequest);
        return ResponseEntity.ok(review);
    }

    // Additional endpoints for reviews...
}
// Review controller for managing property reviews and ratings
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.addReview(reviewRequest);
        return ResponseEntity.ok(review);
    }

    // Additional endpoints for reviews...
}
// PayPal service for handling transactions and applying commission fees
@Service
public class PayPalService {
    public PaymentResponse createPayment(Double amount, String currency, String paymentMethod, String intent, String description, String cancelUrl, String successUrl) {
        // Logic to create a payment with PayPal API
        // Apply a 30% commission fee on the total amount
    }

    public PaymentResponse executePayment(String paymentId, String payerId) {
        // Logic to execute the payment after approval
    }
}

// PropertyController.java
@RestController
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> listProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso    @GetMapping
            public ResponseEntity<List<Property>> listProperties(
                    @RequestParam(required = false) String location,
                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                    @RequestParam(required = false) Set<String> amenities,
                    @RequestParam(required = false) BigDecimal minPrice,
                    @RequestParam(required = false) BigDecimal maxPrice) {
                List<Property> properties = propertyService.searchProperties(location, startDate, endDate, amenities, minPrice, maxPrice);
                return ResponseEntity.ok(properties);
            }
        }
        // BookingController.java
        @RestController
        @RequestMapping("/bookings")
        public class BookingController {
            @Autowired
            private BookingService bookingService;
       
            @PostMapping
            public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
                Booking booking = bookingService.bookProperty(bookingRequest);
                // Send confirmation email or notification
                return ResponseEntity.ok(booking);
            }
        }
        // SecurityConfiguration.java
        @EnableWebSecurity
        public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
            // Configure authentication mechanisms, such as OAuth2 or form login
        }
       
        // UserProfileController.java
        @RestController
        @RequestMapping("/user-profile")
        public class UserProfileController {
            @Autowired
            private UserProfileService userProfileService;
       
            @GetMapping("/{userId}")
            public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
                UserProfile userProfile = userProfileService.getUserProfile(userId);
                return ResponseEntity.ok(userProfile);
            }
        }
        // WebSocket configuration for real-time messaging
        @Configuration
        @EnableWebSocketMessageBroker
        public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
            // Configure WebSocket endpoints and message broker
        }
       
        // MessageController.java for in-app messaging
        @RestController
        @RequestMapping("/messages")
        public class MessageController {
            @Autowired
            private MessageService messageService;
       
            @PostMapping
            public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
                Message message = messageService.sendMessage(messageRequest);
                return ResponseEntity.ok(message);
            }
        }
        // ReviewController.java
        @RestController
        @RequestMapping("/reviews")
        public class ReviewController {
            @Autowired
            private ReviewService reviewService;
       
            @PostMapping
            public ResponseEntity<Review> leaveReview(@RequestBody ReviewRequest reviewRequest) {
                Review review = reviewService.leaveReview(reviewRequest);
                return ResponseEntity.ok(review);
            }
        }
        // PaymentController.java
        @RestController
        @RequestMapping("/payments")
        public class PaymentController {
            @Autowired
            private PaymentService paymentService;
       
            @PostMapping("/process")
            public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest paymentRequest) {
                PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
                return ResponseEntity.ok(paymentResult);
            }
        }
        // NotificationService.java
        @Service
        public class NotificationService {
            public void sendBookingConfirmationNotification(Booking booking) {
                // Logic to send push notification for booking confirmation
            }
        }
        // HostController.java
        @RestController
        @RequestMapping("/host")
        public class HostController {
            @Autowired
            private PropertyManagementService propertyManagementService;
       
            @PostMapping("/property")
            public ResponseEntity<Property> manageProperty(@RequestBody PropertyRequest propertyRequest) {
                Property property = propertyManagementService.manageProperty(propertyRequest);
                return ResponseEntity.ok(property);
            }
        }
        // SupportController.java
        @RestController
        @RequestMapping("/support")
        public class SupportController {
            @Autowired
            private SupportService supportService;
       
            @PostMapping("/inquiry")
            public ResponseEntity<SupportTicket> createSupportInquiry(@RequestBody SupportInquiryRequest inquiryRequest) {
                SupportTicket ticket = supportService.createInquiry(inquiryRequest);
                return ResponseEntity.ok(ticket);
            }
        }
        @Service
        public class AuthenticationService {
       
            @Autowired
            private UserRepository userRepository;
            @Autowired
            private PasswordEncoder passwordEncoder;
       
            public User registerUser(SignupDto signupDto) {
                if (userRepository.existsByEmail(signupDto.getEmail())) {
                    throw new IllegalStateException("Email already in use.");
                }
       
                User newUser = new User();
                newUser.setEmail(signupDto.getEmail());
                newUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
                // Set additional fields from SignupDto
       
                return userRepository.save(newUser);
            }
       
            // Additional methods for login and authentication
        }
        @Service
        public class PropertyService {
       
            @Autowired
            private PropertyRepository propertyRepository;
       
            public List<Property> searchProperties(SearchCriteria criteria) {
                // Implement search logic based on criteria
                return propertyRepository.findPropertiesByCriteria(criteria);
            }
       
            public Property addProperty(PropertyDto propertyDto) {
                Property property = new Property();
                // Set property details from PropertyDto
                return propertyRepository.save(property);
            }
       
            // Additional methods for property management
        }
        @Service
        public class BookingService {
       
            @Autowired
            private BookingRepository bookingRepository;
            @Autowired
            private AvailabilityService availabilityService;
       
            public Booking createBooking(BookingDto bookingDto) {
                if (!availabilityService.isAvailable(bookingDto.getPropertyId(), bookingDto.getCheckIn(), bookingDto.getCheckOut())) {
                    throw new IllegalStateException("Property is not available for the selected dates.");
                }
       
                Booking booking = new Booking();
                // Set booking details from BookingDto
                return bookingRepository.save(booking);
            }
       
            // Additional methods for booking management
        }
        @Service
        public class MessagingService {
       
            @Autowired
            private MessageRepository messageRepository;
       
            public Message sendMessage(MessageDto messageDto) {
                Message message = new Message();
                // Set message details from MessageDto
                return messageRepository.save(message);
            }
       
            // Additional methods for messaging and communication
        }
        @Service
        public class ReviewService {
       
            @Autowired
            private ReviewRepository reviewRepository;
       
            public Review leaveReview(ReviewDto reviewDto) {
                Review review = new Review();
                // Set review details from ReviewDto
                return reviewRepository.save(review);
            }
       
            // Additional methods for handling reviews and ratings
        }
        @Service
        public class PaymentService {
       
            public PaymentResult processPayment(PaymentDto paymentDto) {
                // Integrate with payment gateway (e.g., Stripe, PayPal)
                // Apply business logic for payment processing
                // Return the result of the payment transaction
            }
       
            // Additional methods for payment processing
        }
        @Configuration
        @EnableWebSecurity
        public class SecurityConfig extends WebSecurityConfigurerAdapter {
       
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/api/public/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic(); // Or any other authentication mechanism
            }
        }
 // MessageController.java
@RestController
@RequestMapping("/messages")
public class MessageController {
    // Inject services as needed

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        // Logic to handle sending a message
        return ResponseEntity.ok("Message sent successfully");
    }
}

// User.java (Entity)
@Entity
public class User {
    // Existing fields
    private String email; // Add email field

    // Generate a referral code for a new user
    public static String generateReferralCode() {
        // Logic to generate a unique referral code
    }

    // Add referral information when a user is referred
    public void addReferral(Long referredById) {
        this.referredBy = referredById;
        // Additional logic if needed
    }

    // Reward the referrer with points
    public void rewardReferrer(User referrer) {
        referrer.setRewardPoints(referrer.getRewardPoints() + 10); // Example reward
        // Additional logic if needed
    }

    // Getters and setters for new fields
}

// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

// UserNotFoundException.java (Custom Exception)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// MessageDto.java (Data Transfer Object)
public class MessageDto {
    private String senderEmail;
    private String receiverEmail;
    private String content;
    // Getters and setters
}
// Main Application Entry Point
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// User Entity
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String referralCode;
    private Long referredBy;
    private Integer rewardPoints = 0;

    // Generate a unique referral code
    @PrePersist
    protected void onCreate() {
        referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    // ... other methods ...
}

// UserRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}

// UserService
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Find a user by email
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Save a user to the database
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Process a referral
    public void processReferral(String referralCode, Long referredById) {
        User referredUser = findUserByEmail(referralCode);
        referredUser.setReferredBy(referredById);
        userRepository.save(referredUser);

        // Reward the referrer
        userRepository.findById(referredById).ifPresent(referrer -> {
            referrer.setRewardPoints(referrer.getRewardPoints() + 10);
            userRepository.save(referrer);
        });
    }
}

// UserController
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set user details from DTO
        userService.saveUser(newUser);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping("/refer")
    public ResponseEntity<?> referUser(@RequestParam String referralCode, Principal principal) {
        User referrer = userService.findUserByEmail(principal.getName());
        userService.processReferral(referralCode, referrer.getId());
        return ResponseEntity.ok("Referral processed successfully");
    }
}

// UserDto
public class UserDto {
    private String username;
    private String email;
    private String password;
    // Getters and setters
}

// Security Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Security configuration methods
}

// Exception Handling
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

// WebSocket Configuration
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket configuration methods
}

// HTML, CSS, and JavaScript files
// Place your HTML, CSS, and JavaScript files in the 'src/main/resources/static' directory
@Entity
public class Availability {
    // Fields: propertyId, startDate, endDate, isAvailable, etc.
    // Getters and setters
}

@Entity
public class Pricing {
    // Fields: propertyId, basePrice, seasonalAdjustments, etc.
    // Getters and setters
}

@Entity
public class Payment {
    // Fields: userId, bookingId, amount, paymentStatus, etc.
    // Getters and setters
}

@Entity
public class UserDetails {
    // Fields: userId, firstName, lastName, address, phone, etc.
    // Getters and setters
}
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Methods for availability management
}

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Methods for pricing management
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Methods for payment management
}

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    // Methods for user details management
}
@Service
public class AvailabilityService {
    // Methods for managing property availability
}

@Service
public class PricingService {
    // Methods for managing property pricing
}

@Service
public class PaymentService {
    // Methods for handling payments
}

@Service
public class UserDetailsService {
    // Methods for managing user details
}
@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    // Endpoints for managing property availability
}

@RestController
@RequestMapping("/pricing")
public class PricingController {
    // Endpoints for managing property pricing
}

@RestController
@RequestMapping("/payment")
public class PaymentController {
    // Endpoints for handling payments
}

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    //

// User Entity with Referral Information
@Entity
public class User {
    // ... existing fields ...

    // Method to generate a unique referral code for each user
    @PrePersist
    private void initializeReferralCode() {
        this.referralCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // ... getters and setters ...
}

// UserService with Referral Logic
@Service
public class UserService {
    // ... existing methods ...

    // Method to process a referral when a new user signs up
    public void processReferral(String referralCode) {
        User referrer = userRepository.findByReferralCode(referralCode)
            .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));
        referrer.setRewardPoints(referrer.getRewardPoints() + 10);
        userRepository.save(referrer);
    }

    // ... other methods ...
}

// UserController with Sign-up and Referral Endpoints
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    // Endpoint to handle user sign-up with referral code
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        User newUser = new User();
        // Set newUser properties from userDto
        newUser = userService.saveUser(newUser);

        // If a referral code is provided, process the referral
        if (userDto.getReferralCode() != null) {
            userService.processReferral(userDto.getReferralCode());
        }

        return ResponseEntity.ok("User signed up successfully");
    }

    // ... other endpoints ...
}

// UserDto with Referral Code
public class UserDto {
    // ... existing fields ...
    private String referralCode;
    // ... getters and setters ...
}

// Referral Dashboard Controller
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private UserService userService;

    // Endpoint to retrieve referral information for the current user
    @GetMapping("/my-referrals")
    public ResponseEntity<?> getMyReferrals(Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        List<ReferralInfo> referrals = userService.getReferralsForUser(currentUser.getId());
        return ResponseEntity.ok(referrals);
    }
}

// ReferralInfo DTO
public class ReferralInfo {
    private String referredUsername;
    private LocalDate dateReferred;
    private Boolean hasBooked;
    // ... getters and setters ...
}

// Security Configuration for Referral Dashboard
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // ... existing security configuration ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // ... other security configurations ...
            .authorizeRequests()
            .antMatchers("/referral-dashboard/**").authenticated()
            // ... other matchers ...
    }
}

// Exception Handling for Referral Process
@ControllerAdvice
public class GlobalExceptionHandler {
    // ... existing exception handlers ...

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    // ... other property fields ...
}

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Property property;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... other booking fields ...
}

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal pricePerNight;
    // ... other pricing fields ...
}
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom query methods for properties
}

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods for bookings
}

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // Custom query methods for availability
}

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    // Custom query methods for pricing
}
@Service
public class BookingService {
    // Autowire repositories and implement methods for:
    // - Creating bookings
    // - Checking availability
    // - Calculating pricing
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto) {
        // Endpoint to create a new booking
    }

    // ... other endpoints for managing bookings ...
}
@Entity
public class User {
    // ... existing fields ...

    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String profilePictureUrl;

    // ... getters and setters ...
}
@Service
public class UserService {
    // Autowire UserRepository and implement methods for:
    // - Updating user profile details
    // - Handling photo uploads
}
@RestController
@RequestMapping("/users")
public class UserController {
    // ... existing endpoints ...

    @PostMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        // Endpoint to update user profile
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // Endpoint to upload a user's photo
    }
}
public class BookingDto {
    private Long userId;
    private Long propertyId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    // ... getters and setters ...
}

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    // ... getters and setters ...
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            userService.uploadUserPhoto(id, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }
}
// BookingController.java
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        Booking updatedBooking = bookingService.updateBooking(id, bookingRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}

// UserProfileController.java
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody UserProfile userProfileDetails) {
        UserProfile updatedUserProfile = userProfileService.updateUserProfile(userId, userProfileDetails);
        return ResponseEntity.ok(updatedUserProfile);
    }
}

// ReferralDashboardController.java
@RestController
@RequestMapping("/referral-dashboard")
public class ReferralDashboardController {
    @Autowired
    private ReferralService referralService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getReferralDetails(@PathVariable Long userId) {
        ReferralDashboard referralDashboard = referralService.getReferralDashboard(userId);
        return ResponseEntity.ok(referralDashboard);
    }
}
 
// UserController.java with backend validation and error handling
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        if (!userDto.isValid()) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }
        if (!userDto.isTermsAccepted()) {
            return ResponseEntity.badRequest().body("Terms and conditions not accepted");
        }
        try {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }
}

// UserDto.java with consent storage
public class UserDto {
    private String username;
    private String email;
    private String password;
    private boolean termsAccepted;

    public boolean isValid() {
        // Implement validation logic
    }

    // Getters and setters
}

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    private PropertyType propertyType; // Enum: APARTMENT, HOUSE, etc.
    private int maxOccupancy;
    // ... other property fields ...
}
@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private LocalDate date;
    private boolean isAvailable;
    // ... other availability fields ...
}

@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Property property;
    private BigDecimal nightlyRate;
    private int minimumStay;
    // ... other pricing fields ...
}
@Service
public class BookingService {
    // ... existing methods ...

    public List<Property> searchAvailableProperties(LocalDate checkIn, LocalDate checkOut, String location) {
        // Implement property search logic based on availability and location
    }

    public Booking createShortTermBooking(Long userId, Long propertyId, LocalDate checkIn, LocalDate checkOut) {
        // Implement booking creation for short-term rentals
    }
}
@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchAvailableProperties(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,
            @RequestParam String location) {
        List<Property> availableProperties = bookingService.searchAvailableProperties(checkIn, checkOut, location);
        return ResponseEntity.ok(availableProperties);
    }

    @PostMapping
    public ResponseEntity<?> createShortTermBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createShortTermBooking(
                bookingRequest.getUserId(),
                bookingRequest.getPropertyId(),
                bookingRequest.getCheckIn(),
                bookingRequest.getCheckOut());
        return ResponseEntity.ok(booking);
    }
}

@Entity
public class UserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @OneToOne
    private User user;
   
    private String photoUrl; // URL to access the photo
   
    // Getters and setters
}
@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
    Optional<UserPhoto> findByUserId(Long userId);
}
@Service
public class UserPhotoService {
    @Autowired
    private UserPhotoRepository userPhotoRepository;
   
    public String uploadPhoto(MultipartFile file, Long userId) {
        // Logic to store the photo and return the URL
    }
}
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserPhotoService userPhotoService;
   
    @PostMapping("/{userId}/photo")
    public ResponseEntity<?> uploadUserPhoto(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        String photoUrl = userPhotoService.uploadPhoto(file, userId);
        return ResponseEntity.ok(photoUrl);
    }
}
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    private String name;
    private String description; // Short description of the property
    private BigDecimal pricePerNight; // Pricing information
   
    // Getters and setters
}
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Additional query methods if needed
}
@Service
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;
   
    public Property createOrUpdateProperty(Property property) {
        // Logic to save or update property details
    }
}
@RestController
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;
   
    @PostMapping
    public ResponseEntity<?> createOrUpdateProperty(@RequestBody Property property) {
        Property savedProperty = propertyService.createOrUpdateProperty(property);
        return ResponseEntity.ok(savedProperty);
    }
}
// Example of a WebSocket configuration for real-time interactions
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }
}
// Example of a service for handling image management
@Service
public class ImageService {

    public String uploadImage(MultipartFile file) {
        // Logic to handle image upload
    }

    public byte[] getImage(String imageName) {
        // Logic to retrieve and return image data
    }
}
// Example of a caching strategy for performance optimization
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        // Configure and return an appropriate CacheManager
    }
}
// Example of a security configuration for secure payment processing
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/payments/**").authenticated()
            .and()
            .formLogin();
    }
}
// Example of a repository with custom search methods for scalability
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.location = :location AND p.availability = true")
    List<Property> findAvailableProperties(@Param("location") String location);
}
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}
@RestController
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    // Getters and setters

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
@Service
public class PushNotificationService {

    public void sendPushNotification(String deviceToken, String title, String body) {
        // Logic to send push notification using Firebase Cloud Messaging (FCM)
    }
}
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @ManyToOne
    private User user;
   
    @ManyToOne
    private Property property;
   
    private int rating;
    private String comment;

    // Getters and setters
}
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProperty(Property property);
}
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForProperty(Long propertyId) {
        // Logic to retrieve reviews for a property
    }
}
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.saveReview(review);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Review>> getReviewsForProperty(@PathVariable Long propertyId) {
        List<Review> reviews = reviewService.getReviewsForProperty(propertyId);
        return ResponseEntity.ok(reviews);
    }
}
@Service
public class PayPalService {

    public PaymentResponse createPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) {
        // Logic to create a payment using PayPal API
    }

    public PaymentResponse completePayment(String paymentId, String payerId) {
        // Logic to complete the payment after the user approves
    }
}
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse payment = payPalService.createPayment(
                paymentRequest.getTotal(),
                paymentRequest.getCurrency(),
                paymentRequest.getMethod(),
                paymentRequest.getIntent(),
                paymentRequest.getDescription(),
                paymentRequest.getCancelUrl(),
                paymentRequest.getSuccessUrl());
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        PaymentResponse payment = payPalService.completePayment(paymentId, payerId);
        return ResponseEntity.ok(payment);
    }
}

// Security Configuration for Authentication
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login").permitAll()
            .and()
            .oauth2Login(); // For social login integration
    }
}
// Custom UserDetails service for authentication
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}

// Security configuration for OAuth and form login
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll()
            .and()
            .oauth2Login();
    }
}
// Property Controller for Listing and Managing Properties
@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.findAllProperties();
        return ResponseEntity.ok(properties);
    }

    // Additional endpoints for property management...
}
// Property controller for listing and managing properties
@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties(@RequestParam(required = false) String location) {
        List<Property> properties = propertyService.getPropertiesByLocation(location);
        return ResponseEntity.ok(properties);
    }

    // Additional CRUD endpoints...
}
// User Profile Controller for Managing User Profiles
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    // Additional endpoints for user profile management...
}
// Controller for user profile management
@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    // Additional endpoints for updating user profiles...
}
// Booking Controller for Handling the Booking Process
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    // Additional endpoints for booking management...
}
// Booking controller to handle the booking process
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    // Additional endpoints for booking management...
}

// Messaging Controller for Real-Time Communication
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
        Message message = messageService.sendMessage(messageRequest);
        return ResponseEntity.ok(message);
    }

    // Additional endpoints for messaging...
}
// Messaging controller for real-time communication between hosts and guests
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
        Message message = messageService.sendMessage(messageRequest);
        return ResponseEntity.ok(message);
    }

    // Additional endpoints for messaging...
}
// Payment Controller for Handling Payments
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(paymentResult);
    }

    // Additional endpoints for payment processing...
}
// Payment controller for handling secure payment transactions
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(paymentResult);
    }

    // Additional endpoints for payment processing...
}
// Review Controller for Property Reviews
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.createReview(reviewRequest);
        return ResponseEntity.ok(review);
    }

    // Additional endpoints for reviews...
}
// Review controller for managing property reviews and ratings
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.addReview(reviewRequest);
        return ResponseEntity.ok(review);
    }

    // Additional endpoints for reviews...
}
// PayPal service for handling transactions and applying commission fees
@Service
public class PayPalService {
    public PaymentResponse createPayment(Double amount, String currency, String paymentMethod, String intent, String description, String cancelUrl, String successUrl) {
        // Logic to create a payment with PayPal API
        // Apply a 30% commission fee on the total amount
    }

    public PaymentResponse executePayment(String paymentId, String payerId) {
        // Logic to execute the payment after approval
    }
}

// PropertyController.java
@RestController
@RequestMapping("/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<Property>> listProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso            
            @GetMapping
            public ResponseEntity<List<Property>> listProperties(
                    @RequestParam(required = false) String location,
                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                    @RequestParam(required = false) Set<String> amenities,
                    @RequestParam(required = false) BigDecimal minPrice,
                    @RequestParam(required = false) BigDecimal maxPrice) {
                List<Property> properties = propertyService.searchProperties(location, startDate, endDate, amenities, minPrice, maxPrice);
                return ResponseEntity.ok(properties);
            }
        }
        // BookingController.java
        @RestController
        @RequestMapping("/bookings")
        public class BookingController {
            @Autowired
            private BookingService bookingService;
       
            @PostMapping
            public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) {
                Booking booking = bookingService.bookProperty(bookingRequest);
                // Send confirmation email or notification
                return ResponseEntity.ok(booking);
            }
        }
        // SecurityConfiguration.java
        @EnableWebSecurity
        public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
            // Configure authentication mechanisms, such as OAuth2 or form login
        }
       
        // UserProfileController.java
        @RestController
        @RequestMapping("/user-profile")
        public class UserProfileController {
            @Autowired
            private UserProfileService userProfileService;
       
            @GetMapping("/{userId}")
            public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
                UserProfile userProfile = userProfileService.getUserProfile(userId);
                return ResponseEntity.ok(userProfile);
            }
        }
        // WebSocket configuration for real-time messaging
        @Configuration
        @EnableWebSocketMessageBroker
        public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
            // Configure WebSocket endpoints and message broker
        }
       
        // MessageController.java for in-app messaging
        @RestController
        @RequestMapping("/messages")
        public class MessageController {
            @Autowired
            private MessageService messageService;
       
            @PostMapping
            public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
                Message message = messageService.sendMessage(messageRequest);
                return ResponseEntity.ok(message);
            }
        }
        // ReviewController.java
        @RestController
        @RequestMapping("/reviews")
        public class ReviewController {
            @Autowired
            private ReviewService reviewService;
       
            @PostMapping
            public ResponseEntity<Review> leaveReview(@RequestBody ReviewRequest reviewRequest) {
                Review review = reviewService.leaveReview(reviewRequest);
                return ResponseEntity.ok(review);
            }
        }
        // PaymentController.java
        @RestController
        @RequestMapping("/payments")
        public class PaymentController {
            @Autowired
            private PaymentService paymentService;
       
            @PostMapping("/process")
            public ResponseEntity<PaymentResult> processPayment(@RequestBody PaymentRequest paymentRequest) {
                PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
                return ResponseEntity.ok(paymentResult);
            }
        }
        // NotificationService.java
        @Service
        public class NotificationService {
            public void sendBookingConfirmationNotification(Booking booking) {
                // Logic to send push notification for booking confirmation
            }
        }
        // HostController.java
        @RestController
        @RequestMapping("/host")
        public class HostController {
            @Autowired
            private PropertyManagementService propertyManagementService;
       
            @PostMapping("/property")
            public ResponseEntity<Property> manageProperty(@RequestBody PropertyRequest propertyRequest) {
                Property property = propertyManagementService.manageProperty(propertyRequest);
                return ResponseEntity.ok(property);
            }
        }
        // SupportController.java
        @RestController
        @RequestMapping("/support")
        public class SupportController {
            @Autowired
            private SupportService supportService;
       
            @PostMapping("/inquiry")
            public ResponseEntity<SupportTicket> createSupportInquiry(@RequestBody SupportInquiryRequest inquiryRequest) {
                SupportTicket ticket = supportService.createInquiry(inquiryRequest);
                return ResponseEntity.ok(ticket);
            }
        }
        @Service
        public class AuthenticationService {
       
            @Autowired
            private UserRepository userRepository;
            @Autowired
            private PasswordEncoder passwordEncoder;
       
            public User registerUser(SignupDto signupDto) {
                if (userRepository.existsByEmail(signupDto.getEmail())) {
                    throw new IllegalStateException("Email already in use.");
                }
       
                User newUser = new User();
                newUser.setEmail(signupDto.getEmail());
                newUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
                // Set additional fields from SignupDto
       
                return userRepository.save(newUser);
            }
       
            // Additional methods for login and authentication
        }
        @Service
        public class PropertyService {
       
            @Autowired
            private PropertyRepository propertyRepository;
       
            public List<Property> searchProperties(SearchCriteria criteria) {
                // Implement search logic based on criteria
                return propertyRepository.findPropertiesByCriteria(criteria);
            }
       
            public Property addProperty(PropertyDto propertyDto) {
                Property property = new Property();
                // Set property details from PropertyDto
                return propertyRepository.save(property);
            }
       
            // Additional methods for property management
        }
        @Service
        public class BookingService {
       
            @Autowired
            private BookingRepository bookingRepository;
            @Autowired
            private AvailabilityService availabilityService;
       
            public Booking createBooking(BookingDto bookingDto) {
                if (!availabilityService.isAvailable(bookingDto.getPropertyId(), bookingDto.getCheckIn(), bookingDto.getCheckOut())) {
                    throw new IllegalStateException("Property is not available for the selected dates.");
                }
       
                Booking booking = new Booking();
                // Set booking details from BookingDto
                return bookingRepository.save(booking);
            }
       
            // Additional methods for booking management
        }
        @Service
        public class MessagingService {
       
            @Autowired
            private MessageRepository messageRepository;
       
            public Message sendMessage(MessageDto messageDto) {
                Message message = new Message();
                // Set message details from MessageDto
                return messageRepository.save(message);
            }
       
            // Additional methods for messaging and communication
        }
        @Service
        public class ReviewService {
       
            @Autowired
            private ReviewRepository reviewRepository;
       
            public Review leaveReview(ReviewDto reviewDto) {
                Review review = new Review();
                // Set review details from ReviewDto
                return reviewRepository.save(review);
            }
       
            // Additional methods for handling reviews and ratings
        }
        @Service
        public class PaymentService {
       
            public PaymentResult processPayment(PaymentDto paymentDto) {
                // Integrate with payment gateway (e.g., Stripe, PayPal)
                // Apply business logic for payment processing
                // Return the result of the payment transaction
            }
       
            // Additional methods for payment processing
        }
        @Configuration
        @EnableWebSecurity
        public class SecurityConfig extends WebSecurityConfigurerAdapter {
       
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/api/public/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic(); // Or any other authentication mechanism
            }
        }
       
        export default BookingForm;
        // PropertyType.java (Enum)
        public enum PropertyType {
            HOME,
            APARTMENT,
            VILLA,
            // Add more property types as needed
        }
        // Property.java (Entity)
        @Entity
        public class Property {
            // Existing fields
            @Enumerated(EnumType.STRING)
            private PropertyType type;
            // Other property details
        }
        // PropertyRepository.java
        public interface PropertyRepository extends JpaRepository<Property, Long> {
            List<Property> findByTypeAndLocationAndPriceBetween(PropertyType type, String location, BigDecimal minPrice, BigDecimal maxPrice);
        }
        // Booking.java (Entity)
        @Entity
        public class Booking {
            // Existing fields
            private LocalDate checkInDate;
            private LocalDate checkOutDate;
            private boolean isRecurring; // Add this field
            // Other booking details
        }
        // BookingService.java
        @Service
        public class BookingService {
            public Booking createLongTermBooking(BookingRequest bookingRequest) {
                // Logic to handle long-term bookings
            }
        }
        // Review.java (Entity)
        @Entity
        public class Review {
            // Existing fields
            private int rating;
            private String comment;
            // Other review details
 
    // FcmService.java
@Service
public class FcmService {
    private String firebaseServerKey = "YOUR_FIREBASE_SERVER_KEY";

    public void sendPushNotification(String deviceToken, String title, String body) throws Exception {
        URL url = new URL("https://fcm.googleapis.com/fcm/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestProperty("Authorization", "key=" + firebaseServerKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        JSONObject message = new JSONObject();
        message.put("to", deviceToken);
        message.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", title);
        notification.put("body", body);

        message.put("notification", notification);

        OutputStream os = conn.getOutputStream();
        os.write(message.toString().getBytes("UTF-8"));
        os.close();

        // Read FCM response
        InputStream is = conn.getInputStream();
        String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
        is.close();

        // Handle the response as needed
    }
}
 // Backend: Java Spring Boot
// SearchService.java@Servicepublic class SearchService {
@Autowired private PropertyRepository propertyRepository;
// Method to search properties based on criteria public List<Property> searchProperties(String location, LocalDate checkIn, LocalDate checkOut, List<String> amenities, Double maxPriceRange) {     // Implement search logic using propertyRepository and criteria     // Return a list of properties that match the criteria }}
// PropertyController.java@RestController@RequestMapping("/api/properties")public class PropertyController {
@Autowired private SearchService searchService;
// Endpoint to handle search requests @GetMapping("/search") public ResponseEntity<List<Property>> searchProperties(@RequestParam String location,                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,                                                       @RequestParam List<String> amenities,                                                       @RequestParam Double maxPriceRange) {     List<Property> searchResults = searchService.searchProperties(location, checkIn, checkOut, amenities, maxPriceRange);     return ResponseEntity.ok(searchResults); }}

// Backend: Spring Boot application

// Security configuration for authentication and authorization
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    // ... configure HTTP security, authentication manager, password encoder
}

// User authentication and management
@RestController
@RequestMapping("/api/users")
public class UserController {
    // ... endpoints for user registration, login, profile management
}

// Property management
@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    // ... endpoints for property listing, detail retrieval, and management
}

// Booking system
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    // ... endpoints for creating and managing bookings
}

// Payment processing
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    // ... endpoints for processing payments using PayPal API
}

// Review management
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    // ... endpoints for creating and retrieving property reviews
}

// Real-time messaging
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    // ... configure WebSocket endpoints and message handling
}

// Notification service
@Service
public class NotificationService {
    // ... methods for sending notifications like booking confirmations
}

// Backend: Spring Boot application

// Service to track user interactions such as likes, shares, comments
@Service
public class UserInteractionService {
    // Method to record interaction
    public void recordInteraction(InteractionType type, Long contentId, Long userId) {
        // Implementation to save the interaction data
    }

    // Method to calculate content score based on interactions
    public Double calculateContentScore(Long contentId) {
        // Implementation to calculate score based on likes, shares, comments
        return score;
    }
}

// Repository to manage content data
@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    // Custom query to find content with high engagement
    @Query("SELECT c FROM Content c ORDER BY c.score DESC")
    List<Content> findHighEngagementContent(Pageable pageable);
}

// Controller to serve high engagement content
@RestController
@RequestMapping("/api/content")
public class ContentController {
    @Autowired
    private ContentRepository contentRepository;

    // Endpoint to get trending content
    @GetMapping("/trending")
    public ResponseEntity<List<Content>> getTrendingContent() {
        Pageable topTen = PageRequest.of(0, 10);
        List<Content> trendingContent = contentRepository.findHighEngagementContent(topTen);
        return ResponseEntity.ok(trendingContent);
    }
}

// Entity to represent content
@Entity
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    private String title;
    private String description;
    private Double score; // Score based on user interactions

    // Getters and setters
}

// Enum for interaction types
public enum InteractionType {
    LIKE, SHARE, COMMENT
}


// Service to handle content distribution and algorithm refinement
@Service
public class ContentDistributionService {
    // ... methods to process content based on user engagement
}

// Repository to manage content data
@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    // ... custom queries to find content based on engagement scores
}

// Controller to serve content based on the distribution algorithm
@RestController
@RequestMapping("/api/content")
public class ContentController {
    // ... endpoints to get content based on the distribution algorithm
}

// Entity to represent content
@Entity
public class Content {
    // ... properties like title, description, engagement score
}

// Frontend: React application

// Component to display content based on the distribution algorithm
const ContentFeed = () => {
    // ... state and effects to fetch and display content
};

// Start the application
ReactDOM.render(<ContentFeed />, document.getElementById('root'));

<dependencies>
    <!-- Other dependencies -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.social</groupId>
        <artifactId>spring-social-facebook</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.social</groupId>
        <artifactId>spring-social-google</artifactId>
    </dependency>
    <!-- ... -->
</dependencies>
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            clientId: YOUR_GOOGLE_CLIENT_ID
            clientSecret: YOUR_GOOGLE_CLIENT_SECRET
            scope:
              - email
              - profile
          facebook:
            clientId: YOUR_FACEBOOK_CLIENT_ID
            clientSecret: YOUR_FACEBOOK_CLIENT_SECRET
            scope:
              - email
              - public_profile
@RestController
@RequestMapping("/auth")
public class OAuthController {
    // Inject dependencies as needed

    @GetMapping("/login/google")
    public ResponseEntity<?> googleLogin() {
        // Handle Google OAuth login
    }

    @GetMapping("/login/facebook")
    public ResponseEntity<?> facebookLogin() {
        // Handle Facebook OAuth login
    }
}
<dependency>
    <groupId>com.twilio.sdk</groupId>
    <artifactId>twilio</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
@Service
public class VerificationService {
    // Inject Twilio or Nexmo client

    public void sendVerificationCode(String phoneNumber) {
        // Use Twilio or Nexmo to send a verification code
    }

    public boolean verifyCode(String phoneNumber, String code) {
        // Verify the code received by the user
    }
}
@Entity
public class User {
    // Existing fields

    private String googleToken;
    private String facebookToken;
    private boolean isPhoneVerified;

    // Getters and setters
}
@Service
public class UserService {
    // Existing methods

    public void saveOAuthDetails(String provider, String token) {
        // Save OAuth token based on the provider (Google/Facebook)
    }

    public void verifyPhoneNumber(String phoneNumber, String code) {
        // Use VerificationService to verify the phone number
    }
}
@RestController
@RequestMapping("/users")
public class UserController {
    // Inject UserService and VerificationService

    @PostMapping("/verify-phone")
    public ResponseEntity<?> verifyPhone(@RequestParam String phoneNumber, @RequestParam String code) {
        // Call UserService to verify the phone number
    }

    @PostMapping("/oauth/google")
    public ResponseEntity<?> oauthGoogle(@RequestParam String token) {
        // Call UserService to save Google OAuth details
    }

    @PostMapping("/oauth/facebook")
    public ResponseEntity<?> oauthFacebook(@RequestParam String token) {
        // Call UserService to save Facebook OAuth details
    }
}

// Backend (Java Spring Boot)

// SMS Service Integration (e.g., UniStack)
@Service
public class SMSService {
    // Method to send SMS using UniStack API
    public void sendSMS(String phoneNumber, String message) {
        // Logic to integrate with UniStack's SMS sending API
    }
}

// OAuth Service Integration (e.g., Authentik)
@Configuration
public class OAuthServiceConfig {
    // Configuration for Authentik OAuth service
}

// Database Configuration (e.g., PostgreSQL)
@Configuration
public class DatabaseConfig {
    // Configuration for connecting to a PostgreSQL database
}

// Cloud Hosting Configuration (e.g., Google Cloud Platform)
@Configuration
public class CloudHostingConfig {
    // Configuration for deploying to Google Cloud Platform
}



 
// Entities@Entitypublic class Booking { // ... existing fields ... private String paymentMethod; // "PayPal" or "Stripe" // ... constructors, getters, and setters ...}
// Repositories// ... existing repositories ...
// Services@Servicepublic class PaymentService { // ... existing payment logic ...
// Method to process PayPal payment public PaymentStatus processPayPalPayment(String paymentId, double amount) {     // PayPal payment processing logic     // Return payment status }
// Method to process Stripe payment public PaymentStatus processStripePayment(String stripeToken, double amount) {     // Stripe payment processing logic     // Return payment status }}
// Controller@RestController@RequestMapping("/api/bookings")public class BookingController { // ... existing autowired services ...
@Autowired private PaymentService paymentService;
@PostMapping("/pay-and-book") public ResponseEntity<?> payAndBook(@RequestBody PaymentRequest paymentRequest) {     try {         Booking booking;         if (paymentRequest.getPaymentMethod().equals("Stripe")) {             Charge charge = stripeService.chargeCreditCard(paymentRequest.getPaymentToken(), paymentRequest.getAmount());             if (charge.getStatus().equals("succeeded")) {                 booking = bookingService.markAsBooked(paymentRequest.getPropertyId(), paymentRequest.getUserId());                 booking.setPaymentMethod("Stripe");             } else {                 // Handle Stripe payment failure             }         } else if (paymentRequest.getPaymentMethod().equals("PayPal")) {             PaymentStatus paymentStatus = paymentService.processPayPalPayment(paymentRequest.getPaymentId(), paymentRequest.getAmount());             if (paymentStatus == PaymentStatus.SUCCESS) {                 booking = bookingService.markAsBooked(paymentRequest.getPropertyId(), paymentRequest.getUserId());                 booking.setPaymentMethod("PayPal");             } else {                 // Handle PayPal payment failure             }         } else {             // Handle unsupported payment method         }         return ResponseEntity.ok(booking);     } catch (Exception e) {         // Handle exceptions     }     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); }}// 1. Validation// In your service layer, validate the input data before processingpublic class BookingService { public Booking createBooking(BookingDto bookingDto) {     validateBookingDto(bookingDto);     // Proceed with booking creation logic }
private void validateBookingDto(BookingDto bookingDto) {     // Perform validation checks     if (bookingDto.getPropertyId() == null || bookingDto.getUserId() == null) {         throw new IllegalArgumentException("Property ID and User ID cannot be null");     }     // Additional validation logic }}
// 2. Error Handling// Use @ControllerAdvice to handle exceptions globally@ControllerAdvicepublic class GlobalExceptionHandler { @ExceptionHandler(value = {IllegalArgumentException.class}) public ResponseEntity<Object> handleInvalidInputException(IllegalArgumentException ex) {     // Provide a user-friendly error message     return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); } // Other exception handlers}
// 3. Security// Configure security settings in SecurityConfiguration.java@Configuration@EnableWebSecuritypublic class SecurityConfiguration extends WebSecurityConfigurerAdapter { @Override protected void configure(HttpSecurity http) throws Exception {     http         .csrf().disable() // Disable CSRF for stateless API         .authorizeRequests()         .antMatchers("/api/public/**").permitAll() // Public endpoints         .anyRequest().authenticated() // All other endpoints require authentication         .and()         .httpBasic(); // Use HTTP Basic Authentication     // Additional security configurations }}
// 4. Logging// Use SLF4J for logging throughout your application@Servicepublic class PaymentService { private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
public PaymentStatus processPayment(PaymentDto paymentDto) {        logger.info("Processing payment for user {}", paymentDto.getUserId());     // Payment processing logic }}
// 5. Unit Testing// Write unit tests for your services and controllers@SpringBootTestpublic class BookingServiceTest { @Autowired private BookingService bookingService;
@Test public void whenValidInput_thenCreateBooking() {     BookingDto bookingDto = new BookingDto(/* valid data */);     Booking result = bookingService.createBooking(bookingDto);     assertNotNull(result);     // Additional assertions }}
// 6. Documentation// Use Javadoc comments and README.md to document your codebase/*** Creates a new booking based on the provided BookingDto.* @param bookingDto the data transfer object containing booking details* @return the created Booking entity* @throws IllegalArgumentException if the input data is invalid*/public Booking createBooking(BookingDto bookingDto) { // Method implementation}
// README.md# Project TitleDescription of the project and its functionalities.## Getting StartedInstructions on setting up the project locally.### PrerequisitesRequired software and tools.### InstallationStep-by-step guide to install and run the project.// ...
// 7. Performance Optimization// Review and optimize your code for performancepublic class PropertyService { @Autowired private PropertyRepository propertyRepository;
@Cacheable("properties") public List<Property> searchProperties(SearchCriteria criteria) {     // Use efficient database queries and caching for performance }}// 1. Transaction Management// Use @Transactional to manage transactions in your service layer@Servicepublic class BookingService { @Transactional public Booking createBooking(BookingDto bookingDto) {     // Business logic that involves multiple database operations }}
// 2. API Versioning// Version your API endpoints to manage changes and maintain backward compatibility@RestController@RequestMapping("/api/v1/bookings")public class BookingControllerV1 { // API endpoints for version 1}
@RestController@RequestMapping("/api/v2/bookings")public class BookingControllerV2 { // API endpoints for version 2}
// 3. Request and Response Logging// Use an interceptor or filter to log incoming requests and outgoing responses@Componentpublic class RequestResponseLoggingFilter extends OncePerRequestFilter { private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
@Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {     // Log the incoming request        logger.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());     // Continue with the filter chain     filterChain.doFilter(request, response);     // Log the outgoing response        logger.info("Outgoing response status: {}", response.getStatus()); }}
// 4. Rate Limiting and Throttling// Implement rate limiting using a library like Bucket4j or a custom solution@Beanpublic FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter() { FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>(); registrationBean.setFilter(new RateLimitingFilter()); registrationBean.addUrlPatterns("/api/*"); return registrationBean;}
// 5. Swagger Documentation// Configure Swagger to generate interactive API documentation@Configuration@EnableSwagger2public class SwaggerConfig { @Bean public Docket apiDocket() {     return new Docket(DocumentationType.SWAGGER_2)             .select()             .apis(RequestHandlerSelectors.basePackage("com.example"))             .paths(PathSelectors.any())             .build(); }}
// 6. Continuous Integration/Continuous Deployment (CI/CD)// Example of a Jenkinsfile for setting up a CI/CD pipelinepipeline { agent any stages {     stage('Build') {         steps {             sh 'mvn clean package'         }     }     stage('Test') {         steps {             sh 'mvn test'         }     }     stage('Deploy') {         steps {             sh 'deploy-script.sh'         }     } }}
// ... Existing code ...
// 1. Database Migrations (Flyway)// application.propertiesspring.flyway.enabled=truespring.flyway.locations=classpath:db/migration
// 2. Internationalization (i18n)// messages_en.propertiesbooking.created=Booking created successfully!booking.updated=Booking updated successfully!
// 3. Auditing@EntityListeners(AuditingEntityListener.class)public class Booking { // ... @CreatedDate private LocalDateTime createdAt; @LastModifiedDate private LocalDateTime updatedAt; @CreatedBy private String createdBy; // ...}
// 4. Caching Strategies (Redis)@Cacheable("properties")public List<Property> searchProperties(SearchCriteria criteria) { // Fetch properties from the database}
// 5. Microservices Architecture// Separate services: BookingService, PaymentService, UserService
// 6. Asynchronous Processing (RabbitMQ)@Servicepublic class EmailService { @Autowired private RabbitTemplate rabbitTemplate;
public void sendBookingConfirmationEmail(String userEmail, String bookingId) {     // Send a message to RabbitMQ queue     rabbitTemplate.convertAndSend("booking-email-queue", "Booking confirmation for " + userEmail); }}
// 7. Security Enhancements (OAuth 2.0)@Configuration@EnableAuthorizationServerpublic class OAuth2Config extends AuthorizationServerConfigurerAdapter { // Configure OAuth clients, tokens, etc.}
// 8. Monitoring and Alerting (Prometheus and Grafana)// Prometheus configurationmanagement.endpoint.prometheus.enabled=truemanagement.endpoints.web.exposure.include=prometheus
// Grafana dashboard for Spring Boot metrics
// 9. Containerization (Docker and Kubernetes)// Dockerfile for each service// Kubernetes deployment files (deployment.yaml, service.yaml)
// ... Other code ...
// Internationalization (i18n)// messages.properties (default)welcome.message=Welcome to our application!
// messages_fr.properties (French)welcome.message=Bienvenue dans notre application!
// Auditing with Spring Data JPA@EntityListeners(AuditingEntityListener.class)public class AuditableEntity { @CreatedBy private User createdBy;
@LastModifiedBy private User lastModifiedBy;
@CreatedDate private Instant createdDate;
@LastModifiedDate private Instant lastModifiedDate;}
// Caching with Redis@Cacheable(value = "users", key = "#userId")public User getUserById(String userId) { // Method implementation}
// Microservices Architecture// User Service - handles user-related operations// Booking Service - handles booking-related operations// Payment Service - handles payment processing
// Asynchronous Processing with RabbitMQpublic class AsyncEmailService { @Autowired private RabbitTemplate rabbitTemplate;
public void sendEmail(String destination, String message) {     rabbitTemplate.convertAndSend("emailQueue", new EmailMessage(destination, message)); }}
// Security with OAuth 2.0@Configuration@EnableResourceServerpublic class ResourceServerConfig extends ResourceServerConfigurerAdapter { @Override public void configure(HttpSecurity http) throws Exception {     http         .authorizeRequests()         .antMatchers("/public/**").permitAll()         .anyRequest().authenticated(); }}
// Monitoring with Prometheus and Grafana// application.propertiesmanagement.endpoints.web.exposure.include=prometheusmanagement.metrics.export.prometheus.enabled=true
// Docker Containerization// DockerfileFROM openjdk:11-jdk-slimCOPY target/app.jar app.jarENTRYPOINT ["java","-jar","/app.jar"]
// Kubernetes Deployment// deployment.yamlapiVersion: apps/v1kind: Deploymentmetadata:  name: app-deploymentspec:  replicas: 3  selector: matchLabels:   app: app  template: metadata:   labels:     app: app spec:   containers:   - name: app     image: app:latest     ports:     - containerPort: 8080


// MessagingService.java
@Service
public class MessagingService {
    // Inject dependencies
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private TranslationService translationService;
    // Other necessary services like UserRepository

    // Send a message
    public Message sendMessage(Long senderId, Long recipientId, String content, MultipartFile attachment) {
        // Save message to the database
        // Handle file attachment if present
        // Return the saved message
    }

    // Read receipt update
    public void updateReadReceipt(Long messageId, Long userId, ReadStatus status) {
        // Update the message's read status for the user
    }

    // Retrieve conversation history
    public List<Message> getConversationHistory(Long userId1, Long userId2) {
        // Fetch messages between two users
    }

    // Search within messages
    public List<Message> searchMessages(Long userId, String searchTerm) {
        // Search for messages containing the searchTerm
    }

    // Translate message
    public String translateMessage(String content, Language targetLanguage) {
        // Use the translation service to translate the message content
    }

    // Report inappropriate message
    public void reportMessage(Long messageId, Long reporterId) {
        // Handle the reporting of a message
    }

    // Block user
    public void blockUser(Long requesterId, Long blockedUserId) {
        // Block communication between two users
    }
}

// MessageRepository.java
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Define methods for CRUD operations and custom queries
}

// TranslationService.java
@Service
public class TranslationService {
    public String translate(String content, Language sourceLanguage, Language targetLanguage) {
        // Implement translation logic or integrate with an external API
    }
}

// Message.java
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderId;
    private Long recipientId;
    private String content;
    private String attachmentPath; // Path to the stored file
    private LocalDateTime sentTime;
    private ReadStatus readStatus;
    // Constructors, getters, setters
}

// ReadStatus.java
public enum ReadStatus {
    SENT, DELIVERED, READ
}

// Language.java
public enum Language {
    ENGLISH, ZULU
    // Other languages
}

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/properties")
public class PropertyFilterController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/filter")
    public ResponseEntity<List<Property>> filterProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) List<String> amenities,
            @RequestParam(required = false) PropertyType type) {
       
        List<Property> filteredProperties = propertyService.getAllProperties().stream()
            .filter(p -> (location == null || p.getLocation().equalsIgnoreCase(location)))
            .filter(p -> (minPrice == null || p.getPrice() >= minPrice))
            .filter(p -> (maxPrice == null || p.getPrice() <= maxPrice))
            .filter(p -> (type == null || p.getType() == type))
            .filter(p -> (amenities == null || p.getAmenities().containsAll(amenities)))
            .collect(Collectors.toList());

        return ResponseEntity.ok(filteredProperties);
    }
}

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }
}


// In PropertyService.java
public List<Property> searchProperties(SearchCriteria criteria) {
    // Logic to filter properties based on destination, date range, trip duration, and number of travelers
}
// In PropertyController.java
@GetMapping("/properties/monthly")
public ResponseEntity<List<Property>> getMonthlyStays() {
    // Logic to return properties available for monthly stays
}

@GetMapping("/properties/category/{category}")
public ResponseEntity<List<Property>> getByCategory(@PathVariable String category) {
    // Logic to return properties based on category
}
// In PropertyService.java
public List<Property> searchByLocation(String location) {
    // Use geocoding API to convert location to coordinates and then search
}
// In PropertyService.java
public List<Property> filteredSearch(FilterCriteria filterCriteria) {
    // Logic to apply various filters like type of place, price range, etc.
}
// In MessageController.java
@PostMapping("/messages/contact-host")
public ResponseEntity<Message> contactHost(@RequestBody ContactHostDto contactHostDto) {
    // Logic for guest to contact host
}

// In MeetupService.java
public Meetup arrangeMeetup(MeetupDetails details) {
    // Logic to arrange meetups between host and guest
}
// In ReviewController.java
@PostMapping("/reviews/leave-feedback")
public ResponseEntity<Review> leaveFeedback(@RequestBody FeedbackDto feedbackDto) {
    // Logic for leaving feedback about the host
}
// In HostController.java
@PostMapping("/host/list-property")
public ResponseEntity<Property> listProperty(@RequestBody PropertyDto propertyDto) {
    // Logic for host to upload property listings
}
// In WebSocketConfig.java
// Configuration for WebSocket to enable real-time messaging
// In PaymentController.java
@PostMapping("/payments/process")
public ResponseEntity<Payment> processPayment(@RequestBody PaymentDto paymentDto) {
    // Logic to process payments using PayPal API
}
// In SecurityConfiguration.java
// Configuration for OAuth2, JWT tokens, and other security mechanisms
// In signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    // Frontend validation logic
});
// In GlobalExceptionHandler.java
@ControllerAdvice
public class GlobalExceptionHandler {
    // Global exception handling logic
}
// In PropertyType.java
public enum PropertyType {
    // Enum for different property types
}

// In PropertyRepository.java
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom queries to find properties based on filters
}
@Service
public class SearchService {
    // Inject repositories and other services as needed

    public List<Property> searchProperties(SearchCriteria criteria) {
        // Implement the logic to filter properties based on the criteria
        // This can include destination, date range, trip duration, etc.
        return new ArrayList<>();
    }
}


@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/update")
    public ResponseEntity<User> updateProfile(@RequestBody UserDto userDto) {
        // Ensure that the phone number is not included in the UserDto if it's not meant to be shared
        User updatedUser = userProfileService.updateUser(userDto);
        return ResponseEntity.ok(updatedUser);
    }
}
@Service
public class UserProfileService {

    @Autowired
    private UserRepository userRepository;

    public User updateUser(UserDto userDto) {
        // Convert UserDto to User entity while omitting the phone number
        // Update user details in the database
        return userRepository.save(user);
    }
}
@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/update")
    public ResponseEntity<User> updateProfile(@RequestBody User user) {
        User updatedUser = userProfileService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        String photoUrl = userProfileService.uploadPhoto(file);
        return ResponseEntity.ok(photoUrl);
    }
}
@Service
public class UserProfileService {

    // Assume we have an injected repository and photo storage service
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhotoStorageService photoStorageService;

    public User updateUser(User user) {
        // Update user details logic
        return userRepository.save(user);
    }

    public String uploadPhoto(MultipartFile file) {
        // Photo upload logic
        return photoStorageService.store(file);
    }
}
@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> getFilteredProperties(FilterCriteria filterCriteria) {
        // Logic to apply filters and return a list of properties
        return propertyRepository.findByFilters(filterCriteria);
    }
}

public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Method to find properties based on the applied filters
    List<Property> findByFilters(FilterCriteria filterCriteria);
}
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/search/filtered")
    public ResponseEntity<List<Property>> getFilteredSearchResults(@RequestBody FilterCriteria filterCriteria) {
        List<Property> filteredProperties = propertyService.getFilteredProperties(filterCriteria);
        return ResponseEntity.ok(filteredProperties);
    }
}
@Service
public class FilteredSearchService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> getFilteredProperties(FilterCriteria filterCriteria) {
        // Logic to retrieve and return properties based on applied filters
        return propertyRepository.findPropertiesWithFilters(filterCriteria);
    }
}

public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Existing queries

    List<Property> findPropertiesWithFilters(FilterCriteria filterCriteria);
}
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private FilteredSearchService filteredSearchService;

    @PostMapping("/filtered")
    public ResponseEntity<List<Property>> getFilteredProperties(@RequestBody FilterCriteria filterCriteria) {
        List<Property> filteredProperties = filteredSearchService.getFilteredProperties(filterCriteria);
        return ResponseEntity.ok(filteredProperties);
    }
}
@Service
public class TrendingService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> getTrendingProperties() {
        // Logic to retrieve and return properties that are trending
        return propertyRepository.findTrendingProperties();
    }
}

public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Existing queries

    @Query("SELECT p FROM Property p WHERE p.isTrending = true")
    List<Property> findTrendingProperties();
}
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private TrendingService trendingService;

    @GetMapping("/trending")
    public ResponseEntity<List<Property>> getTrendingProperties() {
        List<Property> trendingProperties = trendingService.getTrendingProperties();
        return ResponseEntity.ok(trendingProperties);
    }
}
@Service
public class SearchService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> personalizedSearch(SearchCriteria criteria) {
        // Logic to provide personalized property recommendations
        return propertyRepository.findPropertiesWithPersonalizedFilters(criteria);
    }
}

public class SearchCriteria {
    private String destination;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfGuests;
    // Additional fields for personalized search features
}
@Service
public class SearchService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> searchBookings(SearchCriteria criteria) {
        // Logic to search for properties based on booking criteria
        return propertyRepository.findBookingsWithFilters(criteria);
    }
}
// PropertyService.java
public List<Property> findTrendingProperties() {
    // Logic to determine and return trending properties
}
// PropertyService.java
public List<Property> filteredSearch(FilterCriteria filterCriteria) {
    // Logic to apply various filters like type of place, price range, etc.
}
// In PropertyController.java
@GetMapping("/properties/trending")
public ResponseEntity<List<Property>> getTrendingProperties() {
    // Logic to return properties that are trending
}
// In PropertyService.java
public List<Property> findTrendingProperties() {
    // Logic to determine and return trending properties
}
// In PropertyRepository.java
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Existing custom queries to find properties based on filters

    // Method to retrieve trending properties
    List<Property> findTrendingProperties();
}
// In PropertyService.java
public List<Property> filteredSearch(FilterCriteria filterCriteria) {
    // Logic to apply various filters like type of place, price range, etc.
}
// PropertyService.java
public List<Property> filteredSearch(FilterCriteria filterCriteria) {
    // Logic to apply various filters like type of place, price range, etc.
}
// PropertyService.java
public List<Property> searchProperties(SearchCriteria criteria) {
    // Logic to filter properties based on destination, date range, trip duration, and number of travelers
}
@Service
public class SearchService {
    // Inject repositories and other services as needed

    public List<Property> searchProperties(SearchCriteria criteria) {
        // Implement the logic to filter properties based on the criteria
        // This can include destination, date range, trip duration, etc.
        return new ArrayList<>();
    }
}

// In PropertyType.java
public enum PropertyType {
    // Enum for different property types
}

// In PropertyRepository.java
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom queries to find properties based on filters
}
// In GlobalExceptionHandler.java
@ControllerAdvice
public class GlobalExceptionHandler {
    // Global exception handling logic
}

// In SecurityConfiguration.java
// Configuration for OAuth2, JWT tokens, and other security mechanisms
// In PaymentController.java
@PostMapping("/payments/process")
public ResponseEntity<Payment> processPayment(@RequestBody PaymentDto paymentDto) {
    // Logic to process payments using PayPal API
}
// In WebSocketConfig.java
// Configuration for WebSocket to enable real-time messaging
// In HostController.java
@PostMapping("/host/list-property")
public ResponseEntity<Property> listProperty(@RequestBody PropertyDto propertyDto) {
    // Logic for host to upload property listings
}
// In ReviewController.java
@PostMapping("/reviews/leave-feedback")
public ResponseEntity<Review> leaveFeedback(@RequestBody FeedbackDto feedbackDto) {
    // Logic for leaving feedback about the host
}
// In MessageController.java
@PostMapping("/messages/contact-host")
public ResponseEntity<Message> contactHost(@RequestBody ContactHostDto contactHostDto) {
    // Logic for guest to contact host
}

// In MeetupService.java
public Meetup arrangeMeetup(MeetupDetails details) {
    // Logic to arrange meetups between host and guest
}
// In PropertyService.java
public List<Property> filteredSearch(FilterCriteria filterCriteria) {
    // Logic to apply various filters like type of place, price range, etc.
}
// In PropertyController.java
@GetMapping("/properties/monthly")
public ResponseEntity<List<Property>> getMonthlyStays() {
    // Logic to return properties available for monthly stays
}

@GetMapping("/properties/category/{category}")
public ResponseEntity<List<Property>> getByCategory(@PathVariable String category) {
    // Logic to return properties based on category
}
// In PropertyService.java
public List<Property> searchProperties(SearchCriteria criteria) {
    // Logic to filter properties based on destination, date range, trip duration, and number of travelers
}
public class UserResponse {
    private boolean success;
    private String message;
    private User data;

    public UserResponse(boolean success, String message, User data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
}
@GetMapping("/users/{id}")
public ResponseEntity<UserResponse> getUserById(@PathVariable(value = "id") Long userId) {
    User user = userService.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
   
    UserResponse userResponse = new UserResponse(true, "User retrieved successfully", user);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
}
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
   
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
}
public class ErrorResponse {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorResponse(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    // Getters and Setters
}
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
       
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Specific exception handlers below (e.g., for custom exceptions)
}
public class SearchCriteria {
    private String destination;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int duration;
    private int guests;
    // Other fields and getters/setters
}
@Service
public class SearchService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> searchProperties(SearchCriteria criteria) {
        // Implement the logic to apply filters based on the SearchCriteria object
        // This can include destination, date range, trip duration, etc.
        return propertyRepository.findAllWithFilters(criteria);
    }
}

 
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @PostMapping
    public ResponseEntity<String> addReview(@RequestBody ReviewDto reviewDto) {
        // Logic to add a review
        return ResponseEntity.ok("Review added successfully!");
    }
}
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
        // Logic to process payment
        return ResponseEntity.ok("Payment processed successfully!");
    }
}
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @GetMapping
    public ResponseEntity<List<PropertyDto>> getProperties(@RequestParam String location,
                                                           @RequestParam LocalDate startDate,
                                                           @RequestParam LocalDate endDate) {
        // Logic to fetch properties based on search criteria
        List<PropertyDto> properties = ...;
        return ResponseEntity.ok(properties);
    }
}
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody BookingRequest bookingRequest) {
        // Logic to create a booking
        return ResponseEntity.ok("Booking created successfully!");
    }
}
@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long userId) {
        // Logic to fetch user profile
        UserProfileDto userProfile = ...;
        return ResponseEntity.ok(userProfile);
    }
}
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MessageHandler(), "/ws/messages");
    }
}

@Controller
public class MessageController {

    @MessageMapping("/send")
    public void sendMessage(MessageDto message) {
        // Logic to handle real-time messages
    }
}
@Service
public class NotificationService {

    public void sendBookingConfirmationEmail(UserDto user, BookingDto booking) {
        // Logic to send an email
    }
}
@RestController
@RequestMapping("/api/hosts")
public class HostController {

    @PutMapping("/{propertyId}")
    public ResponseEntity<String> updateProperty(@PathVariable Long propertyId,
                                                 @RequestBody PropertyUpdateRequest updateRequest) {
        // Logic to update property details
        return ResponseEntity.ok("Property updated successfully!");
    }
}

@RestController
@RequestMapping("/api/support")
public class SupportController {

    @PostMapping("/inquiries")
    public ResponseEntity<String> createSupportInquiry(@RequestBody SupportInquiryRequest inquiryRequest) {
        // Logic to create a support inquiry
        return ResponseEntity.ok("Support inquiry submitted!");
    }
}
@Service
public class AuthenticationService {

    public void registerUser(UserRegistrationDto registrationDto) {
        // Logic to register a new user
    }

    public UserDto loginUser(LoginRequest loginRequest) {
        // Logic to authenticate and return user details
        return ...;
    }
}
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private List<String> photos; // Store photo URLs
    // Other fields: tags, pricing, visitor details

    // Getters and setters
}
// Example: Sending a notification
@Service
public class NotificationService {
    public void sendLikeNotification(User recipient, Property property) {
        // Logic to send a like notification
    }
}
@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long userId) {
        // Logic to fetch user profile (including property photos)
        UserProfileDto userProfile = ...;
        return ResponseEntity.ok(userProfw  ile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long userId,
                                                    @RequestBody UserProfileUpdateRequest updateRequest) {
        // Logic to update user profile
        return ResponseEntity.ok("Profile updated successfully!");
    }
}
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody BookingRequest bookingRequest) {
        // Logic to create a booking
        return ResponseEntity.ok("Booking created successfully!");
    }
}

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
        // Logic to process payment
        return ResponseEntity.ok("Payment processed successfully!");
    }
}
// Example: Retrieving search history
@RestController
@RequestMapping("/api/history")
public class HistoryController {
    @GetMapping("/{userId}")
    public ResponseEntity<List<SearchHistoryDto>> getSearchHistory(@PathVariable Long userId) {
        // Logic to fetch search history
        List<SearchHistoryDto> history = ...;
        return ResponseEntity.ok(history);
    }
}
@RestController
@RequestMapping("/api/account")
public class AccountController {
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateAccount(@PathVariable Long userId,
                                                @RequestBody AccountUpdateRequest updateRequest) {
        // Logic to update account details
        return ResponseEntity.ok("Account updated successfully!");
    }
}
// Example: Referral service
@Service
public class ReferralService {
    public void rewardReferralPoints(User referrer, User referredUser) {
        // Logic to reward points
    }
}
@RestController
@RequestMapping("/api/support")
public class SupportController {
    @PostMapping("/inquiries")
    public ResponseEntity<String> createSupportInquiry(@RequestBody SupportInquiryRequest inquiryRequest) {
        // Logic to create a support inquiry
        return ResponseEntity.ok("Support inquiry submitted!");
    }
}
// Example: Password change service
@Service
public class SecurityService {
    public void changePassword(User user, String newPassword) {
        // Logic to update user's password
    }
}
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private List<String> photos; // Store photo URLs
    // Other fields: tags, pricing, visitor details

    // Getters and setters
}
@Service
public class NotificationService {
    public void sendLikeNotification(User recipient, Property property) {
        // Logic to send a like notification
    }

    public void sendMessage(User sender, User recipient, String message) {
        // Logic to send a message
    }
}
@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long userId) {
        // Logic to fetch user profile (including property photos)
        UserProfileDto userProfile = ...;
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long userId,
                                                    @RequestBody UserProfileUpdateRequest updateRequest) {
        // Logic to update user profile
        return ResponseEntity.ok("Profile updated successfully!");
    }
}
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody BookingRequest bookingRequest) {
        // Logic to create a booking
        return ResponseEntity.ok("Booking created successfully!");
    }
}

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
        // Logic to process payment
        return ResponseEntity.ok("Payment processed successfully!");
    }
}
@RestController
@RequestMapping("/api/history")
public class HistoryController {
    @GetMapping("/{userId}")
    public ResponseEntity<List<SearchHistoryDto>> getSearchHistory(@PathVariable Long userId) {
        // Logic to fetch search history
        List<SearchHistoryDto> history = ...;
        return ResponseEntity.ok(history);
    }
}
@RestController
@RequestMapping("/api/account")
public class AccountController {
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateAccount(@PathVariable Long userId,
                                                @RequestBody AccountUpdateRequest updateRequest) {
        // Logic to update account details
        return ResponseEntity.ok("Account updated successfully!");
    }
}
@Service
public class ReferralService {
    public void rewardReferralPoints(User referrer, User referredUser) {
        // Logic to reward points
    }
}
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    // Security configuration details (e.g., OAuth, 2FA)
}
    import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class PropertyLocationApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyLocationApplication.class, args);
    }
}

@RestController
class PropertyLocationController {

    @GetMapping("/api/properties")
    public String listProperties() {
        // Logic to retrieve property data (e.g., from a database)
        // Convert property addresses to latitude and longitude using Google Geocoding API
        // Display property markers on a map
        return "List of properties with location markers";
    }
}
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PropertyLocationApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyLocationApplication.class, args);
    }
}
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private double latitude;
    private double longitude;

    // Getters and setters
}
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Custom query methods (if needed)
}
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PropertyLocationController {

    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping("/api/properties")
    public List<Property> listProperties() {
        // Logic to retrieve property data from the database
        return propertyRepository.findAll();
    }
}

