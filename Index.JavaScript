// app.js
document.getElementById('search-form').addEventListener('submit', function(event) {
    event.preventDefault();
    const location = document.getElementById('location').value;
    const date = document.getElementById('date').value;
    
    // Call the backend API to search properties
    fetch(`/search?location=${location}&date=${date}`)
        .then(response => response.json())
        .then(properties => {
            const propertyList = document.getElementById('property-list');
            propertyList.innerHTML = ''; // Clear the list
            properties.forEach(property => {
                const listItem = document.createElement('div');
                listItem.innerHTML = `
                    <h3>${property.name}</h3>
                    <p>${property.description}</p>
                    <p>Located in: ${property.location}</p>
                `;
                propertyList.appendChild(listItem);
            });
        })
        .catch(error => console.error('Error:', error));
});


// booking.js
document.getElementById('booking-form').addEventListener('submit', function(event) {
    event.preventDefault();

    // Input validation
    const location = document.getElementById('location').value;
    const checkInDate = document.getElementById('check-in-date').value;
    const checkOutDate = document.getElementById('check-out-date').value;
    const guests = document.getElementById('guests').value;

    if (!location || !checkInDate || !checkOutDate || guests <= 0) {
        displayError('Please fill in all fields correctly.');
        return;
    }

    // Prepare data for submission
    const bookingData = {
        location,
        checkIn: checkInDate,
        checkOut: checkOutDate,
        guests
    };

    // Call the backend API to book the property
    fetch('/api/book', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(bookingData),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        document.getElementById('booking-response').textContent = 'Booking successful! Confirmation number: ' + data.confirmationNumber;
    })
    .catch(error => {
        displayError('Failed to book: ' + error.message);
    });
});

function displayError(message) {
    const responseDiv = document.getElementById('booking-response');
    responseDiv.textContent = message;
    responseDiv.style.backgroundColor = '#f8d7da';
    responseDiv.style.color = '#721c24';
}


// image-upload.js (Frontend JavaScript for handling image uploads)
document.getElementById('image-upload-form').addEventListener('submit', function(event) {
    event.preventDefault();
    const formData = new FormData(this);

    fetch('/images/upload', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        console.log('Image uploaded successfully');
        // Update the UI to show the uploaded image
    })
    .catch(error => {
        console.error('Error uploading image:', error);
    });
});


// notifications.js
function loadNotifications() {
    fetch('/api/notifications')
        .then(response => response.json())
        .then(notifications => {
            const notificationsContainer = document.getElementById('notifications');
            notifications.forEach(notification => {
                const notificationElement = document.createElement('div');
                notificationElement.innerText = notification.message;
                notificationsContainer.appendChild(notificationElement);
            });
        });
}

// chat.js
var stompClient = null;

function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/public', function(chatMessage) {
            showMessage(JSON.parse(chatMessage.body).content);
        });
    });
}

function sendMessage() {
    var messageContent = document.getElementById('messageInput').value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            content: messageContent,
            // ... other message fields like sender
        };
        stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));
        document.getElementById('messageInput').value = '';
    }
}

function showMessage(message) {
    // Logic to append the message to the chat view
}

// Call connect when the page loads
connect();


// service-worker.js
self.addEventListener('push', function(event) {
    const data = event.data.json();
    const { title, body } = data.notification;

    const options = {
        body: body,
        // Other options like icons, actions, etc.
    };

    event.waitUntil(
        self.registration.showNotification(title, options)
    );
});

// Register service worker and request permission for notifications
if ('serviceWorker' in navigator && 'PushManager' in window) {
    navigator.serviceWorker.register('/service-worker.js
.then(function(swReg) {
            console.log('Service Worker is registered', swReg);

            swReg.pushManager.getSubscription()
                .then(function(subscription) {
                    if (subscription === null) {
                        // Ask for permission and subscribe for push notifications
                        Notification.requestPermission().then(permission => {
                            if (permission === "granted") {
                                swReg.pushManager.subscribe({
                                    userVisibleOnly: true,
                                    applicationServerKey: 'YOUR_VAPID_PUBLIC_KEY'
                                }).then(function(subscription) {
                                    // Send subscription object to the server
                                });
                            }
                        });
                    }
                });
        })
        .catch(function(error) {
            console.error('Service Worker Error', error);
        });
} else {
    console.warn('Push messaging is not supported');
} 


// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    const termsCheckbox = document.getElementById('terms-checkbox');

    if (!termsCheckbox.checked) {
        alert('You must agree to the terms and conditions to sign up.');
        return;
    }

    // Proceed with the sign-up process
    // ...
});


// Example in Node.js for a referral system

// User model with a referralCode and referredBy properties
class User {
  constructor(name, email) {
    this.name = name;
    this.email = email;
    this.referralCode = generateReferralCode();
    this.referredBy = null;
  }
}

// Function to generate a unique referral code
function generateReferralCode() {
  // Implementation to generate a unique code
}

// Function to handle a new referral
function addReferral(referrerEmail, referredEmail) {
  const referrer = findUserByEmail(referrerEmail);
  const referred = new User('New User', referredEmail);
  referred.referredBy = referrer.referralCode;

  // Add logic to reward the referrer
  rewardReferrer(referrer);

  // Save the new user to the database
  saveUser(referred);
}

// Function to reward the referrer
function rewardReferrer(user) {
  // Implementation to add rewards to the referrer's account
}

// Function to find a user by email
function findUserByEmail(email) {
  // Implementation to find and return a user from the database
}

// Function to save a new user to the database
function saveUser(user) {
  // Implementation to save the user to the database
}
// Existing User model in your codebase
class ExistingUserModel {
  constructor(name, email, /* other properties */) {
    this.name = name;
    this.email = email;
    // Initialize other properties
    // ...
    // Add referral properties
    this.referralCode = generateReferralCode();
    this.referredBy = null;
  }

  // Existing methods
  // ...

  // New method to generate a referral code
  generateReferralCode() {
    // Your implementation here
  }

  // Other referral-related methods
  // ...
}

const express = require('express');
const mongoose = require('mongoose');
const app = express();

app.use(express.json());

// User model
const userSchema = new mongoose.Schema({
    name: String,
    email: String,
    referralCode: String,
    referredBy: String,
    rewardPoints: { type: Number, default: 0 }
});

const User = mongoose.model('User', userSchema);

// Referral endpoint
app.post('/refer', async (req, res) => {
    const { referralCode } = req.body;
    try {
        // Find the user who was referred
        const referredUser = await User.findOne({ referralCode: referralCode });
        if (!referredUser) {
            return res.status(404).send('Referral code not found.');
        }
        // Update the referrer's reward points
        const referrer = await User.findById(referredUser.referredBy);
        referrer.rewardPoints += 10; // Assuming each referral gives 10 points
        await referrer.save();
        res.send('Referral successful and rewards updated.');
    } catch (error) {
        res.status(500).send(error.message);
    }
});

// Connect to MongoDB and start the server
mongoose.connect('mongodb://localhost:27017/referralDB', { useNewUrlParser: true, useUnifiedTopology: true })
    .then(() => app.listen(3000, () => console.log('Server running on port 3000')))
    .catch(err => console.error(err));


// image-upload.js
document.getElementById('image-upload-form').addEventListener('submit', function(event) {
    event.preventDefault();
    const formData = new FormData(this);

    fetch('/images/upload', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        console.log('Image uploaded successfully');
        // Update the UI to show the uploaded image
    })
    .catch(error => {
        console.error('Error uploading image:', error);
    });
});

// image-upload.js
document.getElementById('image-upload-form').addEventListener('submit', function(event) {
    event.preventDefault();
    const formData = new FormData(this);

    fetch('/images/upload', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        console.log('Image uploaded successfully');
        // Update the UI to show the uploaded image
    })
    .catch(error => {
        console.error('Error uploading image:', error);
    });
});

// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    // Sign-up logic with terms and conditions validation
});


// Push Notifications (service-worker.js)
self.addEventListener('push', function(event) {
    // Notification display logic...
});

// Register service worker and request permission for notifications
if ('serviceWorker' in navigator && 'PushManager' in window) {
    // Service worker registration and push subscription logic...
}

/* Add your existing styles here */

/* New styles for the booking form and image upload form */
/* ... */
// Add your existing booking-related JavaScript logic here
// ...
document.getElementById('image-upload-form').addEventListener('submit', function(event) {
    event.preventDefault();
    const formData = new FormData(this);

    fetch('/images/upload', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        console.log('Image uploaded successfully');
        // Update the UI to show the uploaded image
    })
    .catch(error => {
        console.error('Error uploading image:', error);
    });
});



self.addEventListener('push', function(event) {
    // Notification display logic...
});

// Register service worker and request permission for notifications
if ('serviceWorker' in navigator && 'PushManager' in window) {
    // Service worker registration and push subscription logic...
}
// Connect to WebSocket
var stompClient = null;

function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/public', function(chatMessage) {
            // Handle incoming messages
        });
    });
}

// Send message
function sendMessage() {
    stompClient.send("/app/sendMessage", {}, JSON.stringify({/* message content */}));
}

// Disconnect
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

// Call connect when the page loads
connect();


// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic here
});

// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Sign-up logic here
});


// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic here
});


// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Sign-up logic here
});


// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Sign-up logic here
});


// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic here
});

// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Sign-up logic here
});

// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic to interact with the backend API
});

// signup.js with frontend validation and error handling
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    var username = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var termsCheckbox = document.getElementById('terms-checkbox');

    if (!termsCheckbox.checked) {
        alert('You must agree to the terms and conditions.');
        return;
    }

    // Proceed with form submission (e.g., AJAX request to backend)
});


// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic here
});

// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Sign-up logic here
});

// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic to interact with the backend API
});


// signup.js with frontend validation and error handling
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    var username = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var termsCheckbox = document.getElementById('terms-checkbox');

    if (!termsCheckbox.checked) {
        alert('You must agree to the terms and conditions.');
        return;
    }

    // Proceed with form submission (e.g., AJAX request to backend)
});


// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic here
});

// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Sign-up logic here
});


// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic to interact with the backend API
});

// signup.js with frontend validation and error handling
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    var username = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var termsCheckbox = document.getElementById('terms-checkbox');

    if (!termsCheckbox.checked) {
        alert('You must agree to the terms and conditions.');
        return;
    }

    // Proceed with form submission (e.g., AJAX request to backend)
});

// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic here
});

// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Sign-up logic here
});

// signup.js with frontend validation and error handling
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    var username = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var termsCheckbox = document.getElementById('terms-checkbox');

    if (!termsCheckbox.checked) {
        alert('You must agree to the terms and conditions.');
        return;
    }

    // Proceed with form submission (e.g., AJAX request to backend)
});


// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic here
});
// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Sign-up logic here
});

// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic to interact with the backend API
});

// signup.js with frontend validation and error handling
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    var username = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var termsCheckbox = document.getElementById('terms-checkbox');

    if (!termsCheckbox.checked) {
        alert('You must agree to the terms and conditions.');
        return;
    }

    // Proceed with form submission (e.g., AJAX request to backend)
});


// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic here
});
// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Sign-up logic here
});

// signup.js with frontend validation and error handling
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    var username = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var termsCheckbox = document.getElementById('terms-checkbox');

    if (!termsCheckbox.checked) {
        alert('You must agree to the terms and conditions.');
        return;
    }

    // Proceed with form submission (e.g., AJAX request to backend)
});


// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic here
});

// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Sign-up logic here
});

// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic to interact with the backend API
});

// signup.js with frontend validation and error handling
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    var username = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var termsCheckbox = document.getElementById('terms-checkbox');

    if (!termsCheckbox.checked) {
        alert('You must agree to the terms and conditions.');
        return;
    }

    // Proceed with form submission (e.g., AJAX request to backend)
});

// CalendarDatePicker.jsx (React component)

import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

const CalendarDatePicker = ({ selectedDate, handleDateChange }) => {
  return (
    <DatePicker
      selected={selectedDate}
      onChange={handleDateChange}
      minDate={new Date()} // Prevent selecting past dates
      selectsRange // Allow selecting date range
      startDate={selectedDate[0]}
      endDate={selectedDate[1]}
      inline // Display inline within the booking form
    />
  );
};

export default CalendarDatePicker;
// BookingForm.jsx (React component)

import React, { useState } from 'react';
import CalendarDatePicker from './CalendarDatePicker'; // Import your custom component

const BookingForm = () => {
  const [selectedDates, setSelectedDates] = useState([null, null]);

  const handleDateChange = (dates) => {
    setSelectedDates(dates);
    // Update booking details or trigger availability checks
  };

  return (
    <div>
      <h2>Book Your Stay</h2>
      <CalendarDatePicker
        selectedDate={selectedDates}
        handleDateChange={handleDateChange}
      />
      {/* Other form fields (guest count, special requests, etc.) */}
    </div>
  );
};

export default BookingForm;

// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic here
});

// signup.js
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Sign-up logic here
});

// app.js
document.addEventListener('DOMContentLoaded', function() {
    // JavaScript logic to interact with the backend API
});


// signup.js with frontend validation and error handling
document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault();
    var username = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var termsCheckbox = document.getElementById('terms-checkbox');

    if (!termsCheckbox.checked) {
        alert('You must agree to the terms and conditions.');
        return;
    }

    // Proceed with form submission (e.g., AJAX request to backend)
});


<!-- Include Stripe.js in your HTML -->
<script src="https://js.stripe.com/v3/"></script>
// PaymentForm.js (React component)
import React, { useState } from 'react';
import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';

const PaymentForm = () => {
    const stripe = useStripe();
    const elements = useElements();
    const [error, setError] = useState(null);

    const handleSubmit = async (event) => {
        event.preventDefault();
        // Handle payment submission using Stripe API
    };

    return (
        <form onSubmit={handleSubmit}>
            <CardElement />
            <button type="submit">Pay Now</button>
            {error && <div>{error.message}</div>}
        </form>
    );
};

const express = require('express');
const app = express();
const server = require('http').createServer(app);
const io = require('socket.io')(server);
const mongoose = require('mongoose');
const { User, Property, Booking, Chat } = require('./models'); // Import your models

app.use(express.json());

// Connect to MongoDB
mongoose.connect('mongodb://localhost:27017/yourDatabase', { useNewUrlParser: true, useUnifiedTopology: true });

// Middleware to hide host details
app.use('/properties', (req, res, next) => {
  if (req.method === 'GET') {
    req.hideHostDetails = true;
  }
  next();
});

// Property listing endpoint
app.get('/properties', async (req, res) => {
  const properties = await Property.find().select('-host.contact');
  res.json(properties);
});

// Booking endpoint
app.post('/bookings', async (req, res) => {
  const { propertyId, userId } = req.body;
  const booking = new Booking({ propertyId, userId });
  await booking.save();

  // After booking, host details should be visible to the user
  const property = await Property.findById(propertyId).populate('host');
  res.json({ booking, hostDetails: property.host });
});

// Real-time chat setup
io.on('connection', (socket) => {
  socket.on('joinChat', ({ chatId, userId }) => {
    socket.join(chatId);
    // Fetch chat history if exists
    const chat = await Chat.findOne({ chatId, userId });
    socket.emit('chatHistory', chat.messages);
  });

  socket.on('sendMessage', ({ chatId, message }) => {
    // Save message to chat history
    const chat = await Chat.findOne({ chatId });
    chat.messages.push(message);
    await chat.save();
    io.to(chatId).emit('newMessage', message);
  });

  // After booking duration, clear chat history
  socket.on('endChat', ({ chatId }) => {
    await Chat.findOneAndUpdate({ chatId }, { messages: [] });
  });
});

