<h1 align="center">🚗 UHOD - Unattended Human Occupant Detection System</h1>
<p align="center"><strong>Child Presence Detection Alert with IoT, Ultralytics YOLOv11 & Firebase Cloud Messaging</strong></p>

<p align="center">
  <img src="https://img.shields.io/badge/status-active-brightgreen" alt="Status">
  <img src="https://img.shields.io/badge/platform-Android-blue" alt="Platform">
  <img src="https://img.shields.io/badge/backend-Firebase-orange" alt="Firebase">
  <img src="https://img.shields.io/badge/messaging-FCM-red" alt="FCM">
  <img src="https://img.shields.io/badge/language-Java-yellow" alt="Java">
</p>

<hr>

<h2>📱 Overview</h2>
<p>
  <strong>UHOD</strong> (Unattended Human Occupant Detection System) is a smart Android application that integrates with an IoT device to detect and alert users of human presence inside parked vehicles. Designed to prevent suffocation—especially among children—UHOD leverages motion detection and AI (YOLOv11), Firebase Realtime Database, and Firebase Cloud Messaging (FCM) to deliver instant mobile notifications and image alerts.
</p>

<h2>🎯 Key Features</h2>
<ul>
  <li><strong>Real-Time Monitoring:</strong> Detects human presence inside a stationary vehicle using computer vision.</li>
  <li><strong>Firebase Cloud Messaging (FCM):</strong> Sends instant push notifications when human presence and motion is detected.</li>
  <li><strong>Snapshot Delivery:</strong> Captures and sends image evidence from the IoT device using base64-encoded data via Firebase.</li>
  <li><strong>Remote Alarm Activation:</strong> Trigger or silence the car’s alarm system from the mobile app.</li>
  <li><strong>Sleep Mode:</strong> Temporarily disable monitoring (5 mins to 1 hour) when needed.</li>
  <li><strong>Battery Status:</strong> Displays the IoT device’s current battery level in-app.</li>
  <li><strong>Emergency Hotlines:</strong> Quick-dial local authorities or pre-saved emergency contacts.</li>
  <li><strong>Alert Log:</strong> View historical alerts with timestamps and images.</li>
</ul>

<h2>🧠 Technologies Used</h2>
<ul>
  <li><strong>Mobile App:</strong> Android (Java & XML) via Android Studio</li>
  <li><strong>Firebase Services:</strong>
    <ul>
      <li>⚡ Realtime Database – Motion data & image exchange (base64 encoded)</li>
      <li>📩 Firebase Cloud Messaging – Push notifications</li>
      <li>🔐 Firebase Authentication – User account system</li>
    </ul>
  </li>
  <li><strong>IoT Hardware:</strong> Raspberry Pi with USB Camera and Ultralytics YOLOv11 for AI-based detection</li>
  <li><strong>UI Libraries:</strong>
    <ul>
      <li>Glide, Picasso – Efficient image loading</li>
      <li>UCrop – In-app image cropping</li>
      <li>AwesomeSplash – Animated splash screen</li>
      <li>Gotev Speech – Voice command functionality</li>
      <li>CircleImageView, CircleProgress – UI enhancements</li>
    </ul>
  </li>
</ul>

<h2>📦 Project Structure</h2>
<pre>
├── app/
│   ├── java/com.qppd.ibuyit/
│   │   ├── MainActivity.java
│   │   ├── AlertActivity.java
│   │   ├── NotificationService.java
│   │   └── ...
│   └── res/
│       ├── layout/
│       ├── values/
│       │   └── strings.xml
│       └── drawable/
├── google-services.json
├── build.gradle
└── README.md
</pre>

<h2>⚙️ Firebase & FCM Setup</h2>
<ol>
  <li>Create a project in the <a href="https://console.firebase.google.com/">Firebase Console</a>.</li>
  <li>Enable the following services:
    <ul>
      <li>Realtime Database</li>
      <li>Firebase Cloud Messaging (FCM)</li>
      <li>Firebase Authentication</li>
    </ul>
  </li>
  <li>Download and place <code>google-services.json</code> into your project’s <code>/app</code> directory.</li>
  <li>Ensure Firebase Messaging permissions are declared in <code>AndroidManifest.xml</code>.</li>
  <li>Sync Gradle and initialize Firebase in your application class.</li>
</ol>

<h2>🚀 Getting Started</h2>
<ol>
  <li>Clone the repository:
    <pre><code>git clone https://github.com/qppd/Child-Presence-Detection-Alert.git</code></pre>
  </li>
  <li>Open the project in Android Studio.</li>
  <li>Set up your Firebase project and integrate the <code>google-services.json</code> file.</li>
  <li>Ensure the IoT device is active and linked to the same Firebase database.</li>
  <li>Build and run the application on a physical Android device.</li>
</ol>

<h2>🔔 How Notifications Work</h2>
<p>The alert flow is designed for speed and reliability:</p>
<ul>
  <li>IoT device detects human presence (YOLOv11)</li>
  <li>Image and motion data sent to Firebase Realtime Database</li>
  <li>FCM pushes a real-time alert to the mobile app</li>
  <li>The app displays snapshot, plays sound/vibration, and triggers the alarm</li>
</ul>

<h2>📖 License</h2>
<p>
  This project is licensed under the <strong>MIT License</strong>. See <code>LICENSE</code> for full details.
</p>

<h2>🙌 Acknowledgments</h2>
<ul>
  <li>Developed by <strong>Quezon Province Programmer/Developers (QPPD)</strong> as part of a safety-first IoT innovation initiative.</li>
  <li>Special thanks to the Firebase team and open-source contributors worldwide.</li>
  <li>Built with ❤️ for child safety and public welfare in the Philippines.</li>
</ul>