server.listen(3000, () => {
  console.log('Server running on port 3000');
});
import React, { useState, useEffect } from 'react';
import io from 'socket.io-client';
import axios from 'axios';

const App = () => {
  const [properties, setProperties] = useState([]);
  const [chat, setChat] = useState([]);
  const [socket, setSocket] = useState(null);

  useEffect(() => {
    // Fetch properties
    const fetchProperties = async () => {
      const response = await axios.get('/properties');
      setProperties(response.data);
    };
    fetchProperties();

    // Setup socket connection
    const newSocket = io(`http://localhost:3000`);
    setSocket(newSocket);
    return () => newSocket.close();
  }, [setSocket]);

  const handleBooking = async (propertyId) => {
    const response = await axios.post('/bookings', { propertyId, userId: 'currentUser' });
    // Join chat room with the host
    socket.emit('joinChat', { chatId: response.data.booking._id, userId: 'currentUser' });
  };

  const handleSendMessage = (chatId, message) => {
    socket.emit('sendMessage', { chatId, message });
  };

  const handleEndChat = (chatId) => {
    socket.emit('endChat', { chatId });
  };

  socket.on('chatHistory', (messages) => {
    setChat(messages);
  });

  socket.on('newMessage', (message) => {
    setChat([...chat, message]);
  });

  return (
    <div>
      {/* Render properties */}
      {/* Render chat interface */}
    </div>
  );
};

export default App;
// User.js
const userSchema = new mongoose.Schema({
  // ... define user schema
  contact: String,
  // ...
});

// Property.js
const propertySchema = new mongoose.Schema({
  // ... define property schema
  host: { type: mongoose.Schema.Types.ObjectId, ref: 'User' },
  // ...
});

// Booking.js
const bookingSchema = new mongoose.Schema({
  // ... define booking schema
  propertyId: { type: mongoose.Schema.Types.ObjectId, ref: 'Property' },
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User' },
  // ...
});

// Chat.js
const chatSchema = new mongoose.Schema({
  // ... define chat schema
  messages: [{ type: String }],
  // ...
});


// CalendarDatePicker.jsx (React component)

import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

const CalendarDatePicker = ({ selectedDate, handleDateChange }) => {
  return (
    <DatePicker
      selected={selectedDate}
      onChange={handleDateChange}
      minDate={new Date()} // Prevent selecting past dates
      selectsRange // Allow selecting date range
      startDate={selectedDate[0]}
      endDate={selectedDate[1]}
      inline // Display inline within the booking form
    />
  );
};

export default CalendarDatePicker;
// BookingForm.jsx (React component)

import React, { useState } from 'react';
import CalendarDatePicker from './CalendarDatePicker'; // Import your custom component

const BookingForm = () => {
  const [selectedDates, setSelectedDates] = useState([null, null]);

  const handleDateChange = (dates) => {
    setSelectedDates(dates);
    // Update booking details or trigger availability checks
  };

  return (
    <div>
      <h2>Book Your Stay</h2>
      <CalendarDatePicker
        selectedDate={selectedDates}
        handleDateChange={handleDateChange}
      />
      {/* Other form fields (guest count, special requests, etc.) */}
    </div>
  );
};


// app.js

document.getElementById('search-form').addEventListener('submit', function(event) {

    event.preventDefault();

    const location = document.getElementById('location').value;

    const date = document.getElementById('date').value;

    

    // Call the backend API to search properties

    fetch(`/search?location=${location}&date=${date}`)

        .then(response => response.json())

        .then(properties => {

            const propertyList = document.getElementById('property-list');

            propertyList.innerHTML = ''; // Clear the list

            properties.forEach(property => {

                const listItem = document.createElement('div');

                listItem.innerHTML = `

                    <h3>${property.name}</h3>

                    <p>${property.description}</p>

                    <p>Located in: ${property.location}</p>

                `;

                propertyList.appendChild(listItem);

            });

        })

        .catch(error => console.error('Error:', error));

});





// booking.js

document.getElementById('booking-form').addEventListener('submit', function(event) {

event.preventDefault();



// Input validation

const location = document.getElementById('location').value;

const checkInDate = document.getElementById('check-in-date').value;

const checkOutDate = document.getElementById('check-out-date').value;

const guests = document.getElementById('guests').value;



if (!location || !checkInDate || !checkOutDate || guests <= 0) {

displayError('Please fill in all fields correctly.');

return;

}



// Prepare data for submission

const bookingData = {

location,

checkIn: checkInDate,

checkOut: checkOutDate,

guests

};



// Call the backend API to book the property

fetch('/api/book', {

method: 'POST',

headers: {

'Content-Type': 'application/json',

},

body: JSON.stringify(bookingData),

})

.then(response => {

if (!response.ok) {

throw new Error('Network response was not ok');

}

return response.json();

})

.then(data => {

document.getElementById('booking-response').textContent = 'Booking successful! Confirmation number: ' + data.confirmationNumber;

})

.catch(error => {

displayError('Failed to book: ' + error.message);

});

});



function displayError(message) {

const responseDiv = document.getElementById('booking-response');

responseDiv.textContent = message;

responseDiv.style.backgroundColor = '#f8d

7da';

responseDiv.style.color = '#721c24';

}







// signup.js

document.getElementById('signup-form').addEventListener('submit', function(event) {

event.preventDefault();

const termsCheckbox = document.getElementById('terms-checkbox');



if (!termsCheckbox.checked) {

alert('You must agree to the terms and conditions to sign up.');

return;

}



// Proceed with the sign-up process

// ...

});



// notifications.js

function loadNotifications() {

fetch('/api/notifications')

.then(response => response.json())

.then(notifications => {

const notificationsContainer = document.getElementById('notifications');

notifications.forEach(notification => {

const notificationElement = document.createElement('div');

notificationElement.innerText = notification.message;

notificationsContainer.appendChild(notificationElement);

});

});

}



// chat.js

var stompClient = null;



function connect() {

var socket = new SockJS('/chat');

stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {

stompClient.subscribe('/topic/public', function(chatMessage) {

showMessage(JSON.parse(chatMessage.body).content);

});

});

}



function sendMessage() {

var messageContent = document.getElementById('messageInput').value.trim();

if(messageContent && stompClient) {

var chatMessage = {

content: messageContent,

// ... other message fields like sender

};

stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));

document.getElementById('messageInput').value = '';

}

}



function showMessage(message) {

// Logic to append the message to the chat view

}



//

Call connect when the page loads

connect();





// app.js

document.getElementById('search-form').addEventListener('submit', function(event) {

event.preventDefault();

const location = document.getElementById('location').value;

const date = document.getElementById('date').value;


// Call the backend API to search properties

fetch(`/search?location=${location}&date=${date}`)

.then(response => response.json())

.then(properties => {

const propertyList = document.getElementById('property-list');

propertyList.innerHTML = ''; // Clear the list

properties.forEach(property => {

const listItem = document.createElement('div');

listItem.innerHTML = `

<h3>${property.name}</h3>

<p>${property.description}</p>

<p>Located in: ${property.location}</p>

`;

propertyList.appendChild(listItem);

});

})

.catch(error => console.error('Error:', error));

});





// booking.js

document.getElementById('booking-form').addEventListener('submit', function(event) {

event.preventDefault();



// Input validation

const location = document.getElementById('location').value;

const checkInDate = document.getElementById('check-in-date').value;

const checkOutDate = document.getElementById('check-out-date').value;

const guests = document.getElementById('guests').value;



if (!location || !checkInDate || !checkOutDate || guests <= 0) {

displayError('Please fill in all fields correctly.');

return;

}



// Prepare data for submission

const bookingData = {

location,

checkIn: checkInDate,

checkOut: checkOutDate,

guests

};



// Call the backend API to book the property

fetch('/api/book', {

method: 'POST',

headers: {

'Content-Type': 'application/json',

},

body: JSON.stringify(bookingData),

})

.then(response => {

if (!response.ok) {

throw new Error('Network response was not ok');

}

return response.json();

})

.then(data => {

document.getElementById('booking-response').textContent = 'Booking successful! Confirmation number: ' + data.confirmationNumber;

})

.catch(error => {

displayError('Failed to book: ' + error.message);

});

});



function displayError(message) {

const responseDiv = document.getElementById('booking-response');

responseDiv.textContent = message;

responseDiv.style.backgroundColor = '#f8d7da';

responseDiv.style.color = '#721c24';

}



// notifications.js

function loadNotifications() {

fetch('/api/notifications')

.then(response => response.json())

.then(notifications => {

const notificationsContainer = document.getElementById('notifications');

notifications.forEach(notification => {

const notificationElement = document.createElement('div');

notificationElement.innerText = notification.message;

notificationsContainer.appendChild(notificationElement);

});

});

}



// chat.js

var stompClient = null;



function connect() {

var socket = new SockJS('/chat');

stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {

stompClient.subscribe('/topic/public', function(chatMessage) {

showMessage(JSON.parse(chatMessage.body).content);

});

});

}



function sendMessage() {

var messageContent = document.getElementById('messageInput').value.trim();

if(messageContent && stompClient) {

var chatMessage = {

content: messageContent,

// ... other message fields like sender

};

stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));

document.getElementById('messageInput').value = '';

}

}



function showMessage(message) {

// Logic to append the message to the chat view

}



//

Call connect when the page loads

connect();



// signup.js

document.getElementById('signup-form').addEventListener('submit', function(event) {

event.preventDefault();

const termsCheckbox = document.getElementById('terms-checkbox');



if (!termsCheckbox.checked) {

alert('You must agree to the terms and conditions to sign up.');

return;

}



// Proceed with the sign-up process


// ...

});



// service-worker.js

self.addEventListener('push', function(event) {

const data = event.data.json();

const { title, body } = data.notification;



const options = {

body: body,

// Other options like icons, actions, etc.

};



event.waitUntil(

self.registration.showNotification(title, options)

);

});



// Register service worker and request permission for notifications

if ('serviceWorker' in navigator && 'PushManager' in window) {

navigator.serviceWorker.register('/service-worker.js

.then(function(swReg) {

console.log('Service Worker is registered', swReg);



swReg.pushManager.getSubscription()

.then(function(subscription) {

if (subscription === null) {

// Ask for permission and subscribe for push notifications

Notification.requestPermission().then(permission => {

if (permission === "granted") {

swReg.pushManager.subscribe({

userVisibleOnly: true,

applicationServerKey: 'YOUR_VAPID_PUBLIC_KEY'

}).then(function(subscription) {

// Send subscription object to the server

});

}

});

}

});

})

.catch(function(error) {

console.error('Service Worker Error', error);

});

} else {

console.warn('Push messaging is not supported');

}



// service-worker.js

self.addEventListener('push', function(event) {

const data = event.data.json();

const { title, body } = data.notification;



const options = {

body: body,

// Other options like icons, actions, etc.

};



event.waitUntil(

self.registration.showNotification(title, options)

);

});



// Register service worker and request permission for notifications

if ('serviceWorker' in navigator && 'PushManager' in window) {

navigator.serviceWorker.register('/service-worker.js

.then(function(swReg) {

console.log('Service Worker is registered', swReg);



swReg.pushManager.getSubscription()

.then(function(subscription) {

if (subscription === null) {

// Ask for permission and subscribe for push notifications

Notification.requestPermission().then(permission => {

if (permission === "granted") {

swReg.pushManager.subscribe({

userVisibleOnly: true,

applicationServerKey: 'YOUR_VAPID_PUBLIC_KEY'

}).then(function(subscription) {

// Send subscription object to the server

});

}

});

}

});

})

.catch(function(error) {

console.error('Service Worker Error', error);

});

} else {

console.warn('Push messaging is not supported');

}





// Example in Node.js for a referral system



// User model with a referralCode and referredBy properties

class User {

constructor(name, email) {

this.name = name;

this.email = email;

this.referralCode = generateReferralCode();

this.referredBy = null;

}

}



// Function to generate a unique referral code

function generateReferralCode() {

// Implementation to generate a unique code

}



// Function to handle a new referral

function addReferral(referrerEmail, referredEmail) {

const referrer = findUserByEmail(referrerEmail);

const referred = new User('New User', referredEmail);

referred.referredBy = referrer.referralCode;



// Add logic to reward the referrer

rewardReferrer(referrer);



// Save the new user to the database

saveUser(referred);

}



// Function to reward the referrer

function rewardReferrer(user) {

// Implementation to add rewards to the referrer's account

}



// Function to find a user by email

function findUserByEmail(email) {

// Implementation to find and return a user from the database

}



// Function to save a new user to the database

function saveUser(user) {

// Implementation to save the user to the database

}

// Existing User model in your codebase

class ExistingUserModel {

constructor(name, email, /* other properties */) {

this.name = name;

this.email = email;

// Initialize other properties

// ...

// Add referral properties

this.referralCode = generateReferralCode();

this.referredBy = null;

}



// Existing methods

// ...



//

New method to generate a referral code

generateReferralCode() {

// Your implementation here

}



// Other referral-related methods

// ...

}



// Assuming you have a User model with a referralCode and referredBy properties

app.post('/refer', async (req, res) => {

const { referralCode } = req.body;

try {

// Find the user who was referred

const referredUser = await User.findOne({ referralCode: referralCode });

if (!referredUser) {

return res.status(404).send('Referral code not found.');

}

// Update the referrer's reward points

const referrer = await User.findById(referredUser.referredBy);

referrer.rewardPoints += 10; // Assuming each referral gives 10 points

await referrer.save();

res.send('Referral successful and rewards updated.');

} catch (error) {

res.status(500).send(error.mess

age);

}

});





// Assuming you have a User model with a referralCode and referredBy properties

app.post('/refer', async (req, res) => {

const { referralCode } = req.body;

try {

// Find the user who was referred

const referredUser = await User.findOne({ referralCode: referralCode });

if (!referredUser) {

return res.status(404).send('Referral code not found.');

}

// Update the referrer's reward points

const referrer = await User.findById(referredUser.referredBy);

referrer.rewardPoints += 10; // Assuming each referral gives 10 points

await referrer.save();

res.send('Referral successful and rewards updated.');

} catch (error) {

res.status(500).send(error.message);

}

});





// image-upload.js

document.getElementById('image-upload-form').addEventListener('submit', function(event) {

event.preventDefault();

const formData = new FormData(this);



fetch('/images/upload', {

method: 'POST',

body: formData

})

.then(response => response.json())

.then(data => {

console.log('Image uploaded successfully');

// Update the UI to show the uploaded image

})

.catch(error => {

console.error('Error uploading image:', error);

});

});





// image-upload.js

document.getElementById('image-upload-form').addEventListener('submit', function(event) {

event.preventDefault();

const formData = new FormData(this);



fetch('/images/upload', {

method: 'POST',

body: formData

})

.then(response => response.json())

.then(data => {

console.log('Image uploaded successfully');

// Update the UI to show the uploaded image

})

.catch(error => {

console.error('Error uploading image:', error);

});

});



// signup.js

document.getElementById('signup-form').addEventListener('submit', function(event) {

// Sign-up logic with terms and conditions validation

});



// Sign-Up Page (signup.html)

<form id="signup-form">

<!-- Other sign-up fields -->

<label for="terms-checkbox">

<input type="checkbox" id="terms-checkbox" name="terms" required>

I agree to the <a href="terms-and-conditions.html" target="_blank">Terms and Conditions</a>

</label>

<button type="submit">Sign Up</button>

</form>



// Push Notifications (service-worker.js)

self.addEventListener('push', function(event) {

// Notification display logic...

});



// Register service worker and request permission for notifications

if ('serviceWorker' in navigator && 'PushManager' in window) {

// Service worker registration and push subscription logic...

}



// Add your existing booking-related JavaScript logic here

// ...

document.getElementById('image-upload-form').addEventListener('submit', function(event) {

event.preventDefault();

const formData = new FormData(this);



fetch('/images/upload', {

method: 'POST',

body: formData

})

.then(response => response.json())

.then(data => {

console.log('Image uploaded successfully');

// Update the UI to show the uploaded image

})

.catch(error => {

console.error('Error uploading image:', error);

});

});



// Notification display logic...

});



// Register service worker and request permission for notifications

if ('serviceWorker' in navigator && 'PushManager' in window) {

// Service worker registration and push subscription logic...

}

// Connect to WebSocket

var stompClient = null;



function connect() {

var socket = new SockJS('/chat');

stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {

stompClient.subscribe('/topic/public', function(chatMessage) {

// Handle incoming messages

});

});

}



// Send message

function sendMessage() {

stompClient.send("/app/sendMessage", {}, JSON.stringify({/* message content */}));

}



// Disconnect

function disconnect() {

if (stompClient !== null) {

stompClient.disconnect();

}

console.log("Disconnected");

}



// Call connect when the page loads

connect();





// signup.js

document.getElementById('signup-form').addEventListener('submit', function(event) {

event.preventDefault();

// Sign-up logic here

});

// app.js

document.addEventListener('DOMContentLoaded', function() {

// JavaScript logic here

});

document.addEventListener('DOMContentLoaded', function() {

// Fetch user-specific data (e.g., notifications, bookings) and update UI

// Handle user interactions (e.g., form submissions, button clicks)

});

// signup.js with frontend validation and error handling

document.getElementById('signup-form').addEventListener('submit', function(event) {

event.preventDefault();

var username = document.getElementById('username').value;

var email = document.getElementById('email').value;

var password = document.getElementById('password').value;

var termsCheckbox = document.getElementById('terms-checkbox');



if (!termsCheckbox.checked) {

alert('You must agree to the terms and conditions.');

return;

}



// Proceed with form submission (e.g., AJAX request to backend)

});

// app.js

document.addEventListener('DOMContentLoaded', function() {

// JavaScript logic to interact with the backend API

});



// CalendarDatePicker.jsx (React component)



import React, { useState } from 'react';

import DatePicker from 'react-datepicker';

import 'react-datepicker/dist/react-datepicker.css';



const CalendarDatePicker = ({ selectedDate, handleDateChange }) => {

return (

<DatePicker

selected={selectedDate}

onChange={handleDateChange}

minDate={new Date()} // Prevent selecting past dates

selectsRange // Allow selecting date range

startDate={selectedDate[0]}

endDate={selectedDate[1]}

inline // Display inline within the booking form

/>

);

};



export default CalendarDatePicker;

// BookingForm.jsx (React component)



import React, { useState } from 'react';

import CalendarDatePicker from './CalendarDatePicker'; // Import your custom component



const BookingForm = () => {

const [selectedDates, setSelectedDates] = useState([null, null]);



const handleDateChange = (dates) => {

setSelectedDates(dates);

// Update booking details or trigger availability checks

};



return (

<div>

<h2>Book Your Stay</h2>

<CalendarDatePicker

selectedDate={selectedDates}

handleDateChange={handleDateChange}

/>

{/* Other form fields (guest count, special requests, etc.) */}

</div>

);

};

<!-- Include Stripe.js in your HTML -->

<script src="https://js.stripe.com/v3/"></script>

// PaymentForm.js (React component)

import React, { useState } from 'react';

import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';



const PaymentForm = () => {

const stripe = useStripe();

const elements = useElements();

const [error, setError] = useState(null);



const handleSubmit = async (event) => {

event.preventDefault();

// Handle payment submission using Stripe API

};



return (

<form onSubmit={handleSubmit}>

<CardElement />

<button type="submit">Pay Now</button>

{error && <div>{error.message}</div>}

</form>

);

};



const express = require('express');

const app = express();

const server = require('http').createServer(app);

const io = require('socket.io')(server);

const mongoose = require('mongoose');

const { User, Property, Booking, Chat } = require('./models'); // Import your models



app.use(express.json());



// Connect to MongoDB

mongoose.connect('mongodb://localhost:27017/yourDatabase', { useNewUrlParser: true, useUnifiedTopology: true });



// Middleware to hide host details

app.use('/properties', (req, res, next) => {

if (req.method === 'GET') {

req.hideHostDetails = true;

}

next();

});



// Property listing endpoint

app.get('/properties', async (req, res) => {

const properties = await Property.find().select('-host.contact');

res.json(properties);

});



// Booking endpoint

app.post('/bookings', async (req, res) => {

const { propertyId, userId } = req.body;

const booking = new Booking({ propertyId, userId });

await booking.save();



// After booking, host details should be visible to the user

const property = await Property.findById(propertyId).populate('host');

res.json({ booking, hostDetails: property.host });

});



// Real-time chat setup

io.on('connection', (socket) => {

socket.on('joinChat', ({ chatId, userId }) => {

socket.join(chatId);

// Fetch chat history if exists

const chat = await Chat.findOne({ chatId, userId });

socket.emit('chatHistory', chat.messages);

});



socket.on('sendMessage', ({ chatId, message }) => {

// Save message to chat history

const chat = await Chat.findOne({ chatId });

chat.messages.push(message);

await chat.save();

io.to(chatId).emit('newMessage', message);

});



// After booking duration, clear chat history

socket.on('endChat', ({ chatId }) => {

await Chat.findOneAndUpdate({ chatId }, { messages: [] });

});

});



server.listen(3000, () => {

console.log('Server running on port 3000');

});

import React, { useState, useEffect } from 'react';

import io from 'socket.io-client';

import axios from 'axios';



const App = () => {

const [properties, setProperties] = useState([]);

const [chat, setChat] = useState([]);

const [socket, setSocket] = useState(null);



useEffect(() => {

// Fetch properties

const fetchProperties = async () => {

const response = await axios.get('/properties');

setProperties(response.data);

};

fetchProperties();



// Setup socket connection

const newSocket = io(`http://localhost:3000`);

setSocket(newSocket);

return () => newSocket.close();

}, [setSocket]);



const handleBooking = async (propertyId) => {

const response = await axios.post('/bookings', { propertyId, userId: 'currentUser' });

// Join chat room with the host

socket.emit('joinChat', { chatId: response.data.booking._id, userId: 'currentUser' });

};



const handleSendMessage = (chatId, message) => {

socket.emit('sendMessage', { chatId, message });

};



const handleEndChat = (chatId) => {

socket.emit('endChat', { chatId });

};



socket.on('chatHistory', (messages) => {

setChat(messages);

});



socket.on('newMessage', (message) => {

setChat([...chat, message]);

});



return (

<div>

{/* Render properties */}

{/* Render chat interface */}

</div>

);

};



export default App;

// User.js

const userSchema = new mongoose.Schema({

// ... define user schema

contact: String,

// ...

});



// Property.js

const propertySchema = new mongoose.Schema({

// ... define property schema

host: { type: mongoose.Schema.Types.ObjectId, ref: 'User' },

// ...

});



// Booking.js

const bookingSchema = new mongoose.Schema({

// ... define booking schema

propertyId: { type: mongoose.Schema.Type

s.ObjectId, ref: 'Property' },

userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User' },

// ...

});



// Chat.js

const chatSchema = new mongoose.Schema({

// ... define chat schema

messages: [{ type: String }],

// ...

});

// app.js

document.addEventListener('DOMContentLoaded', function() {

    // JavaScript logic here

});

// signup.js

document.getElementById('signup-form').addEventListener('submit', function(event) {

    event.preventDefault();

    // Sign-up logic here

});

document.addEventListener('DOMContentLoaded', function() {

    // Fetch user-specific data (e.g., notifications, bookings) and update UI

    // Handle user interactions (e.g., form submissions, button clicks)

});

document.addEventListener('DOMContentLoaded', function() {

    // Fetch user-specific data (e.g., notifications, bookings) and update UI

    // Handle user interactions (e.g., form submissions, button clicks)

});

// app.js

document.addEventListener('DOMContentLoaded', function() {

    // JavaScript logic to interact with the backend API

});

// signup.js with frontend validation and error handling

document.getElementById('signup-form').addEventListener('submit', function(event) {

    event.preventDefault();

    var username = document.getElementById('username').value;

    var email = document.getElementById('email').value;

    var password = document.getElementById('password').value;

    var termsCheckbox = document.getElementById('terms-checkbox');


    if (!termsCheckbox.checked) {

        alert('You must agree to the terms and conditions.');

        return;

    }

// Proceed with form submission (e.g., AJAX request to backend)

});


// CalendarDatePicker.jsx (React component)


import React, { useState } from 'react';

import DatePicker from 'react-datepicker';

import 'react-datepicker/dist/react-datepicker.css';


const CalendarDatePicker = ({ selectedDate, handleDateChange }) => {

  return (

    <DatePicker

      selected={selectedDate}

      onChange={handleDateChange}

      minDate={new Date()} // Prevent selecting past dates

      selectsRange // Allow selecting date range

      startDate={selectedDate[0]}

      endDate={selectedDate[1]}

      inline // Display inline within the booking form

    />

  );

};


export default CalendarDatePicker;

// BookingForm.jsx (React component)


import React, { useState } from 'react';

import CalendarDatePicker from './CalendarDatePicker'; // Import your custom component


const BookingForm = () => {

  const [selectedDates, setSelectedDates] = useState([null, null]);


  const handleDateChange = (dates) => {

    setSelectedDates(dates);

    // Update booking details or trigger availability checks

  };


  return (

    <div>

      <h2>Book Your Stay</h2>

      <CalendarDatePicker

        selectedDate={selectedDates}

        handleDateChange={handleDateChange}

      />

      {/* Other form fields (guest count, special requests, etc.) */}

    </div>

  );

};

<!-- Include Stripe.js in your HTML -->

<script src="https://js.stripe.com/v3/"></script>

// PaymentForm.js (React component)

import React, { useState } from 'react';

import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';


const PaymentForm = () => {

    const stripe = useStripe();

    const elements = useElements();

    const [error, setError] = useState(null);


    const handleSubmit = async (event) => {

        event.preventDefault();

        // Handle payment submission using Stripe API

    };


    return (

        <form onSubmit={handleSubmit}>

            <CardElement />

            <button type="submit">Pay Now</button>

            {error && <div>{error.message}</div>}

        </form>

    );

};



// SmartPricing.js

const { Property } = require('../models/Property');


const calculateSmartPrice = async (propertyId) => {

  const property = await Property.findById(propertyId);

  const demandFactor = getDemandFactor(property.location);

  const smartPrice = property.basePrice * demandFactor;

  return smartPrice;

};


const getDemandFactor = (location) => {

  // Logic to determine demand based on location, season, etc.

  return 1.2; // Example factor

};

// Analytics.js

const { Booking } = require('../models/Booking');


const getPropertyPerformance = async (propertyId) => {

  const bookings = await Booking.find({ propertyId });

  const performanceData = analyzeBookings(bookings);

  return performanceData;

};


const analyzeBookings = (bookings) => {

  // Logic to analyze bookings and calculate performance metrics

  return { occupancyRate: 75, averageRating: 4.5 }; // Example metrics

};

// ReferralSystem.js

const { User } = require('../models/User');


app.post('/referral/redeem', async (req, res) => {

  const { referralCode } = req.body;

  const referredUser = await User.findOne({ referralCode });

  if (referredUser) {

    referredUser.referralPoints += 10; // Add points for successful referral

    await referredUser.save();

    res.send('Referral redeemed successfully.');

  } else {

    res.status(404).send('Invalid referral code.');

  }

});

// PropertyDashboard.js

import React from 'react';

import { useProperty } from '../hooks/useProperty';


const PropertyDashboard = () => {

  const { smartPrice, performanceData } = useProperty();


  return (

    <div>

      <h1>Smart Price: {smartPrice}</h1>

      <h2>Occupancy Rate: {performanceData.occupancyRate}%</h2>

      <h2>Average Rating: {performanceData.averageRating}</h2>

      {/* Additional dashboard elements */}

    </div>

  );

};

// errorHandlingMiddleware.js

module.exports = function (err, req, res, next) {

  console.error(err.stack);

  res.status(500).json({ message: 'Internal Server Error', error: err });

};

// authenticationMiddleware.js

const jwt = require('jsonwebtoken');


module.exports = function (req, res, next) {

  const token = req.header('Authorization').replace('Bearer ', '');

  if (!token) return res.status(401).send('Access denied. No token provided.');


  try {

    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    req.user = decoded;

    next();

  } catch (ex) {

    res.status(400).send('Invalid token.');

  }

};

// User.js

const mongoose = require('mongoose');

const bcrypt = require('bcryptjs');


const userSchema = new mongoose.Schema({

  username: { type: String, required: true, unique: true },

  password: { type: String, required: true },

  // ... other fields

});


userSchema.pre('save', async function (next) {

  if (this.isModified('password')) {

    this.password = await bcrypt.hash(this.password, 8);

  }

  next();

});


const User = mongoose.model('User', userSch





const User = mongoose.model('User', userSchema);

module.exports = User;

// useAuth.js

import { useState } from 'react';

import axios from 'axios';



export const useAuth = () => {

const [user, setUser] = useState(null);



const login = async (username, password) => {

const response = await axios.post('/api/login', { username, password });

setUser(response.data);

};



const logout = () => {

setUser(null);

};



return { user, login, logout };

};

// AuthService.js

import axios from 'axios';



export const login = async (username, password) => {

const response = await axios.post('/api/login', { username, password });

return response.data;

};



export const logout = async () => {

// Perform logout operations

};



// React component structure for the main application layout

const MainApp = () => {

return (

<div className="app-container">

{/* Top bar with logo, profile update, and search */}

<TopBar>

<Logo />

<ProfileUpdateButton />

<SearchBar />

</TopBar>



{/* Row for uploading property images */}

<ImageUploadRow />



{/* Main content area */}

<MainContent>

{/* Conditional rendering based on user role (host/client) */}

<ExploreHomePage />

{/* ... other main content components */}

</MainContent>



{/* Footer with navigation elements */}

<Footer>

<Navigation>

<ExploreButton />

<AuthenticationButton />

<MessagingButton />

<NotificationsButton />

{/* ... other navigation buttons */}

</Navigation>

</Footer>

</div>

);

};



// Components for each part of the layout

const TopBar = ({ children }) => { /* ... */ };

const Logo = () => { /* ... */ };

const ProfileUpdateButton = () => { /* ... */ };

const SearchBar = () => { /* ... */ };

const ImageUploadRow = () => { /* ... */ };

const MainContent = ({ children }) => { /* ... */ };

const ExploreHomePage = () => { /* ... */ };

const Footer = ({ children }) => { /* ... */ };

const Navigation = ({ children }) => { /* ... */ };

const ExploreButton = () => { /* ... */ };

const AuthenticationButton = () => { /* ... */ };

const MessagingButton = () => { /* ... */ };

const NotificationsButton = () => { /* ... */ };

// Additional components for user-related functionalities

const UserProfileManagementButton = () => { /* ... */ };

const ReviewSectionButton = () => { /* ... */ };

const AboutButton = () => { /* ... */ };

const CurrencyButton = () => { /* ... */ };

const SignInOutButton = () => { /* ... */ };



// Updated Footer component with additional navigation elements

const Footer = ({ children }) => {

return (

<div className="footer-container">

{/* ... other navigation buttons */}

<UserProfileManagementButton />

<ReviewSectionButton />

<AboutButton />

<CurrencyButton />

<SignInOutButton />

</div>

);

};



// Updated MainApp component structure

const MainApp = () => {

// ... rest of the MainApp component

return (

<div className="app-container">

{/* ... other components */}

<Footer>

<Navigation>

{/* ... other navigation buttons */}

<UserProfileManagementButton />

<ReviewSectionButton />

<AboutButton />

<CurrencyButton />

<SignInOutButton />

</Navigation>

</Footer>

</div>

);

};

// User Authentication

const express = require('express');

const bcrypt = require('bcrypt');

const jwt = require('jsonwebtoken');

const User = require('./models/User'); // Assuming you have a User model



const app = express();

app.use(express.json());



// Register endpoint

app.post('/register', async (req, res) => {

try {

const hashedPassword = await bcrypt.hash(req.body.password, 10);

const user = new User({

username: req.body.username,

email: req.body.email,

password: hashedPassword,

});

const newUser = await user.save();

res.status(201).json(newUser);

} catch (error) {

res.status(500).json({ message: 'Error registering new user.' });

}

});



// Login endpoint

app.post('/login', async (req, res) => {

// ... login logic using bcrypt and jwt

});



// Data Management

const fetchData = async (endpoint) => {

try {

const response = await fetch(endpoint);

if (!response.ok) throw new Error('Network response was not ok.');

return await response.json();

} catch (error) {

console.error('There has been a problem with your fetch operation:', error);

}

};



// Responsive Design

// This would be handled in your CSS with media queries



// Error Handling

app.use((err, req, res, next) => {

console.error(err.stack);

res.status(500).send('Something broke!');

});



// Testing

// Example using Jest for a simple authentication test

const request = require('supertest');



describe('POST /login', () => {

test('responds with json', async () => {

const response = await request(app)

.post('/login')

.send({

username: 'testuser',

password: 'password123',

});

expect(response.statusCode).toBe(200);

expect(response.type).toBe('application/json');

});

});



// Start server

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => console.log(`Server running on port ${PORT}`));

// Backend: Express server setup with JWT authentication and MongoDB integration

const express = require('express');

const mongoose = require('mongoose');

const jwt = require('jsonwebtoken');

const bodyParser = require('body-parser');

const cors = require('cors');

const User = require('./models/User'); // User model

const Property = require('./models/Property'); // Property model

const authMiddleware = require('./middleware/authMiddleware'); // Middleware for JWT verification



const app = express();

app.use(cors());

app.use(bodyParser.json());



// Connect to MongoDB

mongoose.connect('mongodb://localhost/yourDatabase', { useNewUrlParser: true, useUnifiedTopology: true });



// User authentication routes

app.post('/register', /* ... */);

app.post('/login', /* ... */);



// Property management routes

app.get('/properties', authMiddleware, /* ... */);

app.post('/properties', authMiddleware, /* ... */);



// Start the server

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => console.log(`Server running on port ${PORT}`));



// Frontend: React application with Redux and React Router

import React from 'react';

import ReactDOM from 'react-dom';

import { Provider } from 'react-redux';

import { createStore, applyMiddleware } from 'redux';

import thunk from 'redux-thunk';

import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

import rootReducer from './reducers'; // Root reducer

import App from './components/App'; // Main App component

import Login from './components/Login'; // Login component

import Register from './components/Register'; // Register component

import Properties from './components/Properties'; // Properties component



const store = createStore(rootReducer, applyMiddleware(thunk));



ReactDOM.render(

<Provider store={store}>

<Router>

<Switch>

<Route path="/" exact component={App} />

<Route path="/login" component={L

ogin} />

<Route path="/register" component={Register} />

<Route path="/properties" component={Properties} />

{/* ... other routes */}

</Switch>

</Router>

</Provider>,

document.getElementById('root')

);



// app.js

document.addEventListener('DOMContentLoaded', function() {

// JavaScript logic here

});

// signup.js

document.getElementById('signup-form').addEventListener('submit', function(event) {

event.preventDefault();

// Sign-up logic here

});

document.addEventListener('DOMContentLoaded', function() {

// Fetch user-specific data (e.g., notifications, bookings) and update UI

// Handle user interactions (e.g., form submissions, button clicks)

});



document.addEventListener('DOMContentLoaded', function() {

// Fetch user-specific data (e.g., notifications, bookings) and update UI

// Handle user interactions (e.g., form submissions, button clicks)

});



// app.js

document.addEventListener('DOMContentLoaded', function() {

// JavaScript logic to interact with the backend API

});

// signup.js with frontend validation and error handling

document.getElementById('signup-form').addEventListener('submit', function(event) {

event.preventDefault();

var username = document.getElementById('username').value;

var email = document.getElementById('email').value;

var password = document.getElementById('password').value;

var termsCheckbox = document.getElementById('terms-checkbox');



if (!termsCheckbox.checked) {

alert('You must agree to the terms and conditions.');

return;

}



// Proceed with form submission (e.g., AJAX request to backend)

});



// CalendarDatePicker.jsx (React component)



import React, { useState } from 'react';

import DatePicker from 'react-datepicker';

import 'react-datepicker/dist/react-datepicker.css';



const CalendarDatePicker = ({ selectedDate, handleDateChange }) => {

return (

<DatePicker

selected={selectedDate}

onChange={handleDateChange}

minDate={new Date()} // Prevent selecting past dates

selectsRange // Allow selecting date range

startDate={selectedDate[0]}

endDate={selectedDate[1]}

inline // Display inline within the booking form

/>

);

};



export default CalendarDatePicker;

// BookingForm.jsx (React component)



import React, { useState } from 'react';

import CalendarDatePicker from './CalendarDatePicker'; // Import your custom component



const BookingForm = () => {

const [selectedDates, setSelectedDates] = useState([null, null]);



const handleDateChange = (dates) => {

setSelectedDates(dates);

// Update booking details or trigger availability checks

};



return (

<div>

<h2>Book Your Stay</h2>

<CalendarDatePicker

selectedDate={selectedDates}

handleDateChange={handleDateChange}

/>

{/* Other form fields (guest count, special requests, etc.) */}

</div>

);

};



<!-- Include Stripe.js in your HTML -->

<script src="https://js.stripe.com/v3/"></script>

// PaymentForm.js (React component)

import React, { useState } from 'react';

import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';



const PaymentForm = () => {

const stripe = useStripe();

const elements = useElements();

const [error, setError] = useState(null);



const handleSubmit = async (event) => {

event.preventDefault();

// Handle payment submission using Stripe API

};



return (

<form onSubmit={handleSubmit}>

<CardElement />

<button ty

pe="submit">Pay Now</button>

{error && <div>{error.message}</div>}

</form>

);

};





// SmartPricing.js

const { Property } = require('../models/Property');



const calculateSmartPrice = async (propertyId) => {

const property = await Property.findById(propertyId);

const demandFactor = getDemandFactor(property.location);

const smartPrice = property.basePrice * demandFactor;

return smartPrice;

};



const getDemandFactor = (location) => {

// Logic to determine demand based on location, season, etc.

return 1.2; // Example factor

};

// Analytics.js

const { Booking } = require('../models/Booking');



const getPropertyPerformance = async (propertyId) => {

const bookings = await Booking.find({ propertyId });

const performanceData = analyzeBookings(bookings);

return performanceData;

};



const analyzeBookings = (bookings) => {

// Logic to analyze bookings and calculate performance metrics

return { occupancyRate: 75, averageRating: 4.5 }; // Example metrics

};

// ReferralSystem.js

const { User } = require('../models/User');



app.post('/referral/redeem', async (req, res) => {

const { referralCode } = req.body;

const referredUser = await User.findOne({ referralCode });

if (referredUser) {

referredUser.referralPoints += 10; // Add points for successful referral

await referredUser.save();

res.send('Referral redeemed successfully.');

} else {

res.status(404).send('Invalid referral code.');

}

});

// PropertyDashboard.js

import React from 'react';

import { useProperty } from '../hooks/useProperty';



const PropertyDashboard = () => {

const { smartPrice, performanceData } = useProperty();



return (

<div>

<h1>Smart Price: {smartPrice}</h1>

<h2>Occupancy Rate: {performanceData.occupancyRate}%</h2>

<h2>Average Rating: {performanceData.averageRating}</h2>

{/* Additional dashboard elements */}

</div>

);

};

// errorHandlingMiddleware.js

module.exports = function (err, req, res, next) {

console.error(err.stack);

res.status(500).json({ message: 'Internal Server Error', error: err });

};

// authenticationMiddleware.js

const jwt = require('jsonwebtoken');



module.exports = function (req, res, next) {

const token = req.header('Authorization').replace('Bearer ', '');

if (!token) return res.status(401).send('Access denied. No token provided.');



try {

const decoded = jwt.verify(token, process.env.JWT_SECRET);

req.user = decoded;

next();

} catch (ex) {

res.status(400).send('Invalid token.');

}

};

// User.js

const mongoose = require('mongoose');

const bcrypt = require('bcryptjs');



const userSchema = new mongoose.Schema({

username: { type: String, required: true, unique: true },

password: { type: String, required: true },

// ... other fields

});



userSchema.pre('save', async function (next) {

if (this.isModified('password')) {

this.password = await bcrypt.hash(this.password, 8);

}

next();

});



const User = mongoose.model('User', userSchema);

module.exports = User;

// useAuth.js

import { useState } from 'react';

import axios from 'axios';



export const useAuth = () => {

const [user, setUser] = useState(null);



const login = async (username, password) => {

const response = await axios.post('/api/login', { username, password });

setUser(response.data);

};



const logout = () => {

setUser(null);

};



return { user, login, logout };

};

// AuthService.js

import axios from 'axios';



export const login = async (username, password) => {

const response = await axios.post('/api/login', { username, password });

return response.data;

};



export const logout = async () => {

// Perform logout operations

};



// React component structure for the main application layout

const MainApp = () => {

return (

<div className="app-container">

{/* Top bar with logo, profile update, and search */}

<TopBar>

<Logo />

<ProfileUpdateButton />

<SearchBar />

</TopBar>



{/* Row for uploading property images */}

<ImageUploadRow />



{/* Main content area */}

<MainContent>

{/* Conditional rendering based on user role (host/client) */}

<ExploreHomePage />

{/* ... other main content components */}

</MainContent>



{/* Footer with navigation elements */}

<Footer>

<Navigation>

<ExploreButton />

<AuthenticationButton />

<MessagingButton />

<NotificationsButton />

{/* ... other navigation buttons */}

</Navigation>

</Footer>

</div>

);

};



// Components for each part of the layout

const TopBar = ({ children }) => { /* ... */ };

const Logo = () => { /* ... */ };

const ProfileUpdateButton = () => { /* ... */ };

const SearchBar = () => { /* ... */ };

const ImageUploadRow = () => { /* ... */ };

const MainContent = ({ children }) => { /* ... */ };

const ExploreHomePage = () => { /* ... */ };

const Footer = ({ children }) => { /* ... */ };

const Navigation = ({ children }) => { /* ... */ };

const ExploreButton = () => { /* ... */ };

const AuthenticationButton = () => { /* ... */ };

const MessagingButton = () => { /* ... */ };

const NotificationsButton = () => { /* ... */ };

// Additional components for user-related functionalities

const UserProfileManagementButton = () => { /* ... */ };

const ReviewSectionButton = () => { /* ... */ };

const AboutButton = () => { /* ... */ };

const CurrencyButton = () => { /* ... */ };

const SignInOutButton = () => { /* ... */ };



// Updated Footer component with additional navigation elements

const Footer = ({ children }) => {

return (

<div className="footer-container">

{/* ... other navigation buttons */}

<UserProfileManagementButton />

<ReviewSectionButton />

<AboutButton />

<CurrencyButton />

<SignInOutButton />

</div>

);

};



// Updated MainApp component structure

const MainApp = () => {

// ... rest of the MainApp component

return (

<div className="app-container">

{/* ... other components */}

<Footer>

<Navigation>

{/* ... other navigation buttons */}

<UserProfileManagementButton />

<ReviewSectionButton />

<AboutButton />

<CurrencyButton />

<SignInOutButton />

</Navigation>

</Footer>

</div>

);

};

// User Authentication

const express = require('express');

const bcrypt = require('bcrypt');

const jwt = require('jsonwebtoken');

const User = require('./models/User'); // Assuming you have a User model



const app = express();

app.use(express.json());



// Register endpoint

app.post('/register', async (req, res) => {

try {

const hashedPassword = await bcrypt.hash(req.body.password, 10);

const user = new User({

username: req.body.username,

email: req.body.email,

password: hashedPassword,

});

const newUser = await user.save();

res.status(201).json(newUser);

} catch (error) {

res.status(500).json({ message: 'Error registering new user.' });

}

});



// Login endpoint

app.post('/login', async (req, res) => {

// ... login logic using bcrypt and jwt

});



// Data Management

const fetchData = async (endpoint) => {

try {

const response = await fetch(endpoint);

if (!response.ok) throw new Error('Network response was not ok.');

return await response.json();

} catch (error) {

console.error('There has been a problem with your fetch operation:', error);

}

};



// Responsive Design

// This would be handled in your CSS with media queries



// Error Handling

app.use((err, req, res, next) => {

console.error(err.stack);

res.status(500).send('Something broke!');

});



// Testing

// Example using Jest for a simple authentication test

const request = require('supertest');



describe('POST /login', () => {

test('responds with json', async () => {

const response = await request(app)

.post('/login')

.send({

username: 'testuser',

password: 'password123',

});

expect(response.statusCode).toBe(200);

expect(response.type).toBe('application/json');

});

});



// Start server

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => console.log(`Server running on port ${PORT}`));

// Backend: Express server setup with JWT authentication and MongoDB integration

const express = require('express');

const mongoose = require('mongoose');

const jwt = require('jsonwebtoken');

const bodyParser = require('body-parser');

const cors = require('cors');

const User = require('./models/User'); // User model

const Property = require('./models/Property'); // Property model

const authMiddleware = require('./middleware/authMiddleware'); // Middleware for JWT verification



const app = express();

app.use(cors());

app.use(bodyParser.json());



// Connect to MongoDB

mongoose.connect('mongodb://localhost/yourDatabase', { useNewUrlParser: true, useUnifiedTopology: true });



// User authentication routes

app.post('/register', /* ... */);

app.post('/login', /* ... */);



// Property management routes

app.get('/properties', authMiddleware, /* ... */);

app.post('/properties', authMiddleware, /* ... */);



// Start the server

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => console.log(`Server running on port ${PORT}`));



// Frontend: React application with Redux and React Router

import React from 'react';

import ReactDOM from 'react-dom';

import { Provider } from 'react-redux';

import { createStore, applyMiddleware } from 'redux';

import thunk from 'redux-thunk';

import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

import rootReducer from './reducers'; // Root reducer

import App from './components/App'; // Main App component

import Login from './components/Login'; // Login component

import Register from './components/Register'; // Register component

import Properties from './components/Properties'; // Properties component



const store = createStore(rootReducer, applyMiddleware(thunk));



ReactDOM.render(

<Provider store={store}>

<Router>

<Switch

>

<Route path="/" exact component={App} />

<Route path="/login" component={Login} />

<Route path="/register" component={Register} />

<Route path="/properties" component={Properties} />

{/* ... other routes */}

</Switch>

</Router>

</Provider>,

document.getElementById('root')

);



// Backend: Node.js with Express, MongoDB, and external API integrations



// Express server setup

const express = require('express');

const mongoose = require('mongoose');

const jwt = require('jsonwebtoken');

const cors = require('cors');

const i18n = require('i18n'); // Internationalization library

const searchAlgorithm = require('./searchAlgorithm'); // Custom search algorithm

const dynamicPricing = require('./dynamicPricing'); // Dynamic pricing model

const complianceCheck = require('./complianceCheck'); // Compliance check module



const app = express();

app.use(cors());

app.use(express.json());

app.use(i18n.init); // Initialize internationalization



// Connect to MongoDB

mongoose.connect('mongodb://localhost/yourDatabase', { useNewUrlParser: true, useUnifiedTopology: true });



// User authentication routes

app.post('/register', /* ... */);

app.post('/login', /* ... */);



// Property management routes with search filters

app.get('/properties', searchAlgorithm, /* ... */);



// Booking routes with dynamic pricing

app.post('/bookings', dynamicPricing, /* ... */);



// Middleware to check for regulatory compliance

app.use(complianceCheck);



// Start the server

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => console.log(`Server running on port ${PORT}`));



// Frontend: React application with state management and routing



import React from 'react';

import ReactDOM from 'react-dom';

import { Provider } from 'react-redux';

import { createStore, applyMiddleware } from 'redux';

import thunk from 'redux-thunk';

import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

import rootReducer from './reducers'; // Root reducer

import App from './components/App'; // Main App component

import PropertyList from './components/PropertyList'; // Property list component

import Booking from './components/Booking'; // Booking component

import LanguageSelector from './components/LanguageSelector'; // Language selector component



const store = createStore(rootReducer, applyMiddleware(thunk));



ReactDOM.render(

<Provider store={store}>

<Router>

<Switch>

<Route path="/" exact component={App} />

<Route path="/properties" component={PropertyList} />

<Route path="/booking" component={Booking} />

<Route path="/language" component={LanguageSelector} />

{/* ... other routes */}

</Switch>

</Router>

</Provider>,

document.getElementById('root')

);

// Frontend: React

// SearchComponent.jsx

import React, { useState } from 'react';

import axios from 'axios';



const SearchComponent = () => {

const [location, setLocation] = useState('');

const [checkIn, setCheckIn] = useState('');

const [checkOut, setCheckOut] = useState('');

const [amenities, setAmenities] = useState([]);

const [maxPriceRange, setMaxPriceRange] = useState('');



const handleSearch = async () => {

try {

const response = await axios.get('/api/properties/search', {

params: { location, checkIn, checkOut, amenities, maxPriceRange }

});

// Handle the search results

console.log(response.data);

} catch (error) {

console.error('Error fetching search results:', error);

}

};



return (

<div>

{/* Input fields for location, dates, amenities, price range */}

{/* ... */}

<button onClick={handleSearch}>Search</button>

</div>

);

};



export default SearchComponent;



// Frontend: React application



// Routing and state management

const App = () => (

<Router>

<Switch>

<Route path="/login" component={Login} />

<Route path="/register" component={Register} />

<Route path="/properties" component={PropertyList} />

<Route path="/properties/:id" component={PropertyDetail} />

<Route path="/bookings/new" component={BookingForm} />

<Route path="/user/profile" component={UserProfile} />

// ... other routes

</Switch>

</Router>

);



// Authentication components

const Login = () => { /* ... */ };

const Register = () => { /* ... */ };



// Property components

const PropertyList = () => { /* ... */ };

const PropertyDetail = () => { /* ... */ };



// Booking components

const BookingForm = () => { /* ... */ };



// User profile components

const UserProfile = () => { /* ... */ };



// Review components

const ReviewList = () => { /* ... */ };

const ReviewForm = () => { /* ... */ };



// Payment components

const PaymentProcessing = () => { /* ... */ };



// Real-time messaging components

const Messaging = () => { /* ... */ };



// Notification components

const Notifications = () => { /* ... */ };



// Account settings components

const AccountSettings = () => { /* ... */ };



// Start the application

ReactDOM.render(<App />, document.getElementById('root'));





// Frontend: React application



// Component to display trending content

const TrendingContent = () => {

const [content, setContent] = useState([]);



useEffect(() => {

// Fetch trending content from the backend

fetch('/api/content/trending')

.then(response => response.json())

.then(data => setContent(data));

}, []);



return (

<div>

<h2>Trending Properties</h2>

{content.map((item) => (

<ContentItem key={item.id} title={item.title} description={item.description} />

))}

</div>

);

};



// Component to represent a single content item

const ContentItem = ({ title, description }) => (

<div>

<h3>{title}</h3>

<p>{description}</p>

{/* Add buttons for like, share, comment */}

</div>

);



// Start the application

ReactDOM.render(<TrendingContent />, document.getElementById('root'));

// Backend: Spring Boot application

// Frontend: React application



// Component to display content based on the distribution algorithm

const ContentFeed = () => {

// ... state and effects to fetch and display content

};



// Start the application

ReactDOM.render(<ContentFeed />, document.getElementById('root'));





<!-- Add buttons for Google and Facebook OAuth on your sign-up and login pages -->

<button id="google-login-btn">Login with Google</button>

<button id="facebook-login-btn">Login with Facebook</button>

// Use a library like 'react-oauth' to handle OAuth flow

import { useGoogleLogin, useFacebookLogin } from 'react-oauth';



// Google Login

const googleLogin = useGoogleLogin({

onSuccess: tokenResponse => console.log(tokenResponse),

onError: () => console.log('Google login failed'),

});



document.getElementById('google-login-btn').addEventListener('click', () => googleLogin());



// Facebook Login

const facebookLogin = useFacebookLogin({

onSuccess: tokenResponse => console.log(tokenResponse),

onError: () => console.log('Facebook login failed'),

});



document.getElementById('facebook-login-btn').addEventListener('click', () => facebookLogin());

<meta http-equiv="Content-Security-Policy" content="default-src 'self'; script-src 'self' https://apis.google.com https://connect.facebook.net; style-src 'self' 'unsafe-inline';">



// Frontend (React)



// Environment Configuration Setup

// File: .env

REACT_APP_SMS_SERVICE_BASE_URL=...

REACT_APP_OAUTH_SERVICE_BASE_URL=...



// SMS Service Client Setup

// File: SMSService.js

export const sendSMS = async (phoneNumber, message) => {

// Logic to call the SMS service API

};



// OAuth Service Client Setup

// File: OAuthService.js

export const authenticateUser = async (credentials) => {

// Logic to call the OAuth service API

};



// Database Client Setup (e.g., using an ORM like Sequelize)

// File: DatabaseClient.js

import { Sequelize } from 'sequelize';



const sequelize = new Sequelize('postgres://user:password@host:port/dbname');



// Cloud Hosting Client Setup (e.g., Google Cloud Platform client library)

// File: CloudHostingClient.js

import { Storage } from '@google-cloud/storage';



const storage = new Storage({ projectId: 'your-project-id' });



// App Component Setup with Routing and State Management

// File: App.js

import React from 'react';

import { BrowserRouter as Router, Route } from 'react-router-dom';

import { Login, Register, PropertyList, PropertyDetail } from './components';



const App = () => {

return (

<Router>

<Route path="/login" component={Login} />

<Route path="/register" component={Register} />

<Route path="/properties" component={PropertyList} />

<Route path="/property/:id" component={PropertyDetail} />

// Other routes and components

</Router>

);

};



ReactDOM.render(<App />, document.getElementById('root'));

// SMS Service Integration (e.g., UniStack, Fonoster, Amazon Connect, Atomic SMS Sender, Globfone)

@Service

public class SMSService {

// Method to send SMS using different providers based on configuration or preference

public void sendSMS(String phoneNumber, String message) {

// Logic to integrate with multiple SMS sending APIs

}

}



// OAuth Service Integration (e.g., Authentik, a12n-server, Casdoor, Glewlwyd)

@Configuration

public class OAuthServiceConfig {

// Configuration for multiple OAuth services

}



// Database Configuration (e.g., PostgreSQL, MariaDB, SQLite, MySQL)

@Configuration

public class DatabaseConfig {

// Configuration for connecting to multiple types of databases

}



// Cloud Hosting Configuration (e.g., Google Cloud Platform, AWS, Heroku)

@Configuration

public class CloudHostingConfig {

// Configuration for deploying to multiple cloud hosting platforms

}

// Environment Configuration Setup

// File: .env

REACT_APP_SMS_SERVICE_BASE_URL=...

REACT_APP_OAUTH_SERVICE_BASE_URL=...



// SMS Service Client Setup

// File: SMSService.js

export const sendSMS = async (phoneNumber, message) => {

// Logic to call the SMS service API

};



// OAuth Service Client Setup

// File: OAuthService.js

export const authenticateUser = async (credentials) => {

// Logic to call the OAuth service API

};



// App Component Setup with Routing and State Management

// File: App.js

import React from 'react';

import { BrowserRouter as Router, Route } from 'react-router-dom';

import { Login, Register, PropertyList, PropertyDetail } from './components';



const App = () => {

return (

<Router>

<Route path="/login" component={Login} />


<Route path="/register" component={Register} />

<Route path="/properties" component={PropertyList} />

<Route path="/property/:id" component={PropertyDetail} />

// Other routes and components

</Router>

);

};



ReactDOM.render(<App />, document.getElementById('root'));





// Updated JavaScript for handling the sign-up/sign-in functionality

document.getElementById('google-signin-btn').addEventListener('click', function() {

  // Updated code to handle Google sign-in

});



document.getElementById('facebook-signin-btn').addEventListener('click', function() {

  // Updated code to handle Facebook sign-in

});



// ... other updated scripts for sign-up/sign-in functionality ...

function SearchComponent({ onSearch }) {

    const [searchCriteria, setSearchCriteria] = useState({

        destination: '',

        startDate: null,

        endDate: null,

        // Other criteria

    });



    const handleSearch = () => {

        onSearch(searchCriteria);

    };



    return (

        <div>

            {/* Input fields for search criteria */}

            <button onClick={handleSearch}>Search</button>

        </div>

    );

}



// In signup.js

document.getElementById('signup-form').addEventListener('submit', function(event) {

    // Frontend validation logic

});



// script.js

document.getElementById('send-otp').addEventListener('click', function() {

  const phoneNumber = document.getElementById('phone-number').value;

  // Call backend to send OTP

});



document.getElementById('verify-otp').addEventListener('click', function() {

  const otp = document.getElementById('otp').value;

  // Call backend to verify OTP

});

// server.js

const express = require('express');

const axios = require('axios');

const app = express();

const port = 3000;



app.use(express.json());



app.post('/send-otp', async (req, res) => {

  const phoneNumber = req.body.phoneNumber;

  try {

    const response = await axios.post('YOUR_SMS_GATEWAY_API_URL', {

      // Your SMS gateway's expected request body

      phone: phoneNumber,

      message: 'Your OTP is: @@otp@@'

    });

    res.status(200).send(response.data);

  } catch (error) {

    res.status(500).send(error);

  }

});



app.post('/verify-otp', async (req, res) => {

  const { phoneNumber, otp } = req.body;

  try {

    // Your logic to verify the OTP

    res.status(200).send({ verified: true });

  } catch (error) {

    res.status(500).send(error);

  }

});



app.listen(port, () => {

  console.log(`Server running on port ${port}`);

});




// script.js

document.getElementById('send-otp').addEventListener('click', function() {

  const phoneNumber = document.getElementById('phone-number').value;

  // Call backend to send OTP

});



document.getElementById('verify-otp').addEventListener('click', function() {

  const otp = document.getElementById('otp').value;

  // Call backend to verify OTP

});

// server.js

const express = require('express');

const auth0 = require('auth0');



const app = express();

const port = 3000;



// Initialize Auth0 client

const auth0Client = new auth0.AuthenticationClient({

  domain: 'YOUR_AUTH0_DOMAIN',

  clientId: 'YOUR_AUTH0_CLIENT_ID'

});



app.use(express.json());



app.post('/send-otp', (req, res) => {

  const phoneNumber = req.body.phoneNumber;

  // Use Auth0 to send OTP to the phone number

});



app.post('/verify-otp', (req, res) => {

  const otp = req.body.otp;

  // Use Auth0 to verify the OTP

});



app.listen(port, () => {

  console.log(`Server running on port ${port}`);

});

// script.js

document.getElementById('send-otp').addEventListener('click', function() {

  const phoneNumber = document.getElementById('phone-number').value;

  // Call backend to send OTP

});



document.getElementById('verify-otp').addEventListener('click', function() {

  const otp = document.getElementById('otp').value;

  // Call backend to verify OTP

});



// app.js

// TODO: Replace the following with your app's Firebase project configuration

const firebaseConfig = {

  apiKey: "YOUR_API_KEY",

  authDomain: "YOUR_AUTH_DOMAIN",

  projectId: "YOUR_PROJECT_ID",

  storageBucket: "YOUR_STORAGE_BUCKET",

  messagingSenderId: "YOUR_MESSAGING_SENDER_ID",

  appId: "YOUR_APP_ID"

};



// Initialize Firebase

const app = firebase.initializeApp(firebaseConfig);

const auth = firebase.auth();



document.getElementById('send-otp').addEventListener('click', function() {

  const phoneNumber = document.getElementById('phone-number').value;

  const appVerifier = window.recaptchaVerifier;

  auth.signInWithPhoneNumber(phoneNumber, appVerifier)

    .then((confirmationResult) => {

      // SMS sent. Prompt user to type the code from the message

      window.confirmationResult = confirmationResult;

    }).catch((error) => {

      // Error; SMS not sent

// Updated JavaScript for handling the sign-up/sign-in functionality

document.getElementById('google-signin-btn').addEventListener('click', function() {

  // Updated code to handle Google sign-in

});



document.getElementById('facebook-signin-btn').addEventListener('click', function() {

  // Updated code to handle Facebook sign-in

});

// ... other updated scripts for sign-up/sign-in functionality ...



// JavaScript for handling the sign-up/sign-in functionality

document.getElementById('google-signin-btn').addEventListener('click', function() {

  // Code to handle Google sign-in

});



document.getElementById('facebook-signin-btn').addEventListener('click', function() {

  // Code to handle Facebook sign-in

});



// ... other scripts for sign-up/sign-in functionality ...
const express = require('express');
const app = express();
const port = 3000;

const properties = [
    { id: 1, lat: -1.292066, lng: 36.821946, title: "Property 1", description: "Beautiful 3-bedroom house" },
    { id: 2, lat: -1.28333, lng: 36.81667, title: "Property 2", description: "Modern apartment in the city center" }
];

app.get('/api/properties', (req, res) => {
    res.json(properties);
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});
require('dotenv').config();
const express = require('express');
const app = express();
const port = 3000;

const properties = [
    { id: 1, lat: -1.292066, lng: 36.821946, title: "Property 1", description: "Beautiful 3-bedroom house" },
    { id: 2, lat: -1.28333, lng: 36.81667, title: "Property 2", description: "Modern apartment in the city center" }
];

app.get('/api/properties', (req, res) => {
    res.json(properties);
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});
const express = require('express');
const app = express();
const port = 3000;

const properties = [
    { id: 1, lat: -1.292066, lng: 36.821946, title: "Property 1", description: "Beautiful 3-bedroom house" },
    { id: 2, lat: -1.28333, lng: 36.81667, title: "Property 2", description: "Modern apartment in the city center" }
];

app.get('/api/properties', (req, res) => {
    res.json(properties);
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});
require('dotenv').config();
const express = require('express');
const app = express();
const port = 3000;

const properties = [
    { id: 1, lat: -1.292066, lng: 36.821946, title: "Property 1", description: "Beautiful 3-bedroom house" },
    { id: 2, lat: -1.28333, lng: 36.81667, title: "Property 2", description: "Modern apartment in the city center" }
];

app.get('/api/properties', (req, res) => {
    res.json(properties);
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});
const express = require('express');
const app = express();
const port = 3000;

const properties = [
    {
        id: 1,
        lat: -1.292066,
        lng: 36.821946,
        title: "Property 1",
        description: "Beautiful 3-bedroom house",
        image: "https://example.com/property1.jpg",
        availability: "Available",
        reviews: [
            { rating: 5, comment: "Amazing place!" },
            { rating: 4, comment: "Very comfortable." }
        ]
    },
    {
        id: 2,
        lat: -1.28333,
        lng: 36.81667,
        title: "Property 2",
        description: "Modern apartment in the city center",
        image: "https://example.com/property2.jpg",
        availability: "Booked",
        reviews: [
            { rating: 4, comment: "Great location." },
            { rating: 3, comment: "Good value for money." }
        ]
    }
];

app.get('/api/properties', (req, res) => {
    res.json(properties);
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});
const express = require('express');
const http = require('http');
const WebSocket = require('ws');
const bodyParser = require('body-parser');

const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

app.use(bodyParser.json());

let properties = [
    { id: 1, title: 'Property 1', description: 'Beautiful 3-bedroom house', booked: false },
    { id: 2, title: 'Property 2', description: 'Modern apartment in the city center', booked: false }
];

let clients = [];

wss.on('connection', (ws) => {
    clients.push(ws);
    ws.on('close', () => {
        clients = clients.filter(client => client !== ws);
    });
});

app.post('/api/book', (req, res) => {
    const { propertyId, userId } = req.body;
    const property = properties.find(p => p.id === propertyId);

    if (property && !property.booked) {
        property.booked = true;
        property.bookedBy = userId;
        property.bookedAt = new Date();

        // Notify all clients about the booking
        clients.forEach(client => {
            if (client.readyState === WebSocket.OPEN) {
                client.send(JSON.stringify({ type: 'BOOKED', property }));
            }
        });

        res.json({ success: true, property });
    } else {
        res.status(400).json({ success: false, message: 'Property already booked' });
    }
});

app.get('/api/properties', (req, res) => {
    res.json(properties);
});

server.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
const express = require('express');
const http = require('http');
const WebSocket = require('ws');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');

const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

app.use(bodyParser.json());

// Connect to MongoDB
mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    lat: Number,
    lng: Number,
    booked: Boolean,
    bookedBy: String,
    bookedAt: Date
});

const feedbackSchema = new mongoose.Schema({
    userId: String,
    propertyId: mongoose.Schema.Types.ObjectId,
    feedback: String,
    createdAt: { type: Date, default: Date.now }
});

const Property = mongoose.model('Property', propertySchema);
const Feedback = mongoose.model('Feedback', feedbackSchema);

let clients = [];

wss.on('connection', (ws) => {
    clients.push(ws);
    ws.on('close', () => {
        clients = clients.filter(client => client !== ws);
    });
});

app.post('/api/book', async (req, res) => {
    const { propertyId, userId } = req.body;
    const property = await Property.findById(propertyId);

    if (property && !property.booked) {
        property.booked = true;
        property.bookedBy = userId;
        property.bookedAt = new Date();
        await property.save();

        // Notify all clients about the booking
        clients.forEach(client => {
            if (client.readyState === WebSocket.OPEN) {
                client.send(JSON.stringify({ type: 'BOOKED', property }));
            }
        });

        res.json({ success: true, property });
    } else {
        res.status(400).json({ success: false, message: 'Property already booked' });
    }
});

app.post('/api/feedback', async (req, res) => {
    const { userId, propertyId, feedback } = req.body;
    const newFeedback = new Feedback({ userId, propertyId, feedback });
    await newFeedback.save();
    res.json({ success: true, feedback: newFeedback });
});

app.get('/api/properties', async (req, res) => {
    const properties = await Property.find();
    res.json(properties);
});

server.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    lat: Number,
    lng: Number,
    booked: Boolean,
    bookedBy: String,
    bookedAt: Date
});

const Property = mongoose.model('Property', propertySchema);

app.get('/api/properties', async (req, res) => {
    const properties = await Property.find();
    res.json(properties);
});

app.post('/api/book', async (req, res) => {
    const { propertyId, userId } = req.body;
    const property = await Property.findById(propertyId);

    if (property && !property.booked) {
        property.booked = true;
        property.bookedBy = userId;
        property.bookedAt = new Date();
        await property.save();
        res.json({ success: true, property });
    } else {
        res.status(400).json({ success: false, message: 'Property already booked' });
    }
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    lat: Number,
    lng: Number,
    booked: Boolean,
    bookedBy: String,
    bookedAt: Date,
    amenities: [String]
});

const Property = mongoose.model('Property', propertySchema);

app.get('/api/properties', async (req, res) => {
    const properties = await Property.find();
    res.json(properties);
});

app.post('/api/book', async (req, res) => {
    const { propertyId, userId } = req.body;
    const property = await Property.findById(propertyId);

    if (property && !property.booked) {
        property.booked = true;
        property.bookedBy = userId;
        property.bookedAt = new Date();
        await property.save();
        res.json({ success: true, property });
    } else {
        res.status(400).json({ success: false, message: 'Property already booked' });
    }
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const axios = require('axios');

const app = express();
app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/mobile_money', { useNewUrlParser: true, useUnifiedTopology: true });

const transactionSchema = new mongoose.Schema({
    userId: String,
    amount: Number,
    currency: String,
    status: String,
    createdAt: { type: Date, default: Date.now }
});

const Transaction = mongoose.model('Transaction', transactionSchema);

app.post('/api/pay', async (req, res) => {
    const { userId, amount, currency, paymentMethod } = req.body;

    // Example API call to M-PESA
    try {
        const response = await axios.post('https://api.safaricom.co.ke/mpesa/stkpush/v1/processrequest', {
            BusinessShortCode: 'YOUR_SHORTCODE',
            Password: 'YOUR_PASSWORD',
            Timestamp: 'YOUR_TIMESTAMP',
            TransactionType: 'CustomerPayBillOnline',
            Amount: amount,
            PartyA: 'USER_PHONE_NUMBER',
            PartyB: 'YOUR_SHORTCODE',
            PhoneNumber: 'USER_PHONE_NUMBER',
            CallBackURL: 'YOUR_CALLBACK_URL',
            AccountReference: 'ACCOUNT_REFERENCE',
            TransactionDesc: 'Payment Description'
        }, {
            headers: {
                'Authorization': 'Bearer YOUR_ACCESS_TOKEN'
            }
        });

        const transaction = new Transaction({ userId, amount, currency, status: 'Pending' });
        await transaction.save();

        res.json({ success: true, transaction });
    } catch (error) {
        res.status(500).json({ success: false, message: 'Payment failed', error: error.message });
    }
});

app.post('/api/callback', async (req, res) => {
    const { Body } = req.body;
    const { ResultCode, ResultDesc, CallbackMetadata } = Body.stkCallback;

    if (ResultCode === 0) {
        const transaction = await Transaction.findOneAndUpdate(
            { _id: CallbackMetadata.Item[1].Value },
            { status: 'Completed' },
            { new: true }
        );
        res.json({ success: true, transaction });
    } else {
        res.status(400).json({ success: false, message: ResultDesc });
    }
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const axios = require('axios');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');

const app = express();
app.use(bodyParser.json());
app.use(helmet()); // Security middleware
app.use(rateLimit({ windowMs: 15 * 60 * 1000, max: 100 })); // Rate limiting

mongoose.connect('mongodb://localhost:27017/mobile_money', { useNewUrlParser: true, useUnifiedTopology: true });

const transactionSchema = new mongoose.Schema({
    userId: String,
    amount: Number,
    currency: String,
    status: String,
    createdAt: { type: Date, default: Date.now }
});

const Transaction = mongoose.model('Transaction', transactionSchema);

app.post('/api/pay', async (req, res) => {
    const { userId, amount, currency, paymentMethod } = req.body;

    try {
        // Example API call to M-PESA
        const response = await axios.post('https://api.safaricom.co.ke/mpesa/stkpush/v1/processrequest', {
            BusinessShortCode: 'YOUR_SHORTCODE',
            Password: 'YOUR_PASSWORD',
            Timestamp: 'YOUR_TIMESTAMP',
            TransactionType: 'CustomerPayBillOnline',
            Amount: amount,
            PartyA: 'USER_PHONE_NUMBER',
            PartyB: 'YOUR_SHORTCODE',
            PhoneNumber: 'USER_PHONE_NUMBER',
            CallBackURL: 'YOUR_CALLBACK_URL',
            AccountReference: 'ACCOUNT_REFERENCE',
            TransactionDesc: 'Payment Description'
        }, {
            headers: {
                'Authorization': 'Bearer YOUR_ACCESS_TOKEN'
            }
        });

        const transaction = new Transaction({ userId, amount, currency, status: 'Pending' });
        await transaction.save();

        res.json({ success: true, transaction });
    } catch (error) {
        console.error('Payment error:', error);
        res.status(500).json({ success: false, message: 'Payment failed', error: error.message });
    }
});

app.post('/api/callback', async (req, res) => {
    const { Body } = req.body;
    const { ResultCode, ResultDesc, CallbackMetadata } = Body.stkCallback;

    if (ResultCode === 0) {
        const transaction = await Transaction.findOneAndUpdate(
            { _id: CallbackMetadata.Item[1].Value },
            { status: 'Completed' },
            { new: true }
        );
        res.json({ success: true, transaction });
    } else {
        console.error('Callback error:', ResultDesc);
        res.status(400).json({ success: false, message: ResultDesc });
    }
});

app.use((err, req, res, next) => {
    console.error('Server error:', err);
    res.status(500).json({ success: false, message: 'Internal server error' });
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
const axios = require('axios');
const { MongoMemoryServer } = require('mongodb-memory-server');
const mongoose = require('mongoose');
const request = require('supertest');
const app = require('../app'); // Assuming your Express app is exported from app.js

let mongoServer;

beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create();
    const uri = mongoServer.getUri();
    await mongoose.connect(uri, { useNewUrlParser: true, useUnifiedTopology: true });
});

afterAll(async () => {
    await mongoose.disconnect();
    await mongoServer.stop();
});

describe('Payment API', () => {
    it('should process payment successfully', async () => {
        jest.spyOn(axios, 'post').mockResolvedValue({ data: { success: true } });

        const response = await request(app)
            .post('/api/pay')
            .send({ userId: 'UserA', amount: 100, currency: 'KES', paymentMethod: 'mpesa' });

        expect(response.status).toBe(200);
        expect(response.body.success).toBe(true);
    });

    it('should handle payment failure', async () => {
        jest.spyOn(axios, 'post').mockRejectedValue(new Error('Payment failed'));

        const response = await request(app)
            .post('/api/pay')
            .send({ userId: 'UserA', amount: 100, currency: 'KES', paymentMethod: 'mpesa' });

        expect(response.status).toBe(500);
        expect(response.body.success).toBe(false);
    });
});
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const axios = require('axios');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');

const app = express();
app.use(bodyParser.json());
app.use(helmet()); // Security middleware
app.use(rateLimit({ windowMs: 15 * 60 * 1000, max: 100 })); // Rate limiting

mongoose.connect('mongodb://localhost:27017/mobile_money', { useNewUrlParser: true, useUnifiedTopology: true });

const transactionSchema = new mongoose.Schema({
    userId: String,
    amount: Number,
    currency: String,
    status: String,
    createdAt: { type: Date, default: Date.now }
});

const Transaction = mongoose.model('Transaction', transactionSchema);

app.post('/api/pay', async (req, res) => {
    const { userId, amount, currency, paymentMethod } = req.body;

    try {
        // Example API call to FNB eWallet
        const response = await axios.post('https://api.fnb.co.za/ewallet/send', {
            userId,
            amount,
            currency,
            paymentMethod
        }, {
            headers: {
                'Authorization': 'Bearer YOUR_ACCESS_TOKEN'
            }
        });

        const transaction = new Transaction({ userId, amount, currency, status: 'Pending' });
        await transaction.save();

        res.json({ success: true, transaction });
    } catch (error) {
        console.error('Payment error:', error);
        res.status(500).json({ success: false, message: 'Payment failed', error: error.message });
    }
});

app.post('/api/callback', async (req, res) => {
    const { Body } = req.body;
    const { ResultCode, ResultDesc, CallbackMetadata } = Body.stkCallback;

    if (ResultCode === 0) {
        const transaction = await Transaction.findOneAndUpdate(
            { _id: CallbackMetadata.Item[1].Value },
            { status: 'Completed' },
            { new: true }
        );
        res.json({ success: true, transaction });
    } else {
        console.error('Callback error:', ResultDesc);
        res.status(400).json({ success: false, message: ResultDesc });
    }
});

app.use((err, req, res, next) => {
    console.error('Server error:', err);
    res.status(500).json({ success: false, message: 'Internal server error' });
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');

const app = express();
app.use(bodyParser.json());
app.use(helmet()); // Security middleware
app.use(rateLimit({ windowMs: 15 * 60 * 1000, max: 100 })); // Rate limiting

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    lat: Number,
    lng: Number,
    price: Number,
    amenities: [String]
});

const Property = mongoose.model('Property', propertySchema);

app.get('/api/properties', async (req, res) => {
    const properties = await Property.find();
    res.json(properties);
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');

const app = express();
app.use(bodyParser.json());
app.use(helmet()); // Security middleware
app.use(rateLimit({ windowMs: 15 * 60 * 1000, max: 100 })); // Rate limiting

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    lat: Number,
    lng: Number,
    price: Number,
    amenities: [String]
});

const Property = mongoose.model('Property', propertySchema);

app.get('/api/properties', async (req, res) => {
    const properties = await Property.find();
    res.json(properties);
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');
const http = require('http');
const WebSocket = require('ws');

const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

app.use(bodyParser.json());
app.use(helmet()); // Security middleware
app.use(rateLimit({ windowMs: 15 * 60 * 1000, max: 100 })); // Rate limiting

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    lat: Number,
    lng: Number,
    price: Number,
    amenities: [String],
    hostId: String
});

const bookingSchema = new mongoose.Schema({
    userId: String,
    propertyId: String,
    status: String,
    createdAt: { type: Date, default: Date.now }
});

const hostSchema = new mongoose.Schema({
    hostId: String,
    online: Boolean
});

const Property = mongoose.model('Property', propertySchema);
const Booking = mongoose.model('Booking', bookingSchema);
const Host = mongoose.model('Host', hostSchema);

wss.on('connection', (ws) => {
    ws.on('message', (message) => {
        const data = JSON.parse(message);
        if (data.type === 'hostStatus') {
            Host.findOneAndUpdate({ hostId: data.hostId }, { online: data.online }, { upsert: true }, (err) => {
                if (err) console.error('Error updating host status:', err);
            });
        }
    });
});

app.post('/api/book', async (req, res) => {
    const { userId, propertyId } = req.body;
    const property = await Property.findById(propertyId);

    if (property) {
        const booking = new Booking({ userId, propertyId, status: 'Pending' });
        await booking.save();

        const host = await Host.findOne({ hostId: property.hostId });
        if (host && host.online) {
            wss.clients.forEach(client => {
                if (client.readyState === WebSocket.OPEN) {
                    client.send(JSON.stringify({ type: 'bookingNotification', propertyId, userId }));
                }
            });
        } else {
            // Send email or push notification to host
            console.log('Host is offline. Sending email or push notification...');
        }

        res.json({ success: true, booking });
    } else {
        res.status(400).json({ success: false, message: 'Property not found' });
    }
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
    server.listen(3001, () => {
        console.log('WebSocket server running on ws://localhost:3001');
    });
});
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');
const http = require('http');
const WebSocket = require('ws');
const nodemailer = require('nodemailer');

const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

app.use(bodyParser.json());
app.use(helmet()); // Security middleware
app.use(rateLimit({ windowMs: 15 * 60 * 1000, max: 100 })); // Rate limiting

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    lat: Number,
    lng: Number,
    price: Number,
    amenities: [String],
    hostId: String
});

const bookingSchema = new mongoose.Schema({
    userId: String,
    propertyId: String,
    status: String,
    createdAt: { type: Date, default: Date.now }
});

const userSchema = new mongoose.Schema({
    username: String,
    email: String,
    isHost: Boolean,
    online: Boolean
});

const Property = mongoose.model('Property', propertySchema);
const Booking = mongoose.model('Booking', bookingSchema);
const User = mongoose.model('User', userSchema);

const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'your-email@gmail.com',
        pass: 'your-email-password'
    }
});

app.post('/api/book', async (req, res) => {
    const { userId, propertyId } = req.body;
    const booking = new Booking({ userId, propertyId, status: 'Pending' });
    await booking.save();

    const property = await Property.findById(propertyId);
    const host = await User.findById(property.hostId);

    if (host.online) {
        wss.clients.forEach(client => {
            if (client.readyState === WebSocket.OPEN && client.userId === host._id.toString()) {
                client.send(JSON.stringify({ type: 'booking', message: `New booking request for ${property.title}` }));
            }
        });
    } else {
        const mailOptions = {
            from: 'your-email@gmail.com',
            to: host.email,
            subject: 'New Booking Request',
            text: `You have a new booking request for ${property.title}. Please log in to your account to respond.`
        };
        transporter.sendMail(mailOptions, (error, info) => {
            if (error) {
                console.error('Email error:', error);
            } else {
                console.log('Email sent:', info.response);
            }
        });
    }

    res.json({ success: true, booking });
});

wss.on('connection', (ws, req) => {
    const userId = req.url.split('?userId=')[1];
    ws.userId = userId;

    ws.on('message', message => {
        const data = JSON.parse(message);
        if (data.type === 'status') {
            User.findByIdAndUpdate(userId, { online: data.online }, { new: true }, (err, user) => {
                if (err) {
                    console.error('User status update error:', err);
                }
            });
        }
    });

    ws.on('close', () => {
        User.findByIdAndUpdate(userId, { online: false }, { new: true }, (err, user) => {
            if (err) {
                console.error('User status update error:', err);
            }
        });
    });
});

server.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, Button, FlatList, StyleSheet } from 'react-native';
import { WebSocket } from 'ws';

const Chat = ({ userId, hostId }) => {
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState('');
    const [ws, setWs] = useState(null);

    useEffect(() => {
        const socket = new WebSocket(`ws://localhost:3000?userId=${userId}`);
        setWs(socket);

        socket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            if (data.type === 'chat') {
                setMessages(prevMessages => [...prevMessages, data.message]);
            }
        };

        return () => {
            socket.close();
        };
    }, [userId]);

    const sendMessage = () => {
        if (ws && message.trim()) {
            const chatMessage = { type: 'chat', message: { userId, hostId, text: message, timestamp: new Date() } };
            ws.send(JSON.stringify(chatMessage));
            setMessages(prevMessages => [...prevMessages, chatMessage.message]);
            setMessage('');
        }
    };

    return (
        <View style={styles.container}>
            <FlatList
                data={messages}
                renderItem={({ item }) => (
                    <View style={styles.message}>
                        <Text>{item.text}</Text>
                        <Text style={styles.timestamp}>{new Date(item.timestamp).toLocaleTimeString()}</Text>
                    </View>
                )}
                keyExtractor={(item, index) => index.toString()}
            />
            <TextInput
                style={styles.input}
                value={message}
                onChangeText={setMessage}
                placeholder="Type a message"
            />
            <Button title="Send" onPress={sendMessage} />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 10
    },
    message: {
        padding: 10,
        borderBottomWidth: 1,
        borderBottomColor: '#ccc'
    },
    timestamp: {
        fontSize: 10,
        color: '#999'
    },
    input: {
        borderWidth: 1,
        borderColor: '#ccc',
        padding: 10,
        marginBottom: 10
    }
});

export default Chat;
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');
const http = require('http');
const WebSocket = require('ws');
const nodemailer = require('nodemailer');

const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

app.use(bodyParser.json());
app.use(helmet()); // Security middleware
app.use(rateLimit({ windowMs: 15 * 60 * 1000, max: 100 })); // Rate limiting

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    lat: Number,
    lng: Number,
    price: Number,
    amenities: [String],
    hostId: String
});

const userSchema = new mongoose.Schema({
    username: String,
    email: String,
    isHost: Boolean,
    online: Boolean
});

const Property = mongoose.model('Property', propertySchema);
const User = mongoose.model('User', userSchema);

const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'your-email@gmail.com',
        pass: 'your-email-password'
    }
});

app.post('/api/properties', async (req, res) => {
    const { title, description, lat, lng, price, amenities, hostId } = req.body;
    const property = new Property({ title, description, lat, lng, price, amenities, hostId });
    await property.save();

    notifyHost(hostId, `Your property "${title}" has been successfully uploaded.`);

    res.json({ success: true, property });
});

app.put('/api/properties/:id', async (req, res) => {
    const { id } = req.params;
    const { title, description, lat, lng, price, amenities, hostId } = req.body;
    const property = await Property.findByIdAndUpdate(id, { title, description, lat, lng, price, amenities, hostId }, { new: true });

    notifyHost(hostId, `Your property "${title}" has been successfully updated.`);

    res.json({ success: true, property });
});

function notifyHost(hostId, message) {
    User.findById(hostId, (err, host) => {
        if (err) {
            console.error('Host lookup error:', err);
            return;
        }

        if (host.online) {
            wss.clients.forEach(client => {
                if (client.readyState === WebSocket.OPEN && client.userId === host._id.toString()) {
                    client.send(JSON.stringify({ type: 'notification', message }));
                }
            });
        } else {
            const mailOptions = {
                from: 'your-email@gmail.com',
                to: host.email,
                subject: 'Property Update Notification',
                text: message
            };
            transporter.sendMail(mailOptions, (error, info) => {
                if (error) {
                    console.error('Email error:', error);
                } else {
                    console.log('Email sent:', info.response);
                }
            });
        }
    });
}

wss.on('connection', (ws, req) => {
    const userId = req.url.split('?userId=')[1];
    ws.userId = userId;

    ws.on('message', message => {
        const data = JSON.parse(message);
        if (data.type === 'status') {
            User.findByIdAndUpdate(userId, { online: data.online }, { new: true }, (err, user) => {
                if (err) {
                    console.error('User status update error:', err);
                }
            });
        }
    });

    ws.on('close', () => {
        User.findByIdAndUpdate(userId, { online: false }, { new: true }, (err, user) => {
            if (err) {
                console.error('User status update error:', err);
            }
        });
    });
});

server.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState, useEffect } from 'react';
import { View, Text, FlatList, StyleSheet } from 'react-native';
import { WebSocket } from 'ws';

const Notifications = ({ userId }) => {
    const [notifications, setNotifications] = useState([]);
    const [ws, setWs] = useState(null);

    useEffect(() => {
        const socket = new WebSocket(`ws://localhost:3000?userId=${userId}`);
        setWs(socket);

        socket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            if (data.type === 'notification') {
                setNotifications(prevNotifications => [...prevNotifications, data.message]);
            }
        };

        return () => {
            socket.close();
        };
    }, [userId]);

    return (
        <View style={styles.container}>
            <FlatList
                data={notifications}
                renderItem={({ item }) => (
                    <View style={styles.notification}>
                        <Text>{item}</Text>
                    </View>
                )}
                keyExtractor={(item, index) => index.toString()}
            />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 10
    },
    notification: {
        padding: 10,
        borderBottomWidth: 1,
        borderBottomColor: '#ccc'
    }
});

export default Notifications;
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Recommendations = ({ userId }) => {
    const [recommendations, setRecommendations] = useState([]);

    useEffect(() => {
        axios.post('http://localhost:5000/recommend', { user_id: userId })
            .then(response => setRecommendations(response.data))
            .catch(error => console.error('Error fetching recommendations:', error));
    }, [userId]);

    return (
        <div>
            <h3>Recommended Properties</h3>
            <ul>
                {recommendations.map(propertyId => (
                    <li key={propertyId}>Property ID: {propertyId}</li>
                ))}
            </ul>
        </div>
    );
};

export default Recommendations;
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    category: String,
    lat: Number,
    lng: Number,
    price: Number,
    amenities: [String],
    hostId: String
});

const Property = mongoose.model('Property', propertySchema);

app.get('/api/properties', async (req, res) => {
    const { category } = req.query;
    const properties = await Property.find(category ? { category } : {});
    res.json(properties);
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const PropertyCategories = () => {
    const [category, setCategory] = useState('');
    const [properties, setProperties] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:3000/api/properties', { params: { category } })
            .then(response => setProperties(response.data))
            .catch(error => console.error('Error fetching properties:', error));
    }, [category]);

    return (
        <div>
            <h3>Property Categories</h3>
            <select onChange={e => setCategory(e.target.value)}>
                <option value="">All</option>
                <option value="luxury">Luxury</option>
                <option value="eco">Eco-Friendly</option>
                <option value="digital_nomad">Digital Nomad</option>
                <option value="pet_friendly">Pet-Friendly</option>
            </select>
            <ul>
                {properties.map(property => (
                    <li key={property._id}>{property.title}</li>
                ))}
            </ul>
        </div>
    );
};

export default PropertyCategories;
const express = require('express');
const bodyParser = require('body-parser');
const { NlpManager } = require('node-nlp');

const app = express();
app.use(bodyParser.json());

const manager = new NlpManager({ languages: ['en'] });
// Train the chatbot with some basic intents
manager.addDocument('en', 'hello', 'greetings.hello');
manager.addDocument('en', 'book a property', 'booking.book');
manager.addAnswer('en', 'greetings.hello', 'Hello! How can I assist you today?');
manager.addAnswer('en', 'booking.book', 'Sure, I can help you book a property. Please provide the details.');

manager.train().then(() => {
    manager.save();
});

app.post('/api/chat', async (req, res) => {
    const { message } = req.body;
    const response = await manager.process('en', message);
    res.json({ reply: response.answer });
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState } from 'react';
import axios from 'axios';

const ChatSupport = () => {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');

    const sendMessage = () => {
        axios.post('http://localhost:3000/api/chat', { message: input })
            .then(response => {
                setMessages([...messages, { text: input, user: true }, { text: response.data.reply, user: false }]);
                setInput('');
            })
            .catch(error => console.error('Error sending message:', error));
    };

    return (
        <div>
            <h3>Chat Support</h3>
            <div>
                {messages.map((msg, index) => (
                    <div key={index} style={{ textAlign: msg.user ? 'right' : 'left' }}>
                        <p>{msg.text}</p>
                    </div>
                ))}
            </div>
            <input value={input} onChange={e => setInput(e.target.value)} />
            <button onClick={sendMessage}>Send</button>
        </div>
    );
};

export default ChatSupport;
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    exclusive: Boolean,
    lat: Number,
    lng: Number,
    price: Number,
    amenities: [String],
    hostId: String
});

const Property = mongoose.model('Property', propertySchema);

app.get('/api/exclusive-properties', async (req, res) => {
    const properties = await Property.find({ exclusive: true });
    res.json(properties);
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ExclusiveProperties = () => {
    const [properties, setProperties] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:3000/api/exclusive-properties')
            .then(response => setProperties(response.data))
            .catch(error => console.error('Error fetching properties:', error));
    }, []);

    return (
        <div>
            <h3>Exclusive Properties</h3>
            <ul>
                {properties.map(property => (
                    <li key={property._id}>{property.title}</li>
                ))}
            </ul>
        </div>
    );
};

export default ExclusiveProperties;
const express = require('express');
const bodyParser = require('body-parser');
const { NlpManager } = require('node-nlp');

const app = express();
app.use(bodyParser.json());

const manager = new NlpManager({ languages: ['en'] });
// Train the chatbot with some basic intents
manager.addDocument('en', 'hello', 'greetings.hello');
manager.addDocument('en', 'book a property', 'booking.book');
manager.addAnswer('en', 'greetings.hello', 'Hello! How can I assist you today?');
manager.addAnswer('en', 'booking.book', 'Sure, I can help you book a property. Please provide the details.');

manager.train().then(() => {
    manager.save();
});

app.post('/api/chat', async (req, res) => {
    const { message } = req.body;
    const response = await manager.process('en', message);
    res.json({ reply: response.answer });
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState } from 'react';
import axios from 'axios';

const ChatSupport = () => {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');

    const sendMessage = () => {
        axios.post('http://localhost:3000/api/chat', { message: input })
            .then(response => {
                setMessages([...messages, { text: input, user: true }, { text: response.data.reply, user: false }]);
                setInput('');
            })
            .catch(error => console.error('Error sending message:', error));
    };

    return (
        <div>
            <h3>Chat Support</h3>
            <div>
                {messages.map((msg, index) => (
                    <div key={index} style={{ textAlign: msg.user ? 'right' : 'left' }}>
                        <p>{msg.text}</p>
                    </div>
                ))}
            </div>
            <input value={input} onChange={e => setInput(e.target.value)} />
            <button onClick={sendMessage}>Send</button>
        </div>
    );
};

export default ChatSupport;
import React from 'react';
import { FacebookShareButton, TwitterShareButton, WhatsappShareButton, FacebookIcon, TwitterIcon, WhatsappIcon } from 'react-share';

const SocialMediaShare = ({ url, title }) => {
    return (
        <div>
            <h3>Share Your Booking</h3>
            <FacebookShareButton url={url} quote={title}>
                <FacebookIcon size={32} round />
            </FacebookShareButton>
            <TwitterShareButton url={url} title={title}>
                <TwitterIcon size={32} round />
            </TwitterShareButton>
            <WhatsappShareButton url={url} title={title}>
                <WhatsappIcon size={32} round />
            </WhatsappShareButton>
        </div>
    );
};

export default SocialMediaShare;
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const multer = require('multer');
const path = require('path');

const app = express();
app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const reviewSchema = new mongoose.Schema({
    propertyId: String,
    userId: String,
    rating: Number,
    comment: String,
    photos: [String],
    videos: [String]
});

const Review = mongoose.model('Review', reviewSchema);

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'uploads/');
    },
    filename: (req, file, cb) => {
        cb(null, `${Date.now()}-${file.originalname}`);
    }
});

const upload = multer({ storage });

app.post('/api/reviews', upload.fields([{ name: 'photos', maxCount: 5 }, { name: 'videos', maxCount: 2 }]), async (req, res) => {
    const { propertyId, userId, rating, comment } = req.body;
    const photos = req.files['photos'] ? req.files['photos'].map(file => file.path) : [];
    const videos = req.files['videos'] ? req.files['videos'].map(file => file.path) : [];
    const review = new Review({ propertyId, userId, rating, comment, photos, videos });
    await review.save();
    res.json({ success: true, review });
});

app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState } from 'react';
import axios from 'axios';

const SubmitReview = ({ propertyId, userId }) => {
    const [rating, setRating] = useState(0);
    const [comment, setComment] = useState('');
    const [photos, setPhotos] = useState([]);
    const [videos, setVideos] = useState([]);

    const handleFileChange = (e, setFiles) => {
        setFiles([...e.target.files]);
    };

    const submitReview = () => {
        const formData = new FormData();
        formData.append('propertyId', propertyId);
        formData.append('userId', userId);
        formData.append('rating', rating);
        formData.append('comment', comment);
        photos.forEach(photo => formData.append('photos', photo));
        videos.forEach(video => formData.append('videos', video));

        axios.post('http://localhost:3000/api/reviews', formData)
            .then(response => {
                console.log('Review submitted:', response.data);
            })
            .catch(error => console.error('Error submitting review:', error));
    };

    return (
        <div>
            <h3>Submit a Review</h3>
            <input type="number" value={rating} onChange={e => setRating(e.target.value)} placeholder="Rating" />
            <textarea value={comment} onChange={e => setComment(e.target.value)} placeholder="Comment"></textarea>
            <input type="file" multiple onChange={e => handleFileChange(e, setPhotos)} accept="image/*" />
            <input type="file" multiple onChange={e => handleFileChange(e, setVideos)} accept="video/*" />
            <button onClick={submitReview}>Submit</button>
        </div>
    );
};

export default SubmitReview;
const express = require('express');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());

const recommendations = {
    attractions: ['Museum', 'Park', 'Historic Site'],
    dining: ['Restaurant', 'Cafe', 'Bar'],
    transportation: ['Taxi', 'Bus', 'Bike Rental'],
    activities: ['Hiking', 'City Tour', 'Boat Ride']
};

app.get('/api/concierge', (req, res) => {
    const { type } = req.query;
    res.json(recommendations[type] || []);
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const VirtualConcierge = () => {
    const [type, setType] = useState('attractions');
    const [recommendations, setRecommendations] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:3000/api/concierge', { params: { type } })
            .then(response => setRecommendations(response.data))
            .catch(error => console.error('Error fetching recommendations:', error));
    }, [type]);

    return (
        <div>
            <h3>Virtual Concierge</h3>
            <select onChange={e => setType(e.target.value)}>
                <option value="attractions">Attractions</option>
                <option value="dining">Dining</option>
                <option value="transportation">Transportation</option>
                <option value="activities">Activities</option>
            </select>
            <ul>
                {recommendations.map((item, index) => (
                    <li key={index}>{item}</li>
                ))}
            </ul>
        </div>
    );
};

export default VirtualConcierge;
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    lat: Number,
    lng: Number,
    price: Number,
    amenities: [String],
    hostId: String,
    suitability: [String], // New field for suitability categories
    maxGuests: Number, // New field for maximum guests
    stayDurations: [String] // New field for stay durations
});

const Property = mongoose.model('Property', propertySchema);

app.post('/api/properties', async (req, res) => {
    const { title, description, lat, lng, price, amenities, hostId, suitability, maxGuests, stayDurations } = req.body;
    const property = new Property({ title, description, lat, lng, price, amenities, hostId, suitability, maxGuests, stayDurations });
    await property.save();
    res.json({ success: true, property });
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState } from 'react';
import axios from 'axios';

const PropertyListingForm = () => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [lat, setLat] = useState('');
    const [lng, setLng] = useState('');
    const [price, setPrice] = useState('');
    const [amenities, setAmenities] = useState([]);
    const [suitability, setSuitability] = useState([]);
    const [maxGuests, setMaxGuests] = useState('');
    const [stayDurations, setStayDurations] = useState([]);

    const handleSubmit = () => {
        const propertyData = { title, description, lat, lng, price, amenities, suitability, maxGuests, stayDurations };
        axios.post('http://localhost:3000/api/properties', propertyData)
            .then(response => {
                console.log('Property listed:', response.data);
            })
            .catch(error => console.error('Error listing property:', error));
    };

    return (
        <div>
            <h3>List Your Property</h3>
            <input type="text" value={title} onChange={e => setTitle(e.target.value)} placeholder="Title" />
            <textarea value={description} onChange={e => setDescription(e.target.value)} placeholder="Description"></textarea>
            <input type="text" value={lat} onChange={e => setLat(e.target.value)} placeholder="Latitude" />
            <input type="text" value={lng} onChange={e => setLng(e.target.value)} placeholder="Longitude" />
            <input type="number" value={price} onChange={e => setPrice(e.target.value)} placeholder="Price" />
            <input type="number" value={maxGuests} onChange={e => setMaxGuests(e.target.value)} placeholder="Max Guests" />
            <select multiple onChange={e => setAmenities([...e.target.selectedOptions].map(option => option.value))}>
                <option value="wifi">WiFi</option>
                <option value="parking">Parking</option>
                <option value="pool">Pool</option>
                <option value="gym">Gym</option>
            </select>
            <select multiple onChange={e => setSuitability([...e.target.selectedOptions].map(option => option.value))}>
                <option value="solo">Solo Travelers</option>
                <option value="couples">Couples</option>
                <option value="business">Business Travelers</option>
                <option value="families">Families</option>
            </select>
            <select multiple onChange={e => setStayDurations([...e.target.selectedOptions].map(option => option.value))}>
                <option value="1_day">1 Day</option>
                <option value="3_days">3 Days</option>
                <option value="1_week">1 Week</option>
                <option value="1_month">1 Month</option>
                <option value="semi_yearly">Semi-Yearly</option>
                <option value="yearly">Yearly</option>
            </select>
            <button onClick={handleSubmit}>Submit</button>
        </div>
    );
};

export default PropertyListingForm;
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const multer = require('multer');
const path = require('path');

const app = express();
app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const reviewSchema = new mongoose.Schema({
    propertyId: String,
    userId: String,
    rating: Number,
    comment: String,
    photos: [String],
    videos: [String]
});

const Review = mongoose.model('Review', reviewSchema);

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'uploads/');
    },
    filename: (req, file, cb) => {
        cb(null, `${Date.now()}-${file.originalname}`);
    }
});

const upload = multer({ storage });

app.post('/api/reviews', upload.fields([{ name: 'photos', maxCount: 5 }, { name: 'videos', maxCount: 2 }]), async (req, res) => {
    const { propertyId, userId, rating, comment } = req.body;
    const photos = req.files['photos'] ? req.files['photos'].map(file => file.path) : [];
    const videos = req.files['videos'] ? req.files['videos'].map(file => file.path) : [];
    const review = new Review({ propertyId, userId, rating, comment, photos, videos });
    await review.save();
    res.json({ success: true, review });
});

app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState } from 'react';
import axios from 'axios';

const SubmitReview = ({ propertyId, userId }) => {
    const [rating, setRating] = useState(0);
    const [comment, setComment] = useState('');
    const [photos, setPhotos] = useState([]);
    const [videos, setVideos] = useState([]);

    const handleFileChange = (e, setFiles) => {
        setFiles([...e.target.files]);
    };

    const submitReview = () => {
        const formData = new FormData();
        formData.append('propertyId', propertyId);
        formData.append('userId', userId);
        formData.append('rating', rating);
        formData.append('comment', comment);
        photos.forEach(photo => formData.append('photos', photo));
        videos.forEach(video => formData.append('videos', video));

        axios.post('http://localhost:3000/api/reviews', formData)
            .then(response => {
                console.log('Review submitted:', response.data);
            })
            .catch(error => console.error('Error submitting review:', error));
    };

    return (
        <div>
            <h3>Submit a Review</h3>
            <input type="number" value={rating} onChange={e => setRating(e.target.value)} placeholder="Rating" />
            <textarea value={comment} onChange={e => setComment(e.target.value)} placeholder="Comment"></textarea>
            <input type="file" multiple onChange={e => handleFileChange(e, setPhotos)} accept="image/*" />
            <input type="file" multiple onChange={e => handleFileChange(e, setVideos)} accept="video/*" />
            <button onClick={submitReview}>Submit</button>
        </div>
    );
};

export default SubmitReview;
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    lat: Number,
    lng: Number,
    price: Number,
    amenities: [String],
    hostId: String,
    suitability: [String],
    maxGuests: Number,
    stayDurations: [String]
});

const Property = mongoose.model('Property', propertySchema);

app.get('/api/properties', async (req, res) => {
    const { suitability, guestCount, stayDuration, amenities } = req.query;
    const query = {};

    if (suitability) query.suitability = suitability;
    if (guestCount) query.maxGuests = { $gte: guestCount };
    if (stayDuration) query.stayDurations = stayDuration;
    if (amenities) query.amenities = { $all: amenities.split(',') };

    const properties = await Property.find(query);
    res.json(properties);
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const PropertyFilters = () => {
    const [suitability, setSuitability] = useState('');
    const [guestCount, setGuestCount] = useState('');
    const [stayDuration, setStayDuration] = useState('');
    const [amenities, setAmenities] = useState([]);
    const [properties, setProperties] = useState([]);

    useEffect(() => {
        const fetchProperties = async () => {
            const response = await axios.get('http://localhost:3000/api/properties', {
                params: { suitability, guestCount, stayDuration, amenities: amenities.join(',') }
            });
            setProperties(response.data);
        };
        fetchProperties();
    }, [suitability, guestCount, stayDuration, amenities]);

    return (
        <div>
            <h3>Filter Properties</h3>
            <select onChange={e => setSuitability(e.target.value)}>
                <option value="">All</option>
                <option value="solo">Solo Travelers</option>
                <option value="couples">Couples</option>
                <option value="business">Business Travelers</option>
                <option value="families">Families</option>
            </select>
            <input type="number" value={guestCount} onChange={e => setGuestCount(e.target.value)} placeholder="Guest Count" />
            <select onChange={e => setStayDuration(e.target.value)}>
                <option value="">All</option>
                <option value="1_day">1 Day</option>
                <option value="3_days">3 Days</option>
                <option value="1_week">1 Week</option>
                <option value="1_month">1 Month</option>
                <option value="semi_yearly">Semi-Yearly</option>
                <option value="yearly">Yearly</option>
            </select>
            <select multiple onChange={e => setAmenities([...e.target.selectedOptions].map(option => option.value))}>
                <option value="wifi">WiFi</option>
                <option value="parking">Parking</option>
                <option value="pool">Pool</option>
                <option value="gym">Gym</option>
            </select>
            <ul>
                {properties.map(property => (
                    <li key={property._id}>{property.title}</li>
                ))}
            </ul>
        </div>
    );
};

export default PropertyFilters;
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const userSchema = new mongoose.Schema({
    username: String,
    email: String,
    searchHistory: [String]
});

const propertySchema = new mongoose.Schema({
    title: String,
    description: String,
    lat: Number,
    lng: Number,
    price: Number,
    amenities: [String],
    hostId: String,
    suitability: [String],
    maxGuests: Number,
    stayDurations: [String]
});

const User = mongoose.model('User', userSchema);
const Property = mongoose.model('Property', propertySchema);

app.post('/api/recommendations', async (req, res) => {
    const { userId } = req.body;
    const user = await User.findById(userId);
    const recommendations = await Property.find({ title: { $in: user.searchHistory } });
    res.json(recommendations);
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Recommendations = ({ userId }) => {
    const [recommendations, setRecommendations] = useState([]);

    useEffect(() => {
        const fetchRecommendations = async () => {
            const response = await axios.post('http://localhost:3000/api/recommendations', { userId });
            setRecommendations(response.data);
        };
        fetchRecommendations();
    }, [userId]);

    return (
        <div>
            <h3>Recommended Properties</h3>
            <ul>
                {recommendations.map(property => (
                    <li key={property._id}>{property.title}</li>
                ))}
            </ul>
        </div>
    );
};

export default Recommendations;
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const stripe = require('stripe')('your-stripe-secret-key');

const app = express();
app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const bookingSchema = new mongoose.Schema({
    userId: String,
    propertyId: String,
    status: String,
    createdAt: { type: Date, default: Date.now }
});

const Booking = mongoose.model('Booking', bookingSchema);

app.post('/api/book', async (req, res) => {
    const { userId, propertyId, token } = req.body;
    const charge = await stripe.charges.create({
        amount: 5000, // Amount in cents
        currency: 'usd',
        source: token,
        description: 'Property booking charge'
    });

    if (charge.status === 'succeeded') {
        const booking = new Booking({ userId, propertyId, status: 'Confirmed' });
        await booking.save();
        res.json({ success: true, booking });
    } else {
        res.status(500).json({ success: false, message: 'Payment failed' });
    }
});

app.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState } from 'react';
import axios from 'axios';
import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';

const BookingForm = ({ userId, propertyId }) => {
    const stripe = useStripe();
    const elements = useElements();
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const cardElement = elements.getElement(CardElement);
        const { token, error } = await stripe.createToken(cardElement);

        if (error) {
            setError(error.message);
        } else {
            const response = await axios.post('http://localhost:3000/api/book', {
                userId,
                propertyId,
                token: token.id
            });

            if (response.data.success) {
                setSuccess(true);
            } else {
                setError('Payment failed');
            }
        }
    };

    return (
        <div>
            <h3>Book Property</h3>
            <form onSubmit={handleSubmit}>
                <CardElement />
                <button type="submit" disabled={!stripe}>Pay</button>
            </form>
            {error && <div>{error}</div>}
            {success && <div>Booking confirmed!</div>}
        </div>
    );
};

export default BookingForm;
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const http = require('http');
const WebSocket = require('ws');

const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

app.use(bodyParser.json());

mongoose.connect('mongodb://localhost:27017/property_booking', { useNewUrlParser: true, useUnifiedTopology: true });

const messageSchema = new mongoose.Schema({
    senderId: String,
    receiverId: String,
    propertyId: String,
    content: String,
    timestamp: { type: Date, default: Date.now }
});

const Message = mongoose.model('Message', messageSchema);

wss.on('connection', (ws, req) => {
    ws.on('message', async (message) => {
        const parsedMessage = JSON.parse(message);
        const { senderId, receiverId, propertyId, content } = parsedMessage;

        const newMessage = new Message({ senderId, receiverId, propertyId, content });
        await newMessage.save();

        wss.clients.forEach(client => {
            if (client.readyState === WebSocket.OPEN && client !== ws) {
                client.send(JSON.stringify(newMessage));
            }
        });
    });
});

app.get('/api/messages/:propertyId', async (req, res) => {
    const { propertyId } = req.params;
    const messages = await Message.find({ propertyId }).sort({ timestamp: 1 });
    res.json(messages);
});

server.listen(3000, () => {
    console.log('Server running on http://localhost:3000');
});
import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { WebSocket } from 'ws';

const Messaging = ({ userId, propertyId }) => {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');
    const ws = useRef(null);

    useEffect(() => {
        const fetchMessages = async () => {
            const response = await axios.get(`http://localhost:3000/api/messages/${propertyId}`);
            setMessages(response.data);
        };
        fetchMessages();

        ws.current = new WebSocket('ws://localhost:3000');
        ws.current.onmessage = (event) => {
            const newMessage = JSON.parse(event.data);
            setMessages(prevMessages => [...prevMessages, newMessage]);
        };

        return () => {
            ws.current.close();
        };
    }, [propertyId]);

    const sendMessage = () => {
        const message = { senderId: userId, receiverId: 'hostId', propertyId, content: input };
        ws.current.send(JSON.stringify(message));
        setInput('');
    };

    return (
        <div>
            <h3>Messages</h3>
            <div>
                {messages.map((msg, index) => (
                    <div key={index} style={{ textAlign: msg.senderId === userId ? 'right' : 'left' }}>
                        <p>{msg.content}</p>
                    </div>
                ))}
            </div>
            <input value={input} onChange={e => setInput(e.target.value)} placeholder="Type a message" />
            <button onClick={sendMessage}>Send</button>
        </div>
    );
};

export default Messaging;































